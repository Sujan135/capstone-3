package org.yearup.controllers;


import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.OrderDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private OrderDao orderDao;
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProfileDao profileDao;

    @Autowired
    public OrdersController(OrderDao orderDao, ShoppingCartDao shoppingCartDao, UserDao userDao, ProfileDao profileDao) {
        this.orderDao = orderDao;
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.profileDao= profileDao;
    }

    @PostMapping
    public ResponseEntity<Order> checkout(Principal principal) {
        try {
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            List<ShoppingCartItem> cartItems = shoppingCartDao.getCartItems(user.getId());

            if (cartItems.isEmpty()) {
                return new ResponseEntity("{\"error\": \"Not found\"}", HttpStatus.NOT_FOUND);
            }

            Profile profile = profileDao.getByUserId(user.getId());

            Order order = new Order();
            order.setUserId(user.getId());
            order.setDate(LocalDate.now());
            order.setAddress(profile.getAddress());
            order.setCity(profile.getCity());
            order.setState(profile.getState());
            order.setZip(profile.getZip());

            int orderId = orderDao.createOrder(order);

            for (ShoppingCartItem item : cartItems) {
                OrderLineItem orderLineItem = new OrderLineItem();
                orderLineItem.setOrderId(orderId);
                orderLineItem.setProductId(item.getProductId());
                orderLineItem.setQuantity(item.getQuantity());
                orderLineItem.setSalesPrice(item.getProduct().getPrice());
                orderLineItem.setDiscount(0);

                orderDao.addOrderLineItem(orderLineItem);
                order.addOrderLineItem(orderLineItem);
            }

            shoppingCartDao.clearCart(user.getId());

            return new ResponseEntity<Order>(order, HttpStatus.OK);

        }catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error");
        }
    }

}
