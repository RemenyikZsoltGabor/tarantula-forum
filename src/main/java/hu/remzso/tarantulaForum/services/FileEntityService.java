package hu.remzso.tarantulaForum.services;

import hu.remzso.tarantulaForum.entities.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileEntityService {
    List<String> getImagePaths(Long tarantulaId);
    String getImagePath(Long tarantulaId);
    FileEntity saveFileEntity(FileEntity fileEntity);
    void saveImages(List<MultipartFile> images, Long id);
}
