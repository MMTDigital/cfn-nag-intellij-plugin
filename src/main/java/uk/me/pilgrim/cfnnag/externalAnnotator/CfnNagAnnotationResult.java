package uk.me.pilgrim.cfnnag.externalAnnotator;

import uk.me.pilgrim.cfnnag.cmd.CfnNagOutput;
import uk.me.pilgrim.cfnnag.dtos.FileResultDto;
import uk.me.pilgrim.cfnnag.dtos.FileResultEntryDto;
import uk.me.pilgrim.cfnnag.dtos.ViolationDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CfnNagAnnotationResult {

    private final CfnNagInitialInfo input;
    private final CfnNagOutput output;

    public CfnNagAnnotationResult(CfnNagInitialInfo input, CfnNagOutput output) {
        this.input = input;
        this.output = output;
    }

    public List<ViolationDto> getViolations() {
        return Optional.ofNullable(output)
                .stream()
                .map(CfnNagOutput::getFileResultEntries)
                .flatMap(Collection::stream)
                .map(FileResultEntryDto::getFileResults)
                .map(FileResultDto::getViolations)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public CfnNagInitialInfo getInput() {
        return input;
    }

}