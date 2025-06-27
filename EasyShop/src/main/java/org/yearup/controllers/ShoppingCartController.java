package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/cart")
@CrossOrigin
public class ShoppingCartController
{
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;


    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;

    }

    @GetMapping
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return shoppingCartDao.getByUserId(userId);
        }
        catch(Exception e)
        {
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping("/products/{productId}")
    public ShoppingCart addProductToCart(@PathVariable int productId,
                                 Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            shoppingCartDao.addProduct(user.getId(), productId);

            return shoppingCartDao.getByUserId(user.getId());
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not add products");
        }
    }

    @PutMapping("/products/{productId}")
    public void updateCartProduct(@PathVariable int productId,
                                  @RequestBody ShoppingCartItem item,
                                  Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            shoppingCartDao.updateQuantity(user.getId(), productId, item.getQuantity());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not update products");

        }
    }

    @DeleteMapping
    public ShoppingCart deleteCart(Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            shoppingCartDao.clearCart(user.getId());

            return shoppingCartDao.getByUserId(user.getId());

        }catch (Exception e) {
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not delete products");

        }
    }
}
