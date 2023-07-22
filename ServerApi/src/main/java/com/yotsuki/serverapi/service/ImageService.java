package com.yotsuki.serverapi.service;

import com.yotsuki.excommon.common.Response;
import com.yotsuki.excommon.common.ResponseCode;
import com.yotsuki.serverapi.entity.Book;
import com.yotsuki.serverapi.model.response.BookResponse;
import com.yotsuki.serverapi.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class ImageService {

    private final BookRepository bookRepository;
    private final String imgPath = "/api/img/" ;

    public ImageService( BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    //upload image
    public ResponseEntity<?> uploadImage(Long bookId, MultipartFile file) throws IOException {
        if (Objects.isNull(bookId)) {
            log.warn("[book] bookId is null::{}", bookId);
            return Response.error(ResponseCode.INVALID_BOOK_ID);
        }
        // validate file
        if (file == null) {
            log.debug("[book] file is null!::{}", file);
            return Response.error(ResponseCode.INVALID_IMAGE);
        }
        if (file.getSize() > 1048576 * 2) {
            log.debug("[book] file size is max size::{}", file.getSize());
            return Response.error(ResponseCode.MAX_IMAGE_SIZE);
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            log.debug("[book] contentType is null!::{}", contentType);
            return Response.error(ResponseCode.INVALID_IMAGE_TYPE);
        }

        List<String> supportType = Arrays.asList("image/png");
        log.info("supportType {}", supportType);
        if (!supportType.contains(contentType)) {
            log.info("[book] contentType is not support!::{}", contentType);
            return Response.error(ResponseCode.INVALID_IMAGE_TYPE);
        }

        if (file.isEmpty()) {
            return Response.error(ResponseCode.INVALID_IMAGE);
        }
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (!bookOptional.isPresent()) {
            log.warn("[book] bookId not found!::{} ", bookOptional.get());
            return Response.error(ResponseCode.NOT_FOUND);
        }

        Book entity = bookOptional.get();

        String fileName = entity.getName();

        this.initDirectory();

        Path path = Paths.get(this.imgPath,fileName.concat(".jpg"));
        // Copy the uploaded file to the target path
        Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

        entity.setImageName(fileName.concat(".jpg"));

        Book book = bookRepository.save(entity);

        // response
        BookResponse response = BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .type(book.getType())
                .synopsis(book.getSynopsis())
                .content(book.getContent())
                .imageName(book.getImageName())
                .build();

        return Response.success(response);
    }
    private void initDirectory() {
        File dir = new File(this.imgPath);
        if (!dir.exists()) {
            dir.mkdirs();
            dir.canWrite();
            dir.canRead();
        }
    }

    public ResponseEntity<?> findImag(String imgName) {
        try {
            String imagePath = imgPath + imgName;
            Resource image = new UrlResource("file:" + imagePath);

            if (image.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG) // or the appropriate content type for your images
                        .body(image);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
