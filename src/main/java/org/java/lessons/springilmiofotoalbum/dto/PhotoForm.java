package org.java.lessons.springilmiofotoalbum.dto;

import jakarta.validation.constraints.NotBlank;
import org.java.lessons.springilmiofotoalbum.model.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class PhotoForm {

    private Integer id;
    @NotBlank(message = "Title must not be blank")
    private String title;
    private String description;

    private Boolean visible;

    private List<Category> categories = new ArrayList<>();

    //MultipartFile = classe per wrappare file cosi come ci arrivano dai form
    private MultipartFile imageFile;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
