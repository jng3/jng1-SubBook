package com.example.jng1_subbook;

import java.util.Date;

/**
 * Subscription class
 *
 * @author jng1
 */

public class Subscription {

    String name;
    Date date;
    int cost;
    String comment;

    /**
     * Constructor
     * @param name the subscription name
     * @param date the subscription date
     * @param cost the subscription cost
     */
    Subscription(String name, Date date, int cost) {
        this.name = name;
        this.date = date;
        this.cost = cost;
    }

    /**
     * Constructor
     * @param name the subscription name
     * @param date the subscription date
     * @param cost the subscription cost
     * @param comment any comments about the subscription
     */

    Subscription(String name, Date date, int cost, String comment) {
        this.name = name;
        this.date = date;
        this.cost = cost;
        this.comment = comment;
    }
}
