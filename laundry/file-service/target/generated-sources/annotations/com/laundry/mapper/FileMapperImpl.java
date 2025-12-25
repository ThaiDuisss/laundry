package com.laundry.mapper;

import com.laundry.dto.FileInfo;
import com.laundry.entity.File;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-21T11:17:16+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class FileMapperImpl implements FileMapper {

    @Override
    public File toFile(FileInfo fileInfo) {
        if ( fileInfo == null ) {
            return null;
        }

        File.FileBuilder file = File.builder();

        file.id( fileInfo.getName() );
        file.url( fileInfo.getUrl() );
        file.path( fileInfo.getPath() );
        file.size( fileInfo.getSize() );
        file.contentType( fileInfo.getContentType() );
        file.md5CheckSum( fileInfo.getMd5CheckSum() );

        return file.build();
    }
}
