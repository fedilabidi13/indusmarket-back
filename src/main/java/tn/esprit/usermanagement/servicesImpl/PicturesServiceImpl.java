package tn.esprit.usermanagement.servicesImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.usermanagement.entities.Pictures;
import tn.esprit.usermanagement.repositories.PicturesRepo;
import tn.esprit.usermanagement.services.PicturesService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PicturesServiceImpl implements PicturesService {
    @Autowired
    PicturesRepo picturesRepo;

    @Override
    public Pictures savePicture(MultipartFile file) throws IOException {
        Pictures picture = new Pictures();
        picture.setData(file.getBytes());
        return picturesRepo.save(picture);
    }

    @Override
    public List<Pictures> savePictures(List<MultipartFile> files) throws IOException  {
        List<Pictures> picturesList = new ArrayList<>();
        for (MultipartFile file : files) {
            Pictures pictures = new Pictures();
            pictures.setData(file.getBytes());
            picturesList.add(pictures);
        }
        return picturesRepo.saveAll(picturesList);
    }

    @Override
    public List<Pictures> getAllPictures() {
        return picturesRepo.findAll();
    }

    @Override
    public Pictures getPictureById(Integer id) {
        return picturesRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
    }

    @Override
    public void deleteImage(Pictures pictures) {
        picturesRepo.delete(pictures);
    }


}
