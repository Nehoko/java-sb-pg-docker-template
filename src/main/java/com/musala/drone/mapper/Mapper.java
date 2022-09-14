package com.musala.drone.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface Mapper<ENTITY, DTO> {

    DTO toDto(ENTITY entity);
    ENTITY toEntity(DTO dto);

    default List<DTO> toDtoList(List<ENTITY> entityList) {
        if (entityList == null) return null;
        if (entityList.isEmpty()) return new ArrayList<>();
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    default List<ENTITY> toEntityList(List<DTO> dtoList) {

        if (dtoList == null) return null;
        if (dtoList.isEmpty()) return new ArrayList<>();
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
