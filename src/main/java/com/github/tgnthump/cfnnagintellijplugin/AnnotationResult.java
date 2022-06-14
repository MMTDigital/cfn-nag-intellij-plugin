package com.github.tgnthump.cfnnagintellijplugin;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class AnnotationResult {

    private final CheckAnnotationInput input;
    private final CfnNagResult result;

    AnnotationResult(CheckAnnotationInput input, CfnNagResult result) {
        this.input = input;
        this.result = result;
    }

    public List<CfnNagResult.Violation> getIssues() {
        return Optional.ofNullable(result)
                .stream()
                .map(CfnNagResult::getFileResultEntries)
                .flatMap(Collection::stream)
                .map(CfnNagResult.FileResultEntry::getFileResults)
                .map(CfnNagResult.FileResult::getViolations)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public CheckAnnotationInput getInput() {
        return input;
    }

}