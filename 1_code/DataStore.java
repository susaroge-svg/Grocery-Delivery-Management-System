/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GroceryDeliveryManagementSystem;

import java.util.ArrayList;

public class DataStore {
    public static ArrayList<User> users = new ArrayList<User>();
    public static ArrayList<Product> products = new ArrayList<Product>();
    public static ArrayList<Orders> orders = new ArrayList<Orders>();
    public static ArrayList<Delivery> deliveries = new ArrayList<Delivery>();

    private static int nextOrderId = 1;
    private static int nextDeliveryId = 1;

    static {
        loadSampleData();
    }

    public static void loadSampleData() {
        users.add(new User(1, "Susan Customer", "customer@grocery.com", "password123", "CUSTOMER"));
        users.add(new User(2, "Susan Manager", "manager@grocery.com", "password123", "MANAGER"));
        users.add(new User(3, "Susan Driver", "driver@grocery.com", "password123", "DELIVERY"));

        products.add(new Product(1, "Milk", "Dairy", 3.49, 20));
        products.add(new Product(2, "Bread", "Bakery", 2.99, 15));
        products.add(new Product(3, "Eggs", "Dairy", 4.99, 18));
        products.add(new Product(4, "Apples", "Produce", 1.99, 30));
        products.add(new Product(5, "Rice", "Pantry", 5.49, 12));
        products.add(new Product(6, "Orange Juice", "Beverages", 4.29, 10));
    }

    public static User authenticate(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public static ArrayList<User> getDrivers() {
        ArrayList<User> drivers = new ArrayList<User>();
        for (User user : users) {
            if (user.getRole().equals("DELIVERY")) {
                drivers.add(user);
            }
        }
        return drivers;
    }

    public static int getNextOrderId() {
        return nextOrderId++;
    }

    public static int getNextDeliveryId() {
        return nextDeliveryId++;
    }

    public static ArrayList<Orders> getOrdersForCustomer(User customer) {
        ArrayList<Orders> result = new ArrayList<Orders>();
        for (Orders order : orders) {
            if (order.getCustomer().getId() == customer.getId()) {
                result.add(order);
            }
        }
        return result;
    }

    public static ArrayList<Orders> getUnassignedOrders() {
        ArrayList<Orders> result = new ArrayList<Orders>();
        for (Orders order : orders) {
            boolean assigned = false;
            for (Delivery delivery : deliveries) {
                if (delivery.getOrder().getOrderId() == order.getOrderId()) {
                    assigned = true;
                    break;
                }
            }
            if (!assigned) {
                result.add(order);
            }
        }
        return result;
    }

    public static ArrayList<Delivery> getDeliveriesForDriver(User driver) {
        ArrayList<Delivery> result = new ArrayList<Delivery>();
        for (Delivery delivery : deliveries) {
            if (delivery.getDriver().getId() == driver.getId()) {
                result.add(delivery);
            }
        }
        return result;
    }
}