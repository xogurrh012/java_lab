package server;

public class Product {
    private int id;
    private String name;
    private int price;
    private int qty;

    public Product(int id, String name, int price, int qty){
        this.id = id;
        this.name = name;
        this.price = price;
        this.qty = qty;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }
}
