package edu.ewubd.cse489_2021_2_60_041;

public class Item {
    String id;
    String itemName;
    double cost;

    long date;

    public Item(String id, String itemName, double cost, long date){
        this.id =id;
        this.itemName =itemName;
        this.cost =cost;
        this.date =date;
    }
}
