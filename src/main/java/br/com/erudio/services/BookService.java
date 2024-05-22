package br.com.erudio.services;

import br.com.erudio.controllers.BookController;
import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.CustomMapper;
import br.com.erudio.model.Book;
import br.com.erudio.repository.BookRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<BookVO> findAll() {
        List<Book> books = this.bookRepository.findAll();
        List<BookVO> collection = books.stream().map(bk -> CustomMapper.parseObject(bk, BookVO.class)).collect(Collectors.toList());
        collection.stream().forEach(vo -> vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel()));
        return collection;
    }

    public BookVO findById(Integer id) {
        Book book = this.bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No registers was found with this ID!"));
        BookVO vo = CustomMapper.parseObject(book, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO create(BookVO input) {
        if (input == null) {
            throw new RequiredObjectIsNullException("Book must be informed!");
        }

        Book book = CustomMapper.parseObject(input, Book.class);
        Book entity = this.bookRepository.save(book);
        BookVO vo = CustomMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO input) {
        if (input == null) {
            throw new RequiredObjectIsNullException("Book must be informed!");
        }

        Book entity = this.bookRepository.findById(input.getKey()).orElseThrow(() -> new ResourceNotFoundException("No registers was found with this ID!"));
        entity.setAuthor(input.getAuthor());
        entity.setLaunchDate(input.getLaunchDate());
        entity.setPrice(input.getPrice());
        entity.setTitle(input.getTitle());
        BookVO vo = CustomMapper.parseObject(this.bookRepository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Integer id) {
        Book entity = this.bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No registers was found with this ID!"));
        this.bookRepository.delete(entity);
    }
}
