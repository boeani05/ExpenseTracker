package org.bernhard.logic;

/*
TODO: add expense (desc, amount)
TODO: update expense
TODO: delete expense
TODO: view expenses
TODO: view summary of expenses
TODO: view summary of expenses of certain month
-------------------------------
LONG STRETCH:
TODO: expense category and filter by it
TODO: make budget
TODO: export expenses to csv
 */

import org.bernhard.entity.Expense;
import org.bernhard.storage.JSONStorage;

import java.io.IOException;
import java.time.Month;
import java.util.List;
import java.util.Locale;

public class Manager {
    private List<Expense> expenses;
    private JSONStorage storage;

    public Manager() {
        try {
            this.storage = new JSONStorage();
        } catch (IOException e) {
            System.err.println("\nError while reading content from JSON: " + e.getMessage() + e);
            return;
        }
        expenses = storage.loadData();
    }

    public void addExpense(String description, double amount) throws IllegalArgumentException, NullPointerException {
        if (isDescriptionEmptyOrNull(description)) {
            throw new NullPointerException("\nDescription cannot be empty!");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("\nAmount has to be a valid value!");
        }

        if (doesDescriptionAlreadyExist(description)) {
            System.out.println("\nThis expense already exists... Try updating it.");
            return;
        }

        Expense newExpense = new Expense(description, amount);

        storage.add(newExpense);

        expenses = storage.loadData();

        System.out.println("\nSuccessfully created new expense '" + description + "'.");
    }

    public void updateExpense(String descriptionOfExpenseToUpdate, String newDescription) throws NullPointerException {
        updateExpense(descriptionOfExpenseToUpdate, newDescription, null);
    }

    public void updateExpense(String descriptionOfExpenseToUpdate, double updatedAmount) throws NullPointerException {
        updateExpense(descriptionOfExpenseToUpdate, null, updatedAmount);
    }

    private void updateExpense(String descriptionOfExpenseToUpdate, String newDescription, Double newAmount) throws NullPointerException, IllegalArgumentException {
        if (isDescriptionEmptyOrNull(descriptionOfExpenseToUpdate)) {
            throw new NullPointerException("\nDescription cannot be empty!");
        }

        if (newDescription == null && newAmount == null) {
            System.out.println("\nNothing to update.");
            return;
        }

        if (newDescription != null && isDescriptionEmptyOrNull(newDescription)) {
            throw new NullPointerException("\nNew description cannot be empty!");
        }

        if (newAmount != null && newAmount <= 0) {
            throw new IllegalArgumentException("\nAmount has to be a valid value!");
        }

        Expense expenseToUpdate = findExpenseToEdit(descriptionOfExpenseToUpdate);

        if (doesExpenseNotExist(expenseToUpdate)) {
            return;
        }

        if (newDescription != null) {
            String lowercasedNewDescription = normalize(newDescription);
            String currentNorm = expenseToUpdate.getDescription() == null ? "" : normalize(expenseToUpdate.getDescription());
            if (!lowercasedNewDescription.equals(currentNorm) && doesDescriptionAlreadyExist(newDescription)) {
                System.out.println("\nAnother expense with the given description already exists.");
                return;
            }
            expenseToUpdate.setDescription(newDescription);
        }

        if (newAmount != null) {
            expenseToUpdate.setAmount(newAmount);
        }

        storage.updateJson(expenseToUpdate);
        expenses = storage.loadData();

        System.out.println("\n'" + descriptionOfExpenseToUpdate + "' has been updated successfully.");
    }

    public void deleteExpense(String descriptionOfExpenseToDelete) {
        if (isDescriptionEmptyOrNull(descriptionOfExpenseToDelete)) {
            throw new NullPointerException("\nDescription cannot be empty!");
        }

        Expense expenseToDelete = findExpenseToEdit(descriptionOfExpenseToDelete);

        if (doesExpenseNotExist(expenseToDelete)) {
            return;
        }

        storage.deleteExpenseInJson(expenseToDelete);
        expenses = storage.loadData();

        System.out.println("\n'" + descriptionOfExpenseToDelete + "' has been deleted successfully.");
    }

    public void listAllExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("There are no expenses. Try adding one.");
        }

        double sumOfAllExpenses = 0;
        for (Expense expense : expenses) {
            System.out.println(expense.toString());
            sumOfAllExpenses += expense.getAmount();
        }
        System.out.println("\n--- Total amount of expenses ---\n" + String.format("%.2f", sumOfAllExpenses));
    }

    public void getExpensesOfGivenMonth(int choiceOfMonth) {
        for (Month month : Month.values()) {

            if (month.getValue() == choiceOfMonth) {
                double allExpensesInMonth = sumOfAllExpensesInMonth(month);

                if (allExpensesInMonth != 0) {
                    System.out.println("\nTotal expenses in " + month + ": " + allExpensesInMonth);
                    return;
                }
                System.out.println("\nNo expenses found in " + month + ".");
            }
        }

    }

    private boolean doesDescriptionAlreadyExist(String description) {
        if (isDescriptionEmptyOrNull(description) || expenses == null) {
            return false;
        }
        String norm = normalize(description);
        for (Expense expense : expenses) {
            String eDesc = expense.getDescription();
            if (eDesc != null && normalize(eDesc).equals(norm)) {
                return true;
            }
        }
        return false;
    }

    private boolean isDescriptionEmptyOrNull(String descriptionToCheck) {
        return descriptionToCheck == null || normalize(descriptionToCheck).isEmpty();
    }

    private boolean doesExpenseNotExist(Expense expense) {
        return expense == null;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
    }

    private Expense findExpenseToEdit(String description) {
        String lowercaseDescription = normalize(description);

        if (isBlank(lowercaseDescription) || expenses == null) {
            System.out.println("\nExpense '" + description + "' does not exist!");
            return null;
        }
        String norm = normalize(lowercaseDescription);
        for (Expense expense : expenses) {
            String eDesc = expense.getDescription();
            if (eDesc != null && normalize(eDesc).equals(norm)) {
                return expense;
            }
        }
        System.out.println("\nExpense '" + lowercaseDescription + "' does not exist!");
        return null;
    }

    private double sumOfAllExpensesInMonth(Month month) {
        double sumOfAllExpensesInMonth = 0;

        for (Expense expense : expenses) {
            if (expense.getMonthOfCreation().equals(month)) {
                sumOfAllExpensesInMonth += expense.getAmount();
            }
        }

        return sumOfAllExpensesInMonth;
    }
}
