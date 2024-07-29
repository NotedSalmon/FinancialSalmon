package fish.notedsalmon.dao;

import jakarta.persistence.criteria.Order;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

    private int id;
    private String date;
    private String name;
    private String description;
    private String image;
    private double price;
    private String category;
    private InventoryStatus inventoryStatus;
    private int rating;
    private List<Order> orders;

    public Product() {
    }

    public Product(int id, String date, String name, String description, String image, double price, String category,
                   InventoryStatus inventoryStatus, int rating) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.category = category;
        this.inventoryStatus = inventoryStatus;
        this.rating = rating;
    }

    @Override
    public Product clone() {
        return new Product(getId(), getDate(), getName(), getDescription(), getImage(), getPrice(), getCategory(),
                getInventoryStatus(), getRating());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public InventoryStatus getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(InventoryStatus inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Product other = (Product) obj;
        if (date == null) {
            return other.date == null;
        }
        else {
            return date.equals(other.date);
        }
    }

}