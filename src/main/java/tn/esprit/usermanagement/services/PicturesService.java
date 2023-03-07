package tn.esprit.usermanagement.services;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Pictures;

import java.io.IOException;
import java.util.List;

public interface PicturesService {
    public Pictures savePicture(MultipartFile file) throws IOException;
    public List<Pictures> savePictures(List<MultipartFile> files) throws IOException;
    public List<Pictures> getAllPictures();
    public Pictures getPictureById(Integer id);
    public void deleteImage(Pictures pictures);



}
