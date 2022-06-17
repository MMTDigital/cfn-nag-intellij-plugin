package uk.co.mmtdigital.cfnnag.cmd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;
import uk.co.mmtdigital.cfnnag.dtos.FileResultEntryDto;
import uk.co.mmtdigital.cfnnag.settings.CfnNagSettingsState;
import uk.co.mmtdigital.cfnnag.cmd.utils.CommandLineWithInput;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CfnNagRunner {
    private CfnNagRunner() {
    }

    private static final Logger LOG = Logger.getInstance(CfnNagRunner.class);
    private static final int TIME_OUT = (int) TimeUnit.SECONDS.toMillis(120L);

    public static CfnNagOutput run(@NotNull CfnNagSettingsState settingsState, @NotNull String cwd, @NotNull String file, String content) {
        CfnNagOutput result;
        try {
            GeneralCommandLine commandLine = createCommandLine(settingsState.executablePath, cwd);
            List<String> parameters = new ArrayList<>();

            if (!settingsState.ruleDirectory.isBlank()){
                parameters.add("--rule-directory=" + settingsState.ruleDirectory);
            }
            if (!settingsState.blacklistPath.isBlank()){
                parameters.add("--blacklist-path=" + settingsState.blacklistPath);
            }
            parameters.add("--output-format=json");

            if (content == null) {
                parameters.add(file);
                commandLine = commandLine.withParameters(parameters.toArray(new String[]{}));
            } else {
                commandLine = ((CommandLineWithInput) commandLine)
                        .withInput(content)
                        .withParameters(parameters);
            }
            ProcessOutput out = execute(commandLine);
            try {
                result = new CfnNagOutput(parse(out.getStdout()), out.getStderr());
            } catch (Exception e) {
                result = new CfnNagOutput(out.getStdout());
            }
        } catch (Exception e) {
            LOG.error("Problem with running exe", e);
            result = new CfnNagOutput(e.toString());
        }
        return result;
    }

    private static List<FileResultEntryDto> parse(String json) {
        Gson g = new GsonBuilder().create();
        Type listType = new TypeToken<List<FileResultEntryDto>>() {
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
    private static ProcessOutput execute(@NotNull GeneralCommandLine commandLine) throws ExecutionException {
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
