package com.findex.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EntityMapper <D, E> {

  E toEntity(D dto);

  D toDto(E entity);

}