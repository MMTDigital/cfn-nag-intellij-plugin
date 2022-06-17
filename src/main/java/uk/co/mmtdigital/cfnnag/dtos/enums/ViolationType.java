package uk.co.mmtdigital.cfnnag.dtos.enums;

import com.intellij.lang.annotation.HighlightSeverity;

public enum ViolationType {
    FAIL,
    WARN;

    public HighlightSeverity getHighlightSeverity() {
        switch (this) {
            case FAIL:
                return HighlightSeverity.ERROR;
            case WARN:
                return HighlightSeverity.WARNING;
            default:
                throw new IllegalArgumentException("No HighlightSeverity for ViolationType " + name());
        }
    }
}
