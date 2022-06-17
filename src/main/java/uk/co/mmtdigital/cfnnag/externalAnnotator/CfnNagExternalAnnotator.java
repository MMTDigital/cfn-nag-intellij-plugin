package uk.co.mmtdigital.cfnnag.externalAnnotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.DocumentUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.mmtdigital.cfnnag.cmd.CfnNagOutput;
import uk.co.mmtdigital.cfnnag.cmd.CfnNagRunner;
import uk.co.mmtdigital.cfnnag.dtos.ViolationDto;
import uk.co.mmtdigital.cfnnag.settings.CfnNagSettingsState;

public class CfnNagExternalAnnotator extends ExternalAnnotator<CfnNagInitialInfo, CfnNagAnnotationResult> {
    private static final Logger LOG = Logger.getInstance(CfnNagExternalAnnotator.class);

    @Override
    public @Nullable CfnNagInitialInfo collectInformation(@NotNull PsiFile file) {
        if (file.getContext() != null) {
            return null;
        }
        VirtualFile virtualFile = file.getVirtualFile();
        if (virtualFile == null || !virtualFile.isInLocalFileSystem()) {
            return null;
        }
        if (file.getViewProvider() instanceof MultiplePsiFilesPerDocumentFileViewProvider) {
            return null;
        }
        if (!isFile(file)) {
            return null;
        }
        FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
        boolean fileModified = fileDocumentManager.isFileModified(virtualFile);
        return new CfnNagInitialInfo(file, fileModified ? file.getText() : null);
    }

    @Override
    public @Nullable CfnNagAnnotationResult doAnnotate(CfnNagInitialInfo input) {
        try {
            CfnNagOutput result = CfnNagRunner.run(CfnNagSettingsState.getInstance(), input.getCwd(), input.getFilePath(), input.getFileContent());

            if (StringUtils.isNotEmpty(result.getErrorOutput())) {
                LOG.error("Error running  inspection: ", result.getErrorOutput());
                Notifications.Bus.notify(new Notification("cfn-nag", "Cfn-nag", "Error running inspection: " + result.getErrorOutput(), NotificationType.WARNING, null), null);
                return null;
            }
            return new CfnNagAnnotationResult(input, result);
        } catch (Exception e) {
            LOG.error("Error running  inspection: ", e);
            Notifications.Bus.notify(new Notification("cfn-nag", "Cfn-nag", "Error running inspection: " + e.getMessage(), NotificationType.ERROR, null), null);
        }
        return null;
    }

    private static boolean isFile(PsiFile file) {
        String fileType = file.getFileType().getName().toLowerCase();
        switch(fileType) {
            case "yaml":
            case "json":
                return file.getText().contains("AWSTemplateFormatVersion");
            default:
                return false;
        }
    }

    @Override
    public void apply(@NotNull PsiFile file, CfnNagAnnotationResult annotationResult, @NotNull AnnotationHolder holder) {
        if (annotationResult == null) {
            return;
        }
        Document document = PsiDocumentManager.getInstance(file.getProject()).getDocument(file);
        if (document == null) {
            return;
        }

        for (ViolationDto violation : annotationResult.getViolations()) {
            createAnnotation(holder, document, violation);
        }
    }

    private void createAnnotation(@NotNull AnnotationHolder holder, @NotNull Document document, @NotNull ViolationDto violation) {
        for (int errorLine : violation.getLineNumbers()) {

            errorLine -= 1;

            if (errorLine < 0 || errorLine >= document.getLineCount()) {
                return;
            }

            int lineStartOffset = document.getLineStartOffset(errorLine);
            int lineEndOffset = document.getLineEndOffset(errorLine);

            TextRange range;
            int start = DocumentUtil.getFirstNonSpaceCharOffset(document, lineStartOffset, lineEndOffset);
            range = new TextRange(start, lineEndOffset);

            String tooltip = violation.getMessage();
            if (!tooltip.endsWith("\n")) tooltip = tooltip + "\n";
            tooltip = tooltip + "\n[" + violation.getId() + ": " + violation.getName() + "]";

            holder
                    .newAnnotation(violation.getType().getHighlightSeverity(), "[" + violation.getId() + "] " + violation.getName())
                    .range(range)
                    .tooltip(tooltip)
                    .create();
        }
    }
}
