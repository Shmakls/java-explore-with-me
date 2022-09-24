package ru.andrianov.emw.compilations.mapper;

import ru.andrianov.emw.compilations.dto.CompilationDto;
import ru.andrianov.emw.compilations.model.Compilation;

import java.util.Optional;

public class CompilationMapper {

    public static Compilation fromDto(CompilationDto compilationDto) {

        Compilation compilation = new Compilation();

        Optional.ofNullable(compilationDto.getId()).ifPresent(compilation::setId);

        compilation.setPinned(compilationDto.isPinned());
        compilation.setTitle(compilationDto.getTitle());

        return compilation;

    }

    public static CompilationDto toDto(Compilation compilation) {

        CompilationDto compilationDto = new CompilationDto();

        Optional.ofNullable(compilation.getId()).ifPresent(compilationDto::setId);

        compilationDto.setPinned(compilation.isPinned());
        compilationDto.setTitle(compilation.getTitle());

        return compilationDto;

    }

}
