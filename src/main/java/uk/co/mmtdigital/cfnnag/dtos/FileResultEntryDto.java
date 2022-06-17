package uk.co.mmtdigital.cfnnag.dtos;

import com.google.gson.annotations.SerializedName;

public class FileResultEntryDto {
    @SerializedName("filename")
    private final String filename;
    @SerializedName("file_results")
    private final FileResultDto fileResults;

    public FileResultEntryDto(String filename, FileResultDto fileResults) {
        this.filename = filename;
        this.fileResults = fileResults;
    }

    public FileResultDto getFileResults() {
        return fileResults;
    }
}
