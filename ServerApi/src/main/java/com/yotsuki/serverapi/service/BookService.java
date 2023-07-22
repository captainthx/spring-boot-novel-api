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
        if (Objects.isNull(request.getName())) {
            log.warn("[book] name is null! {}", request);
            return Response.error(ResponseCode.INVALID_BOOK_NAME);
        }
        if (Objects.isNull(request.getContent())) {
            log.warn("[book] content is null! {} ", request);
            return Response.error(ResponseCode.INVALID_BOOK_CONTENT);
        }
        if (Objects.isNull(request.getPrice())) {
            log.warn("[book] price is null! {}", request);
            return Response.error(ResponseCode.INVALID_BOOK_PRICE);
        }

        if (Objects.isNull(request.getType())) {
            log.warn("[book] type is null! {} ", request);
            return Response.error(ResponseCode.INVALID_BOOK_TYPE);
        }

        if (Objects.isNull(request.getSynopsis())) {
            log.warn("[book] synopsis is null! {} ", request);
            return Response.error(ResponseCode.INVALID_BOOK_SYNOPSIS);
        }

        // save to entity
        Book entity = new Book();
        entity.setName(request.getName());
        entity.setType(request.getType());
        entity.setSynopsis(request.getSynopsis());
        entity.setContent(request.getContent());
        entity.setPrice(request.getPrice());
        // save to db
        Book bookRes = bookRepository.save(entity);

        //  response
        BookResponse response = BookResponse.builder()
                .id(bookRes.getId())
                .name(bookRes.getName())
                .type(bookRes.getType())
                .synopsis(bookRes.getSynopsis())
                .content(bookRes.getContent())
                .price(bookRes.getPrice())
                .build();

        return Response.success(response);
    }


    //find all book
    public ResponseEntity<?> getAll(Pagination pagination) {
        Pageable paginate = Comm.getPaginate(pagination);
        Page<Book> page = bookRepository.findBookBy(paginate);
        return Response.successList(page);
    }

    public ResponseEntity<?> getAllOrderById(Pagination pagination) {
        Pageable paginate = Comm.getPaginate(pagination);
        Page<Book> page = bookRepository.findAllByOrderByIdDesc(paginate);
        return Response.successList(page);
    }

    public ResponseEntity<?>getAllByType(String type,Pagination pagination){
        if (Objects.isNull(type)) {
            log.info("[book] bookType is null! {}", type);
            return Response.error(ResponseCode.INVALID_BOOK_TYPE);
        }
        Pageable paginate = Comm.getPaginate(pagination);
        Page<Book> page = bookRepository.findAllByType(type,paginate);
        return Response.successList(page);
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
                .content(book.getContent())
                .price(book.getPrice())
                .imageName(book.getImageName())
                .build();
    }

    public static Page<BookResponse> bookResponsePage(Page<Book> data, Pageable pageable) {
        return data.stream().map(e -> response(e)).collect(Collectors.collectingAndThen(Collectors.toList(), list -> new PageImpl<>(list, pageable, list.size())));
}


}
