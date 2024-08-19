package hu.remzso.tarantulaForum.services;

import hu.remzso.tarantulaForum.entities.FileEntity;
import hu.remzso.tarantulaForum.repositories.FileEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileEntityServiceImpl implements FileEntityService{
   private FileEntityRepository fileEntityRepository;
   public FileEntityServiceImpl(FileEntityRepository fileEntityRepository) {
        this.fileEntityRepository = fileEntityRepository;
   }
    @Override
    public List<String> getImagePaths(Long tarantulaId) {

        List<String> imagesPaths = new ArrayList<>();
        for (FileEntity fileEntity : fileEntityRepository.findAllByTarantulaID(tarantulaId).get()) {
            imagesPaths.add(fileEntity.getPath());
        }
        return List.of();
    }

    @Override
    public String getImagePath(Long tarantulaId) {
        return fileEntityRepository.findFirstByTarantulaIDOrderByIdAsc(tarantulaId).get().getPath();
    }

    @Override
    public FileEntity saveFileEntity(FileEntity fileEntity) {
        return fileEntityRepository.save(fileEntity);
    }

    @Override
    public void saveImages(List<MultipartFile> images, Long tarantulaId) {


    }
}
