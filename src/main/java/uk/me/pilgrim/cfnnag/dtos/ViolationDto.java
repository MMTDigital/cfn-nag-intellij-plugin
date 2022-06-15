package uk.me.pilgrim.cfnnag.dtos;

import com.google.gson.annotations.SerializedName;
import uk.me.pilgrim.cfnnag.dtos.enums.ViolationType;

import java.util.List;

public class ViolationDto {
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

    private ViolationDto(String id, String name, ViolationType type, String message, List<String> logicalResourceIds, List<Integer> lineNumbers, List<String> elementTypes) {
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
