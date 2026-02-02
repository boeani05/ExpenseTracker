package org.bernhard.entity;

import java.time.LocalDate;
import java.time.Month;

public class Expense {
    private long id;
    private String description;
    private double amount;
    private Month monthOfCreation;

    public Expense(String description, double amount) {
        this.description = description;
        this.amount = amount;
        this.monthOfCreation = LocalDate.now().getMonth();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Month getMonthOfCreation() {
        return monthOfCreation;
    }

    public void setMonthOfCreation(Month monthOfCreation) {
        this.monthOfCreation = monthOfCreation;
    }

    @Override
    public String toString() {
        return "\nExpense No. " + id + ": " + description + " (" + String.format("%.2f", amount) + ") created in " + monthOfCreation;
    }
}
