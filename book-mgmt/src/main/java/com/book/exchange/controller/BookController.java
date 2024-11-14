package com.book.exchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.book.exchange.model.payload.request.BookRequest;
import com.book.exchange.service.BookService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/book-mgmt", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "BookController", description = "Book Management")
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	// get books by user id 
	@GetMapping("/books/{userId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getAllBooksByUserId(Long userId) {
		return ResponseEntity.ok(bookService.books(userId));
	}
	
	// get all books 
	@GetMapping("/books")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getAllBooks() {
		return ResponseEntity.ok(bookService.getAllBooks());
	}
	
	@GetMapping("/book/{bookId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getBookByBookId(Long bookId) {
		return ResponseEntity.ok(bookService.getBookById(bookId));
	}

	@PostMapping("/book")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> createBook(@RequestBody BookRequest book) {
		return ResponseEntity.ok(bookService.createBook(book));
	}
	
	
	@PutMapping("/book/{bookId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> updateBook(@PathVariable("bookId") Long bookId, @RequestBody BookRequest book) {
		return ResponseEntity.ok(bookService.updateBook(bookId, book));
	}
	
	@DeleteMapping("/book/{bookId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> deleteBook(@PathVariable("bookId") Long bookId) {
		return ResponseEntity.ok(bookService.deleteBook(bookId));
	}
	
	@PostMapping("/search-books")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> searchBooksByCriteria(@RequestBody BookRequest bookRequest, 
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "5") int size) {
		
		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(bookService.searchBooksByCriteria(bookRequest, pageable));
	}
}
