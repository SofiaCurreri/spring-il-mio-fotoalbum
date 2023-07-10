package org.java.lessons.springilmiofotoalbum.controller;

import org.java.lessons.springilmiofotoalbum.exception.PhotoNotFoundException;
import org.java.lessons.springilmiofotoalbum.model.Photo;
import org.java.lessons.springilmiofotoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/files")
public class FileController {

    @Autowired
    PhotoService photoService;

    //metodo che cerca foto per id e ne restituisce l' immagine
    @GetMapping("/image/{photoId}")
    public ResponseEntity<byte[]> getPhotoImage(@PathVariable Integer photoId) {
        try {
            Photo photo = photoService.getById(photoId);
            MediaType mediaType = MediaType.IMAGE_JPEG;
            return ResponseEntity.ok().contentType(mediaType).body(photo.getImageFile());
        } catch (PhotoNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
