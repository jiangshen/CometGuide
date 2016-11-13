package gtappathon.cometguide;

import java.util.HashMap;

/**
 * Created by Henry on 11/12/16.
 */

public class Product {
    private String name;
    private String modelNumber;
    private double price;
    private int stockQuantity;
    private HashMap<Object, Object> extrasHashMap;

    public Product() {

    }

    public Product(String name, String modelNumber, double price, int stockQuantity) {
        this.name = name;
        this.modelNumber = modelNumber;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Product(String name, String modelNumber, int stockQuantity, double price, HashMap<Object, Object> extrasHashMap) {
        this.name = name;
        this.modelNumber = modelNumber;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.extrasHashMap = extrasHashMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public HashMap<Object, Object> getExtrasHashMap() {
        return extrasHashMap;
    }

    public void setExtrasHashMap(HashMap<Object, Object> extrasHashMap) {
        this.extrasHashMap = extrasHashMap;
    }
}
