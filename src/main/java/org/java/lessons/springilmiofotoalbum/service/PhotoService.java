package org.java.lessons.springilmiofotoalbum.service;

import org.java.lessons.springilmiofotoalbum.exception.PhotoNotFoundException;
import org.java.lessons.springilmiofotoalbum.model.Photo;
import org.java.lessons.springilmiofotoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        photoToPersist.setUrl(photo.getUrl());
        photoToPersist.setVisible(photo.getVisible());
        photoToPersist.setCategories(photo.getCategories());

        return photoRepository.save(photoToPersist);
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
}
