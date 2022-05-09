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
import com.mh.soc.vo.request.AddBookRequest;
import com.mh.soc.vo.response.CartResponse;
import com.mh.soc.vo.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    ItemRepository itemRepository;

    @GetMapping("")
    public ResponseEntity<?> get(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        if (user.getCart() == null) {
            throw new CustomException(
                    "CART_IS_EMPTY",
                    "gio hang rong"
            );
        } else {
            CartResponse cartResponse = new CartResponse(user.getCart());
            return ResponseEntity.ok(cartResponse);
        }

    }

    @PostMapping("add")
    public ResponseEntity<?> add(HttpServletRequest request, @RequestBody AddBookRequest body) {
        Book book = bookRepository.findById(body.getBookId()).orElseThrow(() -> new CustomException(
                "INVALID_EMAIL",
                "your email is incorrect")
        );
        Cart cart = new Cart();
        Item item = new Item();
        item.setQuantity(body.getQuantity());
        item.setBook(book);
        Long userId = (Long) request.getAttribute("user");
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new CustomException(
                "INVALID_EMAIL",
                "your email is incorrect")
        );
        if(user.getCart() == null){
            itemRepository.save(item);
            cartRepository.save(cart);
            user.setCart(cart);
            userRepository.save(user);
            ResponseMessage message = new ResponseMessage(
                    "SUCCESSFUL_REGISTRATION",
                    "successful registration, check your email for verification code"
            );
            return ResponseEntity.ok(message);
        }else{
            List<Item> list = user.getCart().getItems();
            for(Item i : list){
                if(i.getBook().getId() == body.getBookId()){
                    i.setQuantity(i.getQuantity()+ body.getQuantity());
                }
            }
            cartRepository.save(user.getCart());
            ResponseMessage message = new ResponseMessage(
                    "SUCCESSFUL_REGISTRATION",
                    "successful registration, check your email for verification code"
            );
            return ResponseEntity.ok(message);
        }
    }
//    @PostMapping("remove/{bookId}")
//    public String remove(@PathVariable("bookId") long bookId){
//        cartService.remove(bookId);
//        return"redircect:/api/cart/list";
//    }
//    @PostMapping("update")
//    public String update(@RequestParam("bookId") long bookId, @RequestParam("quantity") Integer quantity){
//        cartService.update(bookId,quantity);
//        return "redircect:/api/cart/list";
//        }
//        @GetMapping("clear")
//        public String clear(){
//        return "redircect:/api/cart/list";
//        }

}

