package org.java.lessons.springilmiofotoalbum.controller;

import jakarta.validation.Valid;
import org.java.lessons.springilmiofotoalbum.model.Category;
import org.java.lessons.springilmiofotoalbum.model.Photo;
import org.java.lessons.springilmiofotoalbum.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    //controller per mostrare lista categorie e gestire form edit/create categorie
    @GetMapping
    public String index(Model model, @RequestParam("edit") Optional<Integer> categoryId) {
        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("categoryList", categoryList);

        Category categoryObj;
        if (categoryId.isPresent()) {
            //se mi viene passato param categoryId, cerco la category su db
            Optional<Category> category = categoryRepository.findById(categoryId.get());
            if (category.isPresent()) {
                //se category c' è valorizzo categoryObj con quella
                categoryObj = category.get();
            } else {
                //altrimenti ne creo una nuova
                categoryObj = new Category();
            }
        } else {
            //se non ho param valorizzo categoryObj con nuova category
            categoryObj = new Category();
        }

        model.addAttribute("categoryObj", categoryObj);
        return "indexCategories";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("categoryObj") Category formCategory, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryList", categoryRepository.findAll());
            return "indexCategories";
        }
        categoryRepository.save(formCategory);
        return "redirect:/categories";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        Optional<Category> result = categoryRepository.findById(id);
        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Category categoryToDelete = result.get();
        //per ogni foto associata alla category da eliminare, rimuovo la category da eliminare da quelle associate alla foto
        for (Photo photo : categoryToDelete.getPhoto()) {
            photo.getCategories().remove(categoryToDelete);
        }

        categoryRepository.deleteById(id);
        return "redirect:/categories";
    }
}
