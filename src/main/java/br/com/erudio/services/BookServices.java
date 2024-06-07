package br.com.erudio.services;

import br.com.erudio.controllers.BookController;
import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.CustomMapper;
import br.com.erudio.model.Book;
import br.com.erudio.repository.BookRepository;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PagedResourcesAssembler<BookVO> assembler;

    public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {
        logger.info("Finding books...");
        Page<Book> booksPage = this.bookRepository.findAll(pageable);
        Page<BookVO> booksVosPage = booksPage.map(book -> CustomMapper.parseObject(book, BookVO.class));
        booksVosPage.map(vo -> vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel()));
        Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().toString())).withSelfRel();
        return assembler.toModel(booksVosPage, link);
    }

    public BookVO findById(Integer id) {
        Book book = this.bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No registers was found with this ID!"));
        BookVO vo = CustomMapper.parseObject(book, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO create(BookVO input) {
        if (input == null) {
            throw new RequiredObjectIsNullException("It is not allowed to persist a null object!");
        }

        Book book = CustomMapper.parseObject(input, Book.class);
        Book entity = this.bookRepository.save(book);
        BookVO vo = CustomMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO input) {
        if (input == null) {
            throw new RequiredObjectIsNullException("It is not allowed to persist a null object!");
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
