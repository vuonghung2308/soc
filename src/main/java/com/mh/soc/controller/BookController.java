package com.mh.soc.controller;

import com.mh.soc.interceptor.CustomException;
import com.mh.soc.model.*;
import com.mh.soc.repository.BookRepository;
import com.mh.soc.repository.CategoryRepository;
import com.mh.soc.repository.FileRepository;
import com.mh.soc.repository.RatingRepository;
import com.mh.soc.vo.request.CommentRequest;
import com.mh.soc.vo.response.BaseBookResponse;
import com.mh.soc.vo.response.BookDetailResponse;
import com.mh.soc.vo.response.BookResponse;
import com.mh.soc.vo.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RestController
@RequestMapping("/api/book")
public class BookController {
    @Autowired
    private BookRepository bookRepo;
    @Autowired
    private FileRepository fileRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private RatingRepository ratingRepo;

    private static final SimpleDateFormat format =
            new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("base")
    public ResponseEntity<?> get() {
        List<Book> list = bookRepo.findAll();

        BaseBookResponse base = new BaseBookResponse();

        list.sort((b1, b2) -> b2.getSold().compareTo(b1.getSold()));
        base.setTopSale(BookResponse.get(list.subList(
                0, Math.min(10, list.size()))));

        list.sort((b1, b2) -> b2.getStar().compareTo(b1.getStar()));
        base.setTopRating(BookResponse.get(list.subList(
                0, Math.min(10, list.size()))));

        list.sort((b1, b2) -> b2.getDate().compareTo(b1.getDate()));
        base.setTopNew(BookResponse.get(list.subList(
                0, Math.min(10, list.size()))));

        list = bookRepo.findByCategory_Id(3L);
        list.sort((b1, b2) -> b2.getStar().compareTo(b1.getStar()));
        base.setTopComic(BookResponse.get(list.subList(
                0, Math.min(10, list.size()))));

        return ResponseEntity.ok(base);
    }

    @GetMapping("top-sale")
    public ResponseEntity<?> topSale(
            @RequestParam(defaultValue = "100", required = false) Integer limit
    ) {
        List<Book> list = bookRepo.findAll();
        list.sort((b1, b2) -> b2.getSold().compareTo(b1.getSold()));
        return ResponseEntity.ok(BookResponse.get(list.subList(
                0, Math.min(limit, list.size()))));
    }

    @GetMapping("top-new")
    public ResponseEntity<?> topNew(
            @RequestParam(defaultValue = "100", required = false) Integer limit
    ) {
        List<Book> list = bookRepo.findAll();
        list.sort((b1, b2) -> b2.getDate().compareTo(b1.getDate()));
        return ResponseEntity.ok(BookResponse.get(list.subList(
                0, Math.min(limit, list.size()))));
    }

    @GetMapping("")
    public ResponseEntity<?> get(@RequestParam Long id) {
        Optional<Book> optionalBook = bookRepo.findById(id);
        Book book = optionalBook.orElseThrow(() -> new CustomException(
                        "BOOK_NOT_FOUND",
                        "can't find book has id: " + id
                )
        );
        return ResponseEntity.ok(new BookDetailResponse(book));
    }

    @GetMapping("by-category")
    public ResponseEntity<?> getByCategory(@RequestParam Long id) {
        List<Book> list = bookRepo.findByCategory_Id(id);
        return ResponseEntity.ok(BookResponse.get(list));
    }

