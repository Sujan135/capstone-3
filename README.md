# capstone-3 (EasyShop)

# YearUp E-Commerce Project

This is a full-stack e-commerce application built using **Spring Boot (Java)** and **MySQL** as part of my YearUp program. It allows users to browse products, manage a shopping cart, and place orders. Admins can manage categories and products through a secured API.

---

## 🚀 Features

* 🔐 User Authentication (JWT)
* 👤 Admin controls for managing categories/products
* 📦 Product search with filters (category, price range, color)
* 🛒 Persistent Shopping Cart for logged-in users
* ✅ Checkout system (convert cart to order)
* 📇 User Profile View/Update

---

## 🖼️ Application Screens

> Replace these links with actual screenshots
![addCart Testing Image](https://github.com/Sujan135/capstone-3/blob/d286f1bff1024e55b5a4634f80630dfd4cc471c7/added-to-cart.jpg)
![min&max Testing Image](https://github.com/Sujan135/capstone-3/blob/d286f1bff1024e55b5a4634f80630dfd4cc471c7/min-max-filter.jpg)
![postman-product Testing Image](https://github.com/Sujan135/capstone-3/blob/d286f1bff1024e55b5a4634f80630dfd4cc471c7/postman-products.jpg)
> ![postman-categories Testing Image](https://github.com/Sujan135/capstone-3/blob/d286f1bff1024e55b5a4634f80630dfd4cc471c7/postman-categories.jpg)


---

## 🔧 Technologies Used

* Java + Spring Boot
* MySQL + JDBC
* Postman (for testing)
* Git + GitHub

---

## 💡 Interesting Code Snippet

The `OrdersController` handles the full checkout process by converting the cart into an order and saving each item:

```java
@PostMapping
public void checkout(Principal principal) {
    User user = userDao.getByUserName(principal.getName());
    List<ShoppingCartItem> cartItems = shoppingCartDao.getCartItems(user.getId());

    if (cartItems.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
    }

    Order order = new Order();
    order.setUserId(user.getId());
    order.setOrderDate(LocalDate.now());
    int orderId = orderDao.createOrder(order);

    for (ShoppingCartItem item : cartItems) {
        OrderLineItem line = new OrderLineItem();
        line.setOrderId(orderId);
        line.setProductId(item.getProductId());
        line.setQuantity(item.getQuantity());
        line.setSalesPrice(item.getProduct().getPrice().doubleValue());
        line.setDiscount(0.0);  // No discount applied

        orderDao.addOrderLineItem(line);
    }

    shoppingCartDao.clearCart(user.getId());
}
- This method is a good example of:

- Using Principal to get the authenticated user

- Reading the cart and converting it to an order

- Inserting data using DAO classes
```

This logic ensures that if a product is already in the user's cart, its quantity is increased. Otherwise, it adds a new item.

---

## 🧪How to Test the API

Use Postman to test API endpoints like:

* `GET /products?cat=1&minPrice=100`
* `POST /cart/products/5` (Add to cart)
* `PUT /profile` (Update user profile)
* `POST /orders` (Checkout)

---

## Future Improvements

*  Product reviews
*  Email order confirmations
*  Address and payment info
*  Order history and tracking

---

##  Git Commit Strategy

* Each meaningful feature has its own commit
* Bugs are fixed in separate commits
* Clear and descriptive commit messages

---

>  Created by **Sujan Banjara** for YearUp 2025 Final Project
>  Demo Day: \[06/27/2025]
