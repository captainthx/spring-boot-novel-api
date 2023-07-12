package com.yotsuki.serverapi.controller;

import com.yotsuki.boot.configJwt.UserDetailsImp;
import com.yotsuki.excommon.model.Pagination;
import com.yotsuki.serverapi.model.request.BookRequest;
import com.yotsuki.serverapi.model.request.FavoriteRequest;
import com.yotsuki.serverapi.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/books")
@Slf4j
public class BookApi {


    private final BookService bookService;

    public BookApi(BookService bookService) {
        this.bookService = bookService;
    }


    @PostMapping
    public ResponseEntity<?> signupBook(@RequestBody BookRequest request) {
        return bookService.createBook(request);
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImg(@RequestParam MultipartFile file) throws IOException {
        return bookService.uploadImage(file);
    }

    @PostMapping("/favorite")
    public ResponseEntity<?> addFavorite(@RequestBody FavoriteRequest request) {
        return bookService.createFavorite(request);
    }

    @GetMapping
    public ResponseEntity<?> findAll( Pagination pagination) {
        return bookService.getAll(pagination);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> findById(@PathVariable("bookId") Long id) {
        return bookService.getAllById(id);
    }

    //TODO:
    @GetMapping("/favorite/{bookId}")
    public ResponseEntity<?> findByUserId(@AuthenticationPrincipal UserDetailsImp userDetailsImp){
        return null;
    }


//    @GetMapping("/img/{fileName}")
//    public ResponseEntity<?> find ag(fileName);
//    }


    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookRequest request) {
        return bookService.updateBookDetail(id, request);
    }
}
