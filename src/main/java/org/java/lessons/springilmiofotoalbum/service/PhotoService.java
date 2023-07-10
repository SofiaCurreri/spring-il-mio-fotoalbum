package org.java.lessons.springilmiofotoalbum.service;

import org.java.lessons.springilmiofotoalbum.dto.PhotoForm;
import org.java.lessons.springilmiofotoalbum.exception.PhotoNotFoundException;
import org.java.lessons.springilmiofotoalbum.model.Photo;
import org.java.lessons.springilmiofotoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {

    @Autowired
    PhotoRepository photoRepository;

    //metodo che restituisce tutte foto, filtrate o meno a seconda del param
    public List<Photo> getAll(Optional<String> searchInput) {
        if (searchInput.isEmpty()) {
            return photoRepository.findAll();
        } else {
            return photoRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchInput.get(), searchInput.get());
        }
    }

    //metodo che restituisce foto per id o un' eccezione se non la trova
    public Photo getById(Integer id) throws PhotoNotFoundException {
        Optional<Photo> photoOpt = photoRepository.findById(id);
        if (photoOpt.isPresent()) {
            return photoOpt.get();
        } else {
            //siamo in uno strato di Service quindi non è opportuno tirare una ResponseStatusException
            throw new PhotoNotFoundException("Photo with id = " + id + " not found");
        }
    }

    //metodo che salva nuova foto a partire da quella passata come param
    public Photo create(Photo photo) {
        //genero foto da salvare
        Photo photoToPersist = new Photo();

        photoToPersist.setCreatedAt(LocalDateTime.now());

        photoToPersist.setTitle(photo.getTitle());
        photoToPersist.setDescription(photo.getDescription());
        photoToPersist.setVisible(photo.getVisible());
        photoToPersist.setCategories(photo.getCategories());
        photoToPersist.setImageFile(photo.getImageFile());

        return photoRepository.save(photoToPersist);
    }

    //metodo che crea Photo a partire da un PhotoForm
    public Photo create(PhotoForm photoForm) {
        //converto PhotoForm in Photo
        Photo photo = mapPhotoFormToPhoto(photoForm);
        //salvo Photo su db
        return create(photo);
    }

    //metodo per creare un PhotoForm a partire da id di una Photo salvata su db
    public PhotoForm getPhotoFormById(Integer id) throws PhotoNotFoundException {
        Photo photo = getById(id);
        return mapPhotoToPhotoForm(photo);
    }


    //metodo per eliminare singola foto
    public void delete(Integer id) {
        photoRepository.deleteById(id);
    }

    //metodo per salvare modifiche foto
    public Photo update(Integer id, Photo photo) {
        photo.setId(id);
        return photoRepository.save(photo);
    }

    public Photo update(Integer id, PhotoForm photoForm) throws PhotoNotFoundException {
        Photo photo = mapPhotoFormToPhoto(photoForm);

        if (photoForm.getImageFile() == null || photoForm.getImageFile().isEmpty()) {
            Optional<Photo> photoWithImage = photoRepository.findById(id);
            if (photoWithImage.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo with id = " + id + " not found :(");
            }
            photo.setImageFile(photoWithImage.get().getImageFile());
        }
        photoForm.setId(id);
        return photoRepository.save(photo);
    }

    //metodo per convertire tipo PhotoForm in tipo Photo
    private Photo mapPhotoFormToPhoto(PhotoForm photoForm) {
        //creo photo nuova vuota
        Photo photo = new Photo();

        photo.setId(photoForm.getId());
        photo.setTitle(photoForm.getTitle());
        photo.setDescription(photoForm.getDescription());
        photo.setVisible(photoForm.getVisible());
        photo.setCategories(photoForm.getCategories());

        //converto campo imageFile
        photo.setImageFile(multipartFileToByteArray(photoForm.getImageFile()));

        return photo;
    }

    //metodo per convertire tipo Photo in tipo PhotoForm
    private PhotoForm mapPhotoToPhotoForm(Photo photo) {
        //creo photo nuova vuota
        PhotoForm photoForm = new PhotoForm();

        photoForm.setId(photo.getId());
        photoForm.setTitle(photo.getTitle());
        photoForm.setDescription(photo.getDescription());
        photoForm.setVisible(photo.getVisible());
        photoForm.setCategories(photo.getCategories());

        return photoForm;
    }

    private byte[] multipartFileToByteArray(MultipartFile mpf) {
        byte[] bytes = null;
        if (mpf != null && !mpf.isEmpty()) {
            try {
                bytes = mpf.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }
}
