package uk.co.mmtdigital.cfnnag.externalAnnotator;

import com.intellij.psi.PsiFile;

public class CfnNagInitialInfo {
    private final PsiFile psiFile;
    private final String fileContent;

    public CfnNagInitialInfo(PsiFile psiFile, String fileContent) {
        this.psiFile = psiFile;
        this.fileContent = fileContent;
    }

    public String getCwd() {
        return psiFile.getProject().getBasePath();
    }

    public String getFilePath() {
        return psiFile.getVirtualFile().getPath();
    }

    public String getFileContent() {
        return fileContent;
    }

}