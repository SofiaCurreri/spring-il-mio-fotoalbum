package org.java.lessons.springilmiofotoalbum.api;

import org.java.lessons.springilmiofotoalbum.model.Photo;
import org.java.lessons.springilmiofotoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
