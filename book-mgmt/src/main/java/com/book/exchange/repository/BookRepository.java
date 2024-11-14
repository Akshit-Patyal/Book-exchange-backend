package com.book.exchange.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.book.exchange.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	List<Book> findByUserId(Long userId);

	Page<Book> findByUserId(Long userId, Pageable pageable);

	Page<Book> findByTitle(String title, Pageable pageable);

	Page<Book> findByAuthor(String author, Pageable pageable);

	Page<Book> findByGenre(String genre, Pageable pageable);

	Page<Book> findByBookCondition(String bookCondition, Pageable pageable);

	Page<Book> findByLocation(String location, Pageable pageable);

	Page<Book> findByIsAvailable(Boolean isAvailable, Pageable pageable);

}
