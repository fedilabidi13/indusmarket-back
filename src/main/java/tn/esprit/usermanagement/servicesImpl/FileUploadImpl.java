package tn.esprit.usermanagement.servicesImpl;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Media;
import tn.esprit.usermanagement.repositories.MediaRepo;
import tn.esprit.usermanagement.services.FileUpload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Service
public class FileUploadImpl implements FileUpload {
    @Autowired
    Cloudinary cloudinary;
    @Autowired
    MediaRepo mediaRepo;
    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
    }
    public List<String> uploadFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            Media media = new Media();
            String url = cloudinary.uploader()
                    .upload(multipartFile.getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString()))
                    .get("url")
                    .toString();
            urls.add(url);
            media.setImagenUrl(url);
            mediaRepo.save(media);
        }
        return urls;
    }
}
