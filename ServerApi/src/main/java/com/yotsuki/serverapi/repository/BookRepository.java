package com.yotsuki.serverapi.repository;

import com.yotsuki.serverapi.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long> {

    Page<Book> findBookBy(Pageable pageable);
}
