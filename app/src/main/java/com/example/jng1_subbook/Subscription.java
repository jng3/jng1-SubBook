package com.example.jng1_subbook;

import java.util.Date;

/**
 * Created by JN on 2018-02-03.
 */

public class Subscription {

    String name;
    Date date;
    int cost;
    String comment;

    Subscription(String name, Date date, int cost) {
        this.name = name;
        this.date = date;
        this.cost = cost;
    }

    Subscription(String name, Date date, int cost, String comment) {
        this.name = name;
        this.date = date;
        this.cost = cost;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public int getCost() {
        return cost;
    }

    public String getComment() {
        return comment;
    }
}
