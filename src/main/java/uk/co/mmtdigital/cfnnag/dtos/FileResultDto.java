package uk.co.mmtdigital.cfnnag.dtos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FileResultDto {
    @SerializedName("failure_count")
    private final int failureCount;
    @SerializedName("violations")
    private final List<ViolationDto> violations;

    public FileResultDto(int failureCount, List<ViolationDto> violations) {
        this.failureCount = failureCount;
        this.violations = violations;
    }

    public List<ViolationDto> getViolations() {
        return violations;
    }
}
