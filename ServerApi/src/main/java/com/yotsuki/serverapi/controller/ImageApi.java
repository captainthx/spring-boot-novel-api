package com.yotsuki.serverapi.controller;

import com.yotsuki.serverapi.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/img")
public class ImageApi {
    private final BookService bookService;


    public ImageApi(BookService bookService) {
        this.bookService = bookService;
    }



    @GetMapping("/{fileName}")
    public ResponseEntity<?> findImage(@PathVariable String fileName) {
        return bookService.findImag(fileName);
    }
}
