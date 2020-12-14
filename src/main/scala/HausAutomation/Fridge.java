package HausAutomation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Fridge {
    private int weightCapacity = 1000;
    private int spaceCapacity = 200;
    private static int currentWeightOfContents = 0;
    private static int currentSpaceOfContents = 0;
    private static ArrayList<Product> contents = new ArrayList<>();
    private static ArrayList<String> contentsTypes = new ArrayList<>();           // this fridge is designed to contain five different types of products. orange, apple, watermelon, kiwi, meet
    private static HashMap<LocalDate,Product> history=new HashMap<>();

    public Fridge(int weightCapacity, int spaceCapacity) {
        this.weightCapacity = weightCapacity;
        this.spaceCapacity = spaceCapacity;
        contentsTypes.add("apple");
        contentsTypes.add("orange");
        contentsTypes.add("watermelon");
        contentsTypes.add("kiwi");
        contentsTypes.add("meet");
    }

    public int getWeightCapacity() {
        return weightCapacity;
    }

    public int getSpaceCapacity() {
        return spaceCapacity;
    }

    public void consumeFromFridge(Product product) {
        currentSpaceOfContents -= product.getSpace();
        currentWeightOfContents -= product.getWeight();

        for (Product e : contents) {        // when a product runs out from the fridge, it gets automatically ordered.
            if (!contentsTypes.contains(product.getName())) {
                makeOrder(product);
            }
        }
    }

    public void putIntoFridge(Product product) {
        if (currentSpaceOfContents + product.getSpace() <= spaceCapacity && currentWeightOfContents + product.getWeight() <= weightCapacity) {
            contents.add(product);
            currentWeightOfContents += currentWeightOfContents + product.getWeight();
            currentSpaceOfContents += currentSpaceOfContents + product.getSpace();
        }
    }

    public Boolean enoughSpace(int spaceOfOrder) {
        if (spaceOfOrder + currentSpaceOfContents < spaceCapacity)
            return true;
        return false;
    }

    public Boolean enoughtWeight(int weightOfOrder) {
        if (weightOfOrder + currentWeightOfContents < weightCapacity)
            return true;
        return false;
    }

    private void makeOrder(Product product) {
        if (product.getName().equals("meet"))
            tryOrdering(product, 5);
        else if (product.getName().equals("apple"))
            tryOrdering(product, 10);
        else if (product.getName().equals("orange"))
            tryOrdering(product, 10);
        else if (product.getName().equals("watermelon"))
            tryOrdering(product, 1);
        else if (product.getName().equals("kiwi"))
            tryOrdering(product, 10);
    }

    private void tryOrdering(Product product, int i) {
        for (i = 5; i >= 0; i--) {
            if (enoughSpace(i) && enoughtWeight(i * 5)) {
                System.out.println(i * 5 + " kilos of " + product.getName() + " has been ordered");
                currentSpaceOfContents -= i;            // reserving space for the order
                currentWeightOfContents -= i * 5;       // reserving weight for the order
                // for the history
                LocalDate date=LocalDate.now();
                history.put(date,product);
                break;
            }
        }
    }

    private void makeCosumizedOrder(Product product, int weight,int space){
        if (enoughSpace(space) && enoughtWeight(weight)) {
            System.out.println(weight + " kilos of " + product.getName() + " has been ordered");
            currentSpaceOfContents -= space;            // reserving space for the order
            currentWeightOfContents -= weight;       // reserving weight for the order
        }
    }

    private void queryContents(String productName){
        if(productName!= "watermelon" && productName!="orange" && productName!="kiwi" && productName!="apple" && productName!="meet")
            System.out.println("the product you entered does not belong to the contents of the fridge");
        else{
            switch (productName){
                case "watermelon" :
                    displyProductInformation("watermelon");
                    break;
            }
        }
    }

    private void displyProductInformation(String str) {
        int count=0;
        int space=0;
        int weight=0;
        for(Product e: contents){
            if(e.getName().equals(str)){
                count +=1;
                space +=e.getSpace();
                weight=e.getWeight();
            }
        }
        System.out.println("of the product "+str+" you still have "+count+". space used: "+space+", weight used: "+weight);
    }
}
