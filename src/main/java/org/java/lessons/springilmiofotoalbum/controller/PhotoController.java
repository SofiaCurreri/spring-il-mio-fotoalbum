package org.java.lessons.springilmiofotoalbum.controller;

import jakarta.validation.Valid;
import org.java.lessons.springilmiofotoalbum.dto.PhotoForm;
import org.java.lessons.springilmiofotoalbum.model.Photo;
import org.java.lessons.springilmiofotoalbum.repository.CategoryRepository;
import org.java.lessons.springilmiofotoalbum.repository.PhotoRepository;
import org.java.lessons.springilmiofotoalbum.security.DatabaseUserDetailsService;
import org.java.lessons.springilmiofotoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private DatabaseUserDetailsService databaseUserDetailsService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PhotoService photoService;

    //controller per mostrare lista foto visibili e per filtrare la ricerca
    @GetMapping
    public String list(@RequestParam(name = "keyword", required = false) String searchString, Authentication authentication, Model model) {
        List<Photo> listPhotos;
        List<Photo> listPhotosVisible = new ArrayList<>();
        UserDetails user = databaseUserDetailsService.loadUserByUsername(authentication.getName());

        //per la ricerca da parte dell' utente
        if (searchString == null || searchString.isBlank()) {
            listPhotos = photoRepository.findAll();
        } else {
            listPhotos = photoRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchString, searchString);
        }

        //in caso di lista vuota
        if (listPhotos.isEmpty()) {
            model.addAttribute("message", "No photos available, sorry");
        }

        //creo lista con solo foto visibili
        for (Photo photo : listPhotos) {
            if (photo.getVisible()) {
                listPhotosVisible.add(photo);
            }
        }

        //per distinguere tra lista foto accessibile all' admin e quella solo all' utente
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("USER"))) {
            model.addAttribute("listPhotos", listPhotosVisible);
        } else {
            model.addAttribute("listPhotos", listPhotos);
        }

        model.addAttribute("searchInput", searchString == null ? "" : searchString);
        return "index";
    }

    //controller per mostrare dettaglio foto
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
        model.addAttribute("photo", new PhotoForm());
        model.addAttribute("categoryList", categoryRepository.findAll());
        return "editCreate"; //template unico per create e edit
    }

    @PostMapping("/create")
    //ci aspettiamo di ricevere un obj di tipo Photo, i cui attributi vengono riempiti dai dati inseriti nel form
    public String store(@Valid @ModelAttribute("photo") PhotoForm photoForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            //chiedo a PhotoService di salvare foto su db
            photoService.create(photoForm);
        }

        if (bindingResult.hasErrors()) {
            //ci sono stati errori
            model.addAttribute("categoryList", categoryRepository.findAll());
            return "editCreate"; //ritorno template form ma con foto precaricata
        }

        //save fa create sql se obj con quella PK non esiste, altrimenti fa update
//        photoRepository.save(formPhoto);

        //se tutto va bene rimando alla lista delle foto
        return "redirect:/photos";
    }

    //controller per fare l' edit dell' immagine
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        //verificare se esiste foto con quell' id
        Optional<Photo> result = photoRepository.findById(id);
        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo with id = " + id + " not found :(");
        }
        model.addAttribute("photo", result.get());
        model.addAttribute("categoryList", categoryRepository.findAll());
        return "editCreate";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("photo") Photo formPhoto,
                         BindingResult bindingResult,
                         Model model) {
        Optional<Photo> result = photoRepository.findById(id);
        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo with id = " + id + " not found :(");
        }
        Photo photoToEdit = result.get(); //vecchia versione foto
        //nuova versione foto = formPhoto

        //valido formPhoto
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryList", categoryRepository.findAll());
            return "editCreate";
        }

        formPhoto.setId(photoToEdit.getId());
        photoRepository.save(formPhoto);
        return "redirect:/photos";
    }

    //controller per eliminare foto
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Optional<Photo> result = photoRepository.findById(id);
        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo with id = " + id + " not found:(");
        }
        Photo photoToDelete = result.get();
        photoRepository.delete(photoToDelete);
        redirectAttributes.addFlashAttribute("message", "Photo " + photoToDelete.getTitle() + " deleted!");
        return "redirect:/photos";
    }

}
