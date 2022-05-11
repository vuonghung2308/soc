package com.mh.soc.controller;

import com.mh.soc.interceptor.CustomException;
import com.mh.soc.model.Cart;
import com.mh.soc.model.Order;
import com.mh.soc.model.Shipment;
import com.mh.soc.model.User;
import com.mh.soc.repository.CartRepository;
import com.mh.soc.repository.OrderRepository;
import com.mh.soc.repository.ShipmentRepository;
import com.mh.soc.repository.UserRepository;
import com.mh.soc.utils.MailService;
import com.mh.soc.vo.request.MakeOrderRequest;
import com.mh.soc.vo.request.UpdateOrderRequest;
import com.mh.soc.vo.response.OrderDetailResponse;
import com.mh.soc.vo.response.OrderResponse;
import com.mh.soc.vo.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private ShipmentRepository shipmentRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MailService service;

    @PostMapping("make")
    public ResponseEntity<?> makeOrder(
            HttpServletRequest request,
            @RequestBody MakeOrderRequest body
    ) {
        User user = (User) request.getAttribute("user");
        if (user.getCart() == null) {
            throw new CustomException(
                    "CART_IS_EMPTY",
                    "Your cart is empty"
            );
        } else {
            Cart cart = cartRepo.getById(user.getCart().getId());
            List<Order> orders = getOrders(user.getId());
            List<Shipment> shipments = getShipments(user.getId());
            if (cart.getItem().isEmpty()) {
                throw new CustomException(
                        "CART_IS_EMPTY",
                        "Your cart is empty"
                );
            }
            Shipment shipment = new Shipment(body);
            Order order = new Order(cart, shipment);
            cart.getItem().clear();
            shipments.add(shipment);
            orders.add(order);

            shipmentRepo.save(shipment);
            orderRepo.save(order);
            cartRepo.save(cart);

            user.setShipment(shipments);
            user.setOrder(orders);
            user.setCart(cart);
            userRepo.save(user);
            ResponseMessage message = new ResponseMessage(
                    "CREATED_ORDER_SUCCESSFULLY",
                    "You created order successfully"
            );
            return ResponseEntity.ok(message);
        }

    }

    @GetMapping("")
    public ResponseEntity<?> get(
            @RequestParam(required = false) Long id,
            HttpServletRequest request
    ) {
        User user = (User) request.getAttribute("user");
        List<Order> orders = getOrders(user.getId());
        if (id == null) {
            return ResponseEntity.ok(OrderResponse.get(orders));
        } else {
            if (user.getOrder() == null) {
                throw new CustomException(
                        "ORDER_NOT_FOUND",
                        "Can not find your order with id: " + id
                );
            } else {
                Order order = null;
                for (Order o : orders) {
                    if (Objects.equals(o.getId(), id)) {
                        order = o;
                    }
                }
                if (order == null) {
                    throw new CustomException(
                            "ORDER_NOT_FOUND",
                            "Can not find your order with id: " + id
                    );
                } else {
                    OrderDetailResponse orderResponse = new OrderDetailResponse(order);
                    return ResponseEntity.ok(orderResponse);
                }
            }
        }

    }

    @PostMapping("update")
    public ResponseEntity<?> update(
            @RequestBody UpdateOrderRequest body,
            HttpServletRequest request
    ) {
        User user = (User) request.getAttribute("user");
        if (user.getRole() == User.Role.CUSTOMER && body.getStatus() != Order.Status.CANCELED) {
            throw new CustomException(
                    "ACTION_NOT_ALLOWED",
                    "Your action is not allowed"
            );
        }
        Order order = orderRepo.findById(body.getId())
                .orElseThrow(() -> new CustomException(
                        "ORDER_NOT_FOUND",
                        "Can not find order has id: " + body.getId())
                );
        User client = userRepo.getById(orderRepo.getUserId(order.getId()));
        order.setStatus(body.getStatus());
        service.sendOrderInfo(order, user, client);
        orderRepo.save(order);
        ResponseMessage message = new ResponseMessage(
                "UPDATE_ORDER_SUCCESSFULLY",
                "You update order successfully"
        );
        return ResponseEntity.ok(message);
    }

    private List<Order> getOrders(Long userId) {
        List<Long> ids = orderRepo.getOrderIds(userId);
        List<Order> orders = new ArrayList<>();
        for (Long i : ids) {
            orders.add(orderRepo.getById(i));
        }
        return orders;
    }

    private List<Shipment> getShipments(Long userId) {
        List<Long> ids = shipmentRepo.getShipmentIds(userId);
        List<Shipment> shipments = new ArrayList<>();
        for (Long i : ids) {
            shipments.add(shipmentRepo.getById(i));
        }
        return shipments;
    }
}
