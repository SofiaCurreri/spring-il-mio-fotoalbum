package org.java.lessons.springilmiofotoalbum.api;

import jakarta.validation.Valid;
import org.java.lessons.springilmiofotoalbum.model.Photo;
import org.java.lessons.springilmiofotoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin //permette a client di front-end di accedere
@RequestMapping("api/v1/photos") //v1 è per la versione, così hai un punto facile da cui cambiarla
public class PhotoRestController {

    @Autowired
    PhotoRepository photoRepository;

    //servizio per lista foto
    @GetMapping
    public List<Photo> index() {
        return photoRepository.findAll();
    }

    //servizio per dettaglio foto
    @GetMapping("/{id}")
    public Photo get(@PathVariable Integer id) {
        Optional<Photo> photo = photoRepository.findById(id);
        if (photo.isPresent()) {
            return photo.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    //servizio per creare nuova foto; che arriva come Json nel RequestBody
    @PostMapping
    //solo POST e PUT hanno Body
    public Photo create(@Valid @RequestBody Photo photo, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.toString());
//        }
        return photoRepository.save(photo);
    }

    //servizio per cancellare foto
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        photoRepository.deleteById(id);
    }

    //nella put devo passare id della risorsa da modificare e poi devo dare body di una post
    // nel quale metto tutti dati che andranno a sostituire i dati della risorsa con l' id passato nel path
    @PutMapping("/{id}")
    public Photo update(@PathVariable Integer id, @Valid @RequestBody Photo photo) {
        photo.setId(id);
        return photoRepository.save(photo);
    }


}
