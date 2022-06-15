package uk.me.pilgrim.cfnnag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import org.apache.groovy.util.Arrays;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CheckRunner {
    private CheckRunner() {
    }

    private static final Logger LOG = Logger.getInstance(CheckRunner.class);
    private static final int TIME_OUT = (int) TimeUnit.SECONDS.toMillis(120L);

    public static CfnNagResult runCheck(@NotNull String exe, @NotNull String cwd, @NotNull String file, String content) {
        CfnNagResult result;
        try {
            GeneralCommandLine commandLine = createCommandLine(exe, cwd);
            String[] parameters = {
                    "--blacklist-path=/Users/benjaminpilgrim/dev/Vodafone/cfn-nag-custom-rules/config/warnings-blacklist.yaml",
                    "--rule-directory=/Users/benjaminpilgrim/dev/Vodafone/cfn-nag-custom-rules/lib/rules",
                    "--output-format=json"
            };

            if (content == null) {
                commandLine = commandLine.withParameters(Arrays.concat(parameters, new String[]{file}));
            } else {
                commandLine = ((CommandLineWithInput) commandLine)
                        .withInput(content)
                        .withParameters(parameters);
            }
            ProcessOutput out = execute(commandLine);
            try {
                result = new CfnNagResult(parse(out.getStdout()), out.getStderr());
            } catch (Exception e) {
                result = new CfnNagResult(out.getStdout());
            }
        } catch (Exception e) {
            LOG.error("Problem with running exe", e);
            result = new CfnNagResult(e.toString());
        }
        return result;
    }

    private static List<CfnNagResult.FileResultEntry> parse(String json) {
        Gson g = new GsonBuilder().create();
        Type listType = new TypeToken<List<CfnNagResult.FileResultEntry>>() {
        }.getType();
        return g.fromJson(json, listType);
    }

    @NotNull
    private static CommandLineWithInput createCommandLine(@NotNull String exe, @NotNull String cwd) {
        CommandLineWithInput commandLine = new CommandLineWithInput();
        commandLine.setExePath(exe);
        commandLine.setWorkDirectory(cwd);
        return commandLine;
    }

    @NotNull
    public static ProcessOutput execute(@NotNull GeneralCommandLine commandLine) throws ExecutionException {
        LOG.info("Running command: " + commandLine.getCommandLineString());
        Process process = commandLine.createProcess();
        OSProcessHandler processHandler = new ColoredProcessHandler(process, commandLine.getCommandLineString(), StandardCharsets.UTF_8);
        final ProcessOutput output = new ProcessOutput();
        processHandler.addProcessListener(new ProcessAdapter() {
            public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
                if (outputType.equals(ProcessOutputTypes.STDERR)) {
                    output.appendStderr(event.getText());
                } else if (!outputType.equals(ProcessOutputTypes.SYSTEM)) {
                    output.appendStdout(event.getText());
                }
            }
        });
        processHandler.startNotify();
        if (processHandler.waitFor(TIME_OUT)) {
            output.setExitCode(process.exitValue());
        } else {
            processHandler.destroyProcess();
            output.setTimeout();
        }
        if (output.isTimeout()) {
            throw new ExecutionException("Command '" + commandLine.getCommandLineString() + "' is timed out.");
        }
        return output;
    }
}
