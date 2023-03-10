package tn.esprit.usermanagement.servicesImpl.ForumServiceImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Pictures;
import tn.esprit.usermanagement.repositories.PicturesRepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class ImageServiceImpl {
    PicturesRepo picturesRepo;
    public List<Pictures> addimages(List<MultipartFile> files) throws IOException {
        // String ch = DoOCR(image);
        // BufferedImage bi = ImageIO.read(image.getInputStream());
        // if (Filtrage_bad_word(ch) == 0 ) {
        //    Map result = cloudImage.upload(image);

        //  Media media = new Media((String)
        //        result.get("original_filename")
        //      , (String) result.get("url"),
        //    (String) result.get("public_id"));
        // media.setPost(p);
        //mediaService.save(media);
	/*
	Set<Media> lp = p.getMedias();
	lp.add(media);
	p.setMedias(lp);
	*/
        //postRepo.save(p);

        List<Pictures> picturesList = new ArrayList<>();
        for (MultipartFile file : files) {
            Pictures picture = new Pictures();
            byte[] data = file.getBytes();
            if (data.length > 500) { // check if the file is too large
                data = Arrays.copyOfRange(data, 0, 500); // truncate the data
            }
            picture.setData(data);
            picturesList.add(picture);
        }


        return picturesRepo.saveAll(picturesList);
        //}
        // else return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("U r Image Content interdit word");

    }

}
