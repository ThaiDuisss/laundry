package com.laundry.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface BaseMapper <E, Req, Res> {
    E toEntity(Req request);       // Request -> Entity
    Res toResponse(E entity);      // Entity -> Response
    List<Res> toResponseList(List<E> entities);
    void updateEntityFromRequest(Req request, @MappingTarget E entity);
}
