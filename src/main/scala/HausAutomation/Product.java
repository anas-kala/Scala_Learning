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

    public Product(){

    }

    public Product(Product copy){       // deep copy constructor
        Product newProduct=new Product();
        newProduct.name=copy.getName();
        newProduct.space=copy.getSpace();
        newProduct.weight=copy.getSpace();
        newProduct.price=copy.getPrice();
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

    public void setName(String name) {
        this.name = name;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