    @PostMapping("create")
    public ResponseEntity<?> create(
            @RequestParam MultipartFile file, @RequestParam String name,
            @RequestParam String description, @RequestParam Integer numberOfPage,
            @RequestParam String publisher, @RequestParam Long categoryId,
            @RequestParam Long total, @RequestParam Integer sold,
            @RequestParam Long price, @RequestParam String date,
            @RequestParam String author, HttpServletRequest request
    ) {
        User user = (User) request.getAttribute("user");
        if (user.getRole() == User.Role.CUSTOMER) {
            throw new CustomException(
                    "ACTION_NOT_ALLOWED",
                    "Your action is not allowed"
            );
        }

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new CustomException(
                        "CATEGORY_NOT_FOUND",
                        "Can not find category has id: " + categoryId)
                );
        try {
            String originName = StringUtils.cleanPath(
                    Objects.requireNonNull(file.getOriginalFilename()));
            SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSSSSS");
            String ext = originName.substring(originName.lastIndexOf("."));
            String newName = "file_" + f.format(new Date()) + ext;
            File image = new File(newName, file.getContentType(), file.getBytes());
            Book book = new Book(name, description, numberOfPage, publisher, category,
                    total, sold, price, format.parse(date), newName, author);
            fileRepo.save(image);
            bookRepo.save(book);

            ResponseMessage message = new ResponseMessage(
                    "CREATE_SUCCESSFULLY",
                    "Create book: " + name + " successfully"
            );
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(
                    "CANT_NOT_CREATE",
                    "Can not create category name: " + name
            );
        }
    }

    @PostMapping("update")
    public ResponseEntity<?> update(
            @RequestParam Long id, HttpServletRequest request,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer numberOfPage,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long total,
            @RequestParam(required = false) Integer sold,
            @RequestParam(required = false) Long price,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String author
    ) {
        User user = (User) request.getAttribute("user");
        if (user.getRole() == User.Role.CUSTOMER) {
            throw new CustomException(
                    "ACTION_NOT_ALLOWED",
                    "Your action is not allowed"
            );
        }
        Book book = bookRepo.findById(id)
                .orElseThrow(() -> new CustomException(
                        "BOOK_NOT_FOUND",
                        "Can not find book has id: " + id)
                );
        try {
            if (file != null) {
                String originName = StringUtils.cleanPath(
                        Objects.requireNonNull(file.getOriginalFilename()));
                SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSSSSS");
                String ext = originName.substring(originName.lastIndexOf("."));
                String newName = "file_" + f.format(new Date()) + ext;
                File image = new File(newName, file.getContentType(), file.getBytes());
                File oldFile = fileRepo.getByName(book.getImage());
                book.setImage(newName);
                fileRepo.delete(oldFile);
                fileRepo.save(image);
            }
            if (categoryId != null) {
                Category category = categoryRepo.findById(categoryId)
                        .orElseThrow(() -> new CustomException(
                                "CATEGORY_NOT_FOUND",
                                "Can not find category has id: " + id)
                        );
                book.setCategory(category);
            }
            if (name != null) {
                book.setName(name);
            }
            if (description != null) {
                book.setDescription(description);
            }
            if (numberOfPage != null) {
                book.setNumberOfPage(numberOfPage);
            }
            if (publisher != null) {
                book.setPublisher(publisher);
            }
            if (total != null) {
                book.setTotal(total);
            }
            if (sold != null) {
                book.setSold(sold);
            }
            if (price != null) {
                book.setPrice(price);
            }
            if (date != null) {
                book.setDate(format.parse(date));
            }
            if (author != null) {
                book.setAuthor(author);
            }

            bookRepo.save(book);

            ResponseMessage message = new ResponseMessage(
                    "UPDATE_SUCCESSFULLY",
                    "Update book has id: " + id + " successfully"
            );
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(
                    "CANT_NOT_UPDATE",
                    "Can not update book has id: " + id
            );
        }
    }

    @PostMapping("delete")
    public ResponseEntity<?> delete(
            @RequestParam Long id,
            HttpServletRequest request
    ) {
        User user = (User) request.getAttribute("user");
        if (user.getRole() == User.Role.CUSTOMER) {
            throw new CustomException(
                    "ACTION_NOT_ALLOWED",
                    "Your action is not allowed"
            );
        }
        Book book = bookRepo.findById(id)
                .orElseThrow(() -> new CustomException(
                        "BOOK_NOT_FOUND",
                        "Can not find book has id: " + id)
                );
        File file = fileRepo.getByName(book.getImage());
        bookRepo.delete(book);
        fileRepo.delete(file);
        ResponseMessage message = new ResponseMessage(
                "DELETE_SUCCESSFULLY",
                "Delete book has id: " + id + " successfully"
        );
        return ResponseEntity.ok(message);
    }

    @PostMapping("comment")
    public ResponseEntity<?> comment(
            @RequestBody CommentRequest body,
            HttpServletRequest request
    ) {
        User user = (User) request.getAttribute("user");
        Book book = bookRepo.findById(body.getBookId()).orElseThrow(() ->
                new CustomException(
                        "BOOK_NOT_FOUND",
                        "Can not find book has id:" + body.getBookId()
                )
        );
        Long ratingId = ratingRepo.getRatingId(user.getId(), book.getId());
        Rating rating = new Rating();
        if (ratingId != null) {
            rating = ratingRepo.findById(ratingId)
                    .orElse(new Rating());
        }
        rating.setComment(body.getComment());
        rating.setStar(body.getStar());
        rating.setUser(user);
        if (rating.getId() == null) {
            book.getRating().add(rating);
        }
        ratingRepo.save(rating);
        bookRepo.save(book);
        ResponseMessage message = new ResponseMessage(
                "POST_SUCCESSFULLY",
                "Post comment for book has id: " +
                        body.getBookId() + " successfully"
        );
        return ResponseEntity.ok(message);
    }
}