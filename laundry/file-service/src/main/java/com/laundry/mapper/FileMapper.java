package com.laundry.mapper;

import com.laundry.dto.FileInfo;
import com.laundry.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper {
    @Mapping(target = "id", source = "name")
    File toFile(FileInfo fileInfo);
}
