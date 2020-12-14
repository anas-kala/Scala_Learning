package HausAutomation;

public class Product {
    private String name;
    private int space;
    private int weight;
    private int price;

    public Product(String name, int space, int weight, int price) {
        this.name = name;
        this.space = space;
        this.weight = weight;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getSpace() {
        return space;
    }

    public int getWeight() {
        return weight;
    }

    public int getPrice() {
        return price;
    }
}
