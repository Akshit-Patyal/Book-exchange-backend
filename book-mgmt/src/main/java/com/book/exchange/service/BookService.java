package com.book.exchange.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.book.exchange.entity.Book;
import com.book.exchange.entity.User;
import com.book.exchange.model.payload.request.BookRequest;
import com.book.exchange.repository.BookRepository;
import com.book.exchange.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BookService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	public Page<Book> searchBooksByCriteria(BookRequest bookRequest, Pageable pageable) {

		if (StringUtils.hasText(bookRequest.getTitle())) {
			return bookRepository.findByTitleContainingIgnoreCase(bookRequest.getTitle(), pageable);
		}

		else if (StringUtils.hasText(bookRequest.getAuthor())) {
			return bookRepository.findByAuthorContainingIgnoreCase(bookRequest.getAuthor(), pageable);
		}

		else if (StringUtils.hasText(bookRequest.getGenre())) {
			return bookRepository.findByGenreContainingIgnoreCase(bookRequest.getGenre(), pageable);
		}
		return null;
	}

	public Book createBook(BookRequest bookRequest) {
		Optional<User> userData = userRepository.findById(bookRequest.getUserId());
		if (userData.isPresent()) {
			User user = userData.get();

			// create a book
			return bookRepository
					.save(Book.builder().user(user).title(bookRequest.getTitle()).author(bookRequest.getAuthor())
							.bookCondition(bookRequest.getCondition()).genre(bookRequest.getGenre())
							.location(bookRequest.getLocation()).isAvailable(bookRequest.isAvailable()).build());
		} else {
			throw new EntityNotFoundException("User is not available.");
		}

	}

	public Book deleteBook(Long bookId) {
		Optional<Book> bookData = bookRepository.findById(bookId);
		if (bookData.isPresent()) {
			Book book = bookData.get();
			bookRepository.delete(book);
			return book;
		} else {
			throw new EntityNotFoundException("Book is not available.");
		}
	}

	public List<Book> books(Long userId) {
		return bookRepository.findByUserId(userId);
	}
	
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public Book getBookById(Long bookId) {
		Optional<Book> bookData = bookRepository.findById(bookId);
		if (bookData.isPresent()) {
			return bookData.get();
		} else {
			throw new EntityNotFoundException("Book is not available.");
		}
	}

	public Book updateBook(Long bookId, BookRequest bookRequest) {
		Optional<Book> bookData = bookRepository.findById(bookId);
		if (bookData.isPresent()) {
			Book book = bookData.get();
			updateBookData(bookRequest, book);
			return bookRepository.saveAndFlush(book);
		} else {
			throw new EntityNotFoundException("Book is not available.");
		}
	}

	private void updateBookData(BookRequest bookRequest, Book book) {
		if (StringUtils.hasText(bookRequest.getTitle())) {
			book.setTitle(bookRequest.getTitle());
		}
		if (StringUtils.hasText(bookRequest.getAuthor())) {
			book.setAuthor(bookRequest.getAuthor());
		}
		if (StringUtils.hasText(bookRequest.getLocation())) {
			book.setLocation(bookRequest.getLocation());
		}
		if (StringUtils.hasText(bookRequest.getGenre())) {
			book.setGenre(bookRequest.getGenre());
		}
		if (bookRequest.isAvailable() == true) {
			book.setAvailable(true);
		}
		
		if (bookRequest.isAvailable() == false) {
			book.setAvailable(false);
		}

		if (StringUtils.hasText(bookRequest.getCondition())) {
			book.setBookCondition(bookRequest.getCondition());
		}

	}
}
