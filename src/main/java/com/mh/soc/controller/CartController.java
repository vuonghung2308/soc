package com.mh.soc.controller;

import com.mh.soc.interceptor.CustomException;
import com.mh.soc.model.Book;
import com.mh.soc.model.Cart;
import com.mh.soc.model.Item;
import com.mh.soc.model.User;
import com.mh.soc.repository.BookRepository;
import com.mh.soc.repository.CartRepository;
import com.mh.soc.repository.ItemRepository;
import com.mh.soc.repository.UserRepository;
import com.mh.soc.vo.request.AddToCartRequest;
import com.mh.soc.vo.request.RemoveRequest;
import com.mh.soc.vo.response.CartResponse;
import com.mh.soc.vo.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BookRepository bookRepo;
    @Autowired
    private ItemRepository itemRepo;

    @GetMapping("")
    public ResponseEntity<?> get(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        if (user.getCart() == null) {
            throw new CustomException(
                    "CART_IS_EMPTY",
                    "Your cart is empty"
            );
        } else {
            Optional<Cart> cart = cartRepo.findById(user.getCart().getId());
            CartResponse cartResponse = new CartResponse(cart.get());
            return ResponseEntity.ok(cartResponse);
        }
    }

    @PostMapping("add")
    public ResponseEntity<?> add(
            HttpServletRequest request,
            @RequestBody AddToCartRequest body
    ) {
        Book book = bookRepo.findById(body.getBookId())
                .orElseThrow(() -> new CustomException(
                        "BOOK_NOT_EXIST",
                        "Book has id: " + body.getBookId() + " is not exist")
                );
        User user = (User) request.getAttribute("user");

        if (user.getCart() == null) {
            Item item = new Item(book, body.getQuantity());
            Cart cart = new Cart(item);
            itemRepo.save(item);
            cartRepo.save(cart);
            user.setCart(cart);
            userRepo.save(user);
            ResponseMessage message = new ResponseMessage(
                    "ADD_TO_CART_SUCCESSFULLY",
                    "Add book to cart successfully"
            );
            return ResponseEntity.ok(message);
        } else {
            Optional<Cart> cart = cartRepo.findById(user.getCart().getId());
            boolean hasItem = false;
            for (Item item : cart.get().getItem()) {
                if (item.getBook().getId() == body.getBookId()) {
                    hasItem = true;
                    item.setQuantity(item.getQuantity() + body.getQuantity());
                    itemRepo.save(item);
                }
            }
            if (!hasItem) {
                Item item = new Item(book, body.getQuantity());
                itemRepo.save(item);
                cart.get().getItem().add(item);
            }
            cartRepo.save(cart.get());
            ResponseMessage message = new ResponseMessage(
                    "ADD_TO_CART_SUCCESSFULLY",
                    "Add book to cart successfully"
            );
            return ResponseEntity.ok(message);
        }
    }

    @PostMapping("update")
    public ResponseEntity<?> update(
            HttpServletRequest request,
            @RequestBody AddToCartRequest body
    ) {
        Book book = bookRepo.findById(body.getBookId())
                .orElseThrow(() -> new CustomException(
                        "BOOK_NOT_EXIST",
                        "Book has id: " + body.getBookId() + " is not exist")
                );
        User user = (User) request.getAttribute("user");

        if (user.getCart() == null) {
            throw new CustomException(
                    "CART_IS_EMPTY",
                    "Your cart is empty"
            );
        } else {
            Optional<Cart> cart = cartRepo.findById(user.getCart().getId());
            boolean hasItem = false;
            for (Item item : cart.get().getItem()) {
                if (item.getBook().getId() == body.getBookId()) {
                    hasItem = true;
                    item.setQuantity(body.getQuantity());
                    itemRepo.save(item);
                    cartRepo.save(cart.get());
                    break;
                }
            }
            if (!hasItem) {
                throw new CustomException(
                        "ITEM_NOT_FOUND",
                        "Can not find item has bookId" + body.getBookId()
                );
            }
            ResponseMessage message = new ResponseMessage(
                    "ADD_TO_CART_SUCCESSFULLY",
                    "Add book to cart successfully"
            );
            return ResponseEntity.ok(message);
        }
    }

    @PostMapping("clear")
    public ResponseEntity<?> clear(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        if (user.getCart() == null) {
            ResponseMessage message = new ResponseMessage(
                    "CLEAR_CART_SUCCESSFULLY",
                    "Clear your cart successfully"
            );
            return ResponseEntity.ok(message);
        }
        Optional<Cart> cart = cartRepo.findById(user.getCart().getId());
        if (cart.isPresent()) {
            cart.get().getItem().clear();
            cartRepo.save(cart.get());
        }
        ResponseMessage message = new ResponseMessage(
                "CLEAR_CART_SUCCESSFULLY",
                "Clear your cart successfully"
        );
        return ResponseEntity.ok(message);
    }

    @PostMapping("remove")
    public ResponseEntity<?> remove(
            HttpServletRequest request,
            @RequestBody RemoveRequest body
    ) {
        User user = (User) request.getAttribute("user");
        if (user.getCart() == null) {
            throw new CustomException(
                    "CART_IS_EMPTY",
                    "Your cart is empty"
            );
        }
        Optional<Cart> cart = cartRepo.findById(user.getCart().getId());
        List<Item> items = cart.get().getItem();
        items.removeIf(item -> item.getBook().getId() == body.getBookId());
        cartRepo.save(cart.get());
        ResponseMessage message = new ResponseMessage(
                "REMOVE_ITEM_SUCCESSFULLY",
                "Remove item in cart successfully"
        );
        return ResponseEntity.ok(message);
    }
}

