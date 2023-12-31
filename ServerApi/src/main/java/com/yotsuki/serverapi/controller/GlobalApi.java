package com.yotsuki.serverapi.controller;


import com.yotsuki.excommon.model.Pagination;
import com.yotsuki.serverapi.service.BookService;
import com.yotsuki.serverapi.service.ImageService;
import com.yotsuki.serverapi.service.BackPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/global")
@Slf4j
public class GlobalApi {

    private final BookService bookService;
    private final ImageService imageService;
    private final BackPaymentService backPaymentService;

    public GlobalApi(BookService bookService, ImageService imageService, BackPaymentService backPaymentService) {
        this.bookService = bookService;
        this.imageService = imageService;
        this.backPaymentService = backPaymentService;
    }


    @GetMapping("/books")
    public ResponseEntity<?> findAll(Pagination pagination) {
        return bookService.getAll(pagination);
    }

    @GetMapping("/books/new")
    public ResponseEntity<?> findAllByIdDesc(Pagination pagination) {
        return bookService.getAllOrderById(pagination);
    }

    @GetMapping("/books/type/{type}")
    public ResponseEntity<?> findByType(@PathVariable String type, Pagination pagination) {
        return bookService.getAllByType(type, pagination);
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<?> findById(@PathVariable("bookId") Long id) {
        return bookService.getAllById(id);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> findImage(@PathVariable String fileName) {
        return imageService.findImag(fileName);
    }

    @PostMapping("/uploadImage/{bookId}")
    public ResponseEntity<?> uploadImg(@PathVariable Long bookId, @RequestParam MultipartFile file) throws IOException {
        return imageService.uploadImage(bookId, file);
    }







}
