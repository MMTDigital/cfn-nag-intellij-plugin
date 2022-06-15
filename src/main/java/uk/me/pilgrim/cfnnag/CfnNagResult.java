package uk.me.pilgrim.cfnnag;

import com.google.gson.annotations.SerializedName;
import com.intellij.lang.annotation.HighlightSeverity;

import java.util.Collections;
import java.util.List;

public class CfnNagResult {

    private final List<FileResultEntry> fileResultEntries;
    private final String errorOutput;

    public CfnNagResult(List<FileResultEntry> fileResultEntries, String errorOutput) {
        this.fileResultEntries = fileResultEntries;
        this.errorOutput = errorOutput;
    }

    public CfnNagResult(String errorOutput){
        this(Collections.emptyList(), errorOutput);
    }

    public List<FileResultEntry> getFileResultEntries() {
        return fileResultEntries;
    }

    public String getErrorOutput() {
        return errorOutput;
    }

    public static class FileResultEntry {
        @SerializedName("filename")
        private final String filename;
        @SerializedName("file_results")
        private final FileResult fileResults;

        public FileResultEntry(String filename, FileResult fileResults) {
            this.filename = filename;
            this.fileResults = fileResults;
        }

        public FileResult getFileResults() {
            return fileResults;
        }
    }


    public static class FileResult {
        @SerializedName("failure_count")
        private final int failureCount;
        @SerializedName("violations")
        private final List<Violation> violations;

        public FileResult(int failureCount, List<Violation> violations) {
            this.failureCount = failureCount;
            this.violations = violations;
        }

        public List<Violation> getViolations() {
            return violations;
        }
    }

    public enum ViolationType{
        FAIL,
        WARN
        ;

        public HighlightSeverity getHighlightSeverity(){
            switch (this){
                case FAIL: return HighlightSeverity.ERROR;
                case WARN: return HighlightSeverity.WARNING;
                default: throw new IllegalArgumentException("No HighlightSeverity for ViolationType " + name());
            }
        }
    }

    public static class Violation {
        @SerializedName("id")
        private final String id;
        @SerializedName("name")
        private final String name;
        @SerializedName("type")
        private final ViolationType type;
        @SerializedName("message")
        private final String message;
        @SerializedName("logical_resource_ids")
        private final List<String> logicalResourceIds;
        @SerializedName("line_numbers")
        private final List<Integer> lineNumbers;
        @SerializedName("element_types")
        private final List<String> elementTypes;

        private Violation(String id, String name, ViolationType type, String message, List<String> logicalResourceIds, List<Integer> lineNumbers, List<String> elementTypes) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.message = message;
            this.logicalResourceIds = logicalResourceIds;
            this.lineNumbers = lineNumbers;
            this.elementTypes = elementTypes;
        }

        @Override
        public String toString() {
            return "Violation{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", message='" + message + '\'' +
                    ", logicalResourceIds=" + logicalResourceIds +
                    ", lineNumbers=" + lineNumbers +
                    ", elementTypes=" + elementTypes +
                    '}';
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public ViolationType getType() {
            return type;
        }

        public String getMessage() {
            return message;
        }

        public List<String> getLogicalResourceIds() {
            return logicalResourceIds;
        }

        public List<Integer> getLineNumbers() {
            return lineNumbers;
        }

        public List<String> getElementTypes() {
            return elementTypes;
        }
    }
}
