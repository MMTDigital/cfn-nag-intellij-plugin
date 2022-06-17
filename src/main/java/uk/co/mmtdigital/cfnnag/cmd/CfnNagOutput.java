package uk.co.mmtdigital.cfnnag.cmd;

import uk.co.mmtdigital.cfnnag.dtos.FileResultEntryDto;

import java.util.Collections;
import java.util.List;

public class CfnNagOutput {

    private final List<FileResultEntryDto> fileResultEntries;
    private final String errorOutput;

    public CfnNagOutput(List<FileResultEntryDto> fileResultEntries, String errorOutput) {
        this.fileResultEntries = fileResultEntries;
        this.errorOutput = errorOutput;
    }

    public CfnNagOutput(String errorOutput){
        this(Collections.emptyList(), errorOutput);
    }

    public List<FileResultEntryDto> getFileResultEntries() {
        return fileResultEntries;
    }

    public String getErrorOutput() {
        return errorOutput;
    }

}
