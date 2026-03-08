/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GroceryDeliveryManagementSystem;

/**
 *
 * @author NerdyGirl44
 */
public class Delivery {
    private int deliveryId;
    private Orders order;
    private User driver;
    private String status;

    public Delivery(int deliveryId, Orders order, User driver) {
        this.deliveryId = deliveryId;
        this.order = order;
        this.driver = driver;
        this.status = "Assigned";
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public Orders getOrder() {
        return order;
    }

    public User getDriver() {
        return driver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Delivery #" + deliveryId + " | Order #" + order.getOrderId()
                + " | Driver: " + driver.getName() + " | Status: " + status;
    }
}
