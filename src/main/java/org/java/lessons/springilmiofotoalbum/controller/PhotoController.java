package org.java.lessons.springilmiofotoalbum.controller;

import org.java.lessons.springilmiofotoalbum.model.Photo;
import org.java.lessons.springilmiofotoalbum.repository.CategoryRepository;
import org.java.lessons.springilmiofotoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    //controller per mostrare lista foto visibili e per filtrare la ricerca
    @GetMapping
    public String list(@RequestParam(name = "keyword", required = false) String searchString, Model model) {
        List<Photo> listPhotos;
        if (searchString == null || searchString.isBlank()) {
            listPhotos = photoRepository.findAll();
        } else {
            listPhotos = photoRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchString, searchString);
        }

        if (listPhotos.isEmpty()) {
            model.addAttribute("message", "No photos available, sorry");
        }

        model.addAttribute("listPhotos", listPhotos);
        model.addAttribute("searchInput", searchString == null ? "" : searchString);
        return "index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer photoId, Model model) {
        //.findById gestisce un oggetto di tipo Optional che gestisce anche caso in cui arrivi un Id non sistente
        Optional<Photo> result = photoRepository.findById(photoId);
        if (result.isPresent()) {
            model.addAttribute("photo", result.get());
            return "show";
        } else {
            //ritorno http status 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Photo with id = " + photoId + " not found, sorry!");
        }
    }

    //controller che restituisce pagina con form di creazione
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("photo", new Photo());
        model.addAttribute("categoryList", categoryRepository.findAll());
        return "edit"; //template unico per create e edit
    }
}
