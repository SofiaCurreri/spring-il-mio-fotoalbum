package org.java.lessons.springilmiofotoalbum.repository;

import org.java.lessons.springilmiofotoalbum.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    List<Photo> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}
