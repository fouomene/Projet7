package com.oc.microservice.controller;

import com.oc.microservice.dao.BookDao;
import com.oc.microservice.exceptions.BookNotFoundException;
import com.oc.microservice.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class BookController {
    @Autowired
    private BookDao bookDao;

    @GetMapping(value = "BooksSearch/{keyword}")
    public List<Book> findBookByKeyword(@PathVariable String keyword){
        return  bookDao.findByKeyword(keyword);
    }

    @GetMapping(value = "Books/{id}")
    public Book displayBook(@PathVariable Long id) {
        return bookDao.findById(id).orElseThrow(() -> new BookNotFoundException("L'ouvrage avec l'id "+id+" n'existe pas."));
    }

    @GetMapping(value = "Books")
    public List<Book> ListBook(){
        return bookDao.findAll();
    }
}

