package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);

    void addProduct(int userId, int productId);
    void updateQuantity(int userId, int productId, int quantity);
    void deleteCart(int userId);
    // add additional method signatures here
}
