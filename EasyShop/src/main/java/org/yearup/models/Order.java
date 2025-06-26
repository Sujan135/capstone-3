package org.yearup.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private LocalDate date;
    private String address;
    private String city;
    private String state;
    private String zip;
    private int shipping_amount;
    @JsonProperty("lineItems")
    private List<OrderLineItem> lineItemList = new ArrayList<>();

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        this.lineItemList.add(orderLineItem);
    }

    public Order(int orderId, int userId, LocalDate date, String address, String city, String state, String zip, int shipping_amount) {
        this.orderId = orderId;
        this.userId = userId;
        this.date = date;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.shipping_amount = shipping_amount;
    }

    public Order() {

    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public int getShipping_amount() {
        return shipping_amount;
    }

    public void setShipping_amount(int shipping_amount) {
        this.shipping_amount = shipping_amount;
    }

    public List<OrderLineItem> getLineItemList() {
        return lineItemList;
    }
}

