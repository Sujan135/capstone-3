package org.yearup.models;

import java.math.BigDecimal;

public class OrderLineItem {
    private int orderId;
    private int productId;
    private int quantity;
    private BigDecimal salesPrice;
    private int discount;

    public OrderLineItem(int orderId, int productId, int quantity, BigDecimal salesPrice, int discount) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.salesPrice = salesPrice;
        this.discount= discount;
    }

    public OrderLineItem() {

    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal unitPrice) {
        this.salesPrice = unitPrice;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
