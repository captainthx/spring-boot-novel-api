package com.yotsuki.serverapi.service;

import com.yotsuki.excommon.common.Response;
import com.yotsuki.excommon.common.ResponseCode;
import com.yotsuki.excommon.model.Pagination;
import com.yotsuki.excommon.utils.Comm;
import com.yotsuki.serverapi.entity.Book;
import com.yotsuki.serverapi.entity.Favorite;
import com.yotsuki.serverapi.entity.User;
import com.yotsuki.serverapi.model.request.BookRequest;
import com.yotsuki.serverapi.model.request.FavoriteRequest;
import com.yotsuki.serverapi.model.response.BookResponse;
import com.yotsuki.serverapi.repository.BookRepository;
import com.yotsuki.serverapi.repository.FavoriteRepository;
import com.yotsuki.serverapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    public BookService(BookRepository bookRepository, UserRepository userRepository, FavoriteRepository favoriteRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
    }


    // create book
    public ResponseEntity<?> createBook(BookRequest request) {


        if (Objects.isNull(request.getType())) {
            log.debug("[book] type is null!");
            return Response.error(ResponseCode.INVALID_REQUEST);
        }

        if (Objects.isNull(request.getSynopsis())) {
            log.debug("[book] synopsis is null!");
            return Response.error(ResponseCode.INVALID_REQUEST);
        }

        // save to entity
        Book entity = new Book();
        entity.setType(request.getType());
        entity.setSynopsis(request.getSynopsis());

        // save to db
        Book bookRes = bookRepository.save(entity);

        //  response
        BookResponse response = BookResponse.builder()
                .name(bookRes.getName())
                .type(bookRes.getType())
                .synopsis(bookRes.getSynopsis())
                .build();

        return Response.success(response);
    }


    //upload image
    public ResponseEntity<?> uploadImage(MultipartFile file) throws IOException {
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

        try {
            if (file.isEmpty()) {
                return Response.error(ResponseCode.INVALID_IMAGE);
            }
            String fileName = file.getOriginalFilename();

            String uploadDir = "C:/img/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String filePath = uploadDir + fileName;
            // Transfer the file to the specified path
            file.transferTo(new File(filePath));

            Book entity = new Book();
            entity.setName(fileName.replace(".jpg", ""));
            entity.setImageName(fileName);

            Book book = bookRepository.save(entity);

            // response
            BookResponse response = BookResponse.builder()
                    .id(book.getId())
                    .name(book.getName())
                    .imageName(book.getImageName())
                    .build();

            return Response.success(response);

        } catch (IOException e) {
            return Response.error(ResponseCode.INVALID_IMAGE);
        }
    }

    //find bookImg
    public ResponseEntity<?> findImag(String imgName) {
        try {
            String imagePath = "C:/img/" + imgName;
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

    //find all book
    public ResponseEntity<?> getAll(Pagination pagination) {

        Pageable paginate = Comm.getPaginate(pagination);
        Page<Book> page = bookRepository.findBookBy(paginate);

        return Response.successList(bookResponsePage(page,paginate));
    }

    public ResponseEntity<?> updateBookDetail(Long id, BookRequest request) {

        if (Objects.isNull(id)) {
            log.info("[book]  bookId is null! {}", id);
            return Response.error(ResponseCode.INVALID_BOOK_ID);
        }
        if (Objects.isNull(request.getType())) {
            log.info("[book] bookType is null! {}", request.getType());
            return Response.error(ResponseCode.INVALID_BOOK_TYPE);
        }
        if (Objects.isNull(request.getSynopsis())) {
            log.info("[book] bookSynopsis is null {}", request.getSynopsis());
        }

        Optional<Book> opt = bookRepository.findById(id);
        if (!opt.isPresent()) {
            log.info("[book] not found! {}", opt);
            return Response.error(ResponseCode.NOT_FOUND);
        }
        Book book = opt.get();
        book.setType(request.getType());
        book.setSynopsis(request.getSynopsis());
        // save to db
        Book res = bookRepository.save(book);

        return Response.success(response(res));
    }

    public ResponseEntity<?> createFavorite(FavoriteRequest request) {
        if (Objects.isNull(request.getBookId())) {
            log.info("[book]  bookId is null! {}", request);
            return Response.error(ResponseCode.INVALID_BOOK_ID);
        }
        if (Objects.isNull(request.getUserId())) {
            log.info("[book] userId is null! {}", request);
            return Response.error(ResponseCode.INVALID_UID);
        }

        Optional<User> opt = userRepository.findById(request.getUserId());
        if (!opt.isPresent()) {
            log.info("[book] notFound data User {}", opt);
            return Response.error(ResponseCode.NOT_FOUND);
        }
        User user = opt.get();
        Favorite entity = new Favorite();
        entity.setBookId(request.getBookId());
        entity.setUser(user);

        return Response.success(favoriteRepository.save(entity));
    }


    public ResponseEntity<?> getAllById(Long id) {
        if (Objects.isNull(id)) {
            log.info("[book] book id is null!::{}", id);
            return Response.error(ResponseCode.INVALID_BOOK_ID);
        }
        Optional<Book> opt = bookRepository.findById(id);
        if (!opt.isPresent()) {
            log.info("[book] data notFound!::{}", opt);
            return Response.error(ResponseCode.NOT_FOUND);
        }
        Book book = opt.get();

        return Response.success(response(book));

    }




    public static BookResponse response(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .type(book.getType())
                .synopsis(book.getSynopsis())
                .imageName(book.getImageName())
                .build();
    }

    public static Page<BookResponse> bookResponsePage(Page<Book> data, Pageable pageable){
        return data.stream().map(e -> response(e)).collect(Collectors.collectingAndThen(Collectors.toList(),list -> new PageImpl<>(list,pageable,list.size())));
    }


}
