package service;// FileServiceTest.java

import com.laundry.dto.FileInfo;
import com.laundry.dto.response.FileData;
import com.laundry.dto.response.FileResponse;
import com.laundry.entity.File;
import com.laundry.exception.AppException;
import com.laundry.mapper.FileMapper;
import com.laundry.repository.FileMgmtRepository;
import com.laundry.repository.FileRepository;
import com.laundry.service.FileService;
import com.laundry.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.core.io.Resource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileMgmtRepository fileMgmtRepository;

    @Mock
    private FileMapper fileMapper;

    @InjectMocks
    private FileService fileService;

    @Test
    void uploadFile_shouldStoreFile_andSaveMetadata() throws Exception {
        MockMultipartFile multipart = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
        long userId = 123L;

        FileInfo info = FileInfo.builder()
                .name("uuid-test.txt")
                .size(multipart.getSize())
                .contentType(multipart.getContentType())
                .md5CheckSum("md5")
                .path("/tmp/uuid-test.txt")
                .url("http://host/files/uuid-test.txt")
                .build();

        File mappedEntity = File.builder()
                .id(null)
                .path(info.getPath())
                .contentType(info.getContentType())
                .url(info.getUrl())
                .size(info.getSize())
                .build();

        when(fileRepository.store(any())).thenReturn(info);
        when(fileMapper.toFile(info)).thenReturn(mappedEntity);
        when(fileMgmtRepository.save(any(File.class))).thenAnswer(i -> i.getArgument(0));

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getIdFromHeader).thenReturn(userId);

            FileResponse response = fileService.uploadFile(multipart);

            assertNotNull(response);
            assertEquals("test.txt", response.getOriginalFileName());
            assertEquals(info.getUrl(), response.getUrl());

            ArgumentCaptor<File> captor = ArgumentCaptor.forClass(File.class);
            verify(fileMgmtRepository, times(1)).save(captor.capture());
            File saved = captor.getValue();
            assertEquals(userId, saved.getOwnerId());
            assertEquals(info.getUrl(), saved.getUrl());
            verify(fileRepository, times(1)).store(any());
            verify(fileMapper, times(1)).toFile(info);
        }
    }

    @Test
    void downloadFile_shouldReturnData_whenExists() throws Exception {
        String fileName = "my-file-id";
        byte[] content = "file-bytes".getBytes();
        File fileEntity = File.builder()
                .id(fileName)
                .path("/tmp/my-file")
                .contentType("text/plain")
                .build();

        when(fileMgmtRepository.findById(fileName)).thenReturn(Optional.of(fileEntity));
        Resource resource = new ByteArrayResource(content);
        when(fileRepository.read(fileEntity)).thenReturn(resource);

        FileData data = fileService.downloadFile(fileName);

        assertNotNull(data);
        assertEquals("text/plain", data.getContentType());
        assertNotNull(data.getResource());
        assertArrayEquals(content, data.getResource().getInputStream().readAllBytes());

        verify(fileMgmtRepository, times(1)).findById(fileName);
        verify(fileRepository, times(1)).read(fileEntity);
    }

    @Test
    void downloadFile_shouldThrowAppException_whenNotFound() {
        String fileName = "missing";
        when(fileMgmtRepository.findById(fileName)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> fileService.downloadFile(fileName));
        // if AppException exposes the error enum, optionally assert it:
        // assertEquals(ErrorEnum.USER_NOT_FOUND, ex.getErrorEnum());
        verify(fileMgmtRepository, times(1)).findById(fileName);
        verifyNoInteractions(fileRepository);
    }
}

