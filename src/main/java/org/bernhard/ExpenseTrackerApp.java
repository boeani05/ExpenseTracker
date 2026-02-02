package org.bernhard;

import org.bernhard.logic.Manager;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class ExpenseTrackerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager expenseManager = new Manager();

        boolean doesMenuShow = true;

        while (doesMenuShow) {
            int menuInputChoice;

            System.out.println("""
                    
                    === CLI ExpenseTracker ===
                    1. Add Expense
                    2. Update Expense
                    3. Delete Expense
                    4. List Expenses
                    5. View summary of expenses (of certain month)
                    6. Exit
                    """);

            menuInputChoice = (int) checkInputChoice(scanner);

            switch (menuInputChoice) {
                case 1:
                    System.out.println("\n=== Enter the description of the new expense ===");
                    String newExpenseDescription = scanner.nextLine();

                    System.out.println("\n=== Enter the amount of the new expense ===");
                    double newExpenseAmount = checkInputChoice(scanner);

                    try {
                        expenseManager.addExpense(newExpenseDescription, newExpenseAmount);
                    } catch (NullPointerException | IllegalArgumentException e) {
                        System.err.println("\n" + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("\n=== Enter the description of the expense you would like to update ===");
                    String descriptionOfExpenseToUpdate = scanner.nextLine();

                    System.out.println("""
                            
                            Change:
                            1.) Description
                            2.) Amount
                            """);
                    int choiceOfWhatToUpdate = (int) checkInputChoice(scanner);

                    try {
                        if (choiceOfWhatToUpdate == 1) {
                            System.out.println("\n=== Enter the new description ===");
                            String newDescription = scanner.nextLine();

                            expenseManager.updateExpense(descriptionOfExpenseToUpdate, newDescription);
                        } else if (choiceOfWhatToUpdate == 2) {
                            System.out.println("\n=== Enter the new amount ===");
                            double newAmount = checkInputChoice(scanner);

                            expenseManager.updateExpense(descriptionOfExpenseToUpdate, newAmount);
                        } else {
                            System.out.println("\nInvalid input. Try again!");
                        }
                    } catch (NullPointerException | IllegalArgumentException e) {
                        System.err.println("\n" + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("\n=== Enter the description of the expense you would like to delete ===");
                    String descriptionOfExpenseToDelete = scanner.nextLine();

                    System.out.println("--- Do you really want to delete this expense(y/n)? ---");
                    String yesNoChoice = scanner.nextLine();

                    if (yesNoChoice.trim().toLowerCase(Locale.ROOT).equals("y")) {
                        expenseManager.deleteExpense(descriptionOfExpenseToDelete);
                    }
                    break;
                case 4:
                    System.out.println("\n=== All Expenses ===");
                    expenseManager.listAllExpenses();
                    break;
                case 5:
                    System.out.println("""
                            
                            === What expenses would you like to see? ===
                            1.) January
                            2.) February
                            3.) March
                            4.) April
                            5.) May
                            6.) June
                            7.) July
                            8.) August
                            9.) September
                            10.) October
                            11.) November
                            12.) December
                            """
                    );

                    int choiceOfMonth = (int) checkInputChoice(scanner);

                    if (!isInputValidMonth(choiceOfMonth)) {
                        System.out.println("\n--- This is not a valid month. Try again...");
                        continue;
                    }

                    expenseManager.getExpensesOfGivenMonth(choiceOfMonth);
                    break;
                case 6:
                    doesMenuShow = false;
                    break;
                default:
                    System.err.println("\nEnter an option of the menu!");
            }
        }
    }

    private static double checkInputChoice(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.err.println("\nError while reading input: " + e.getMessage());
            } finally {
                scanner.nextLine();
            }
        }
    }

    private static boolean isInputValidMonth(int choiceOfMonth) {
        return !(choiceOfMonth < 1 || choiceOfMonth > 12);
    }
}