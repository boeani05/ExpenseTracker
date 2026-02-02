# CLI Expense Tracker (Java)

This project was inspired by practical application ideas often found on platforms like [roadmap.sh/projects/expense-tracker](https://roadmap.sh/projects/expense-tracker), aiming to solidify core Java development skills.

A simple command-line interface (CLI) application developed in Java to help users track and manage their personal
expenses. This application allows you to add, update, delete, list, and view summaries of your expenses, with data
persisted to a JSON file.

## Features

- **Add Expense**: Record new expenses with a description and an amount. The month of creation is automatically set.
- **Update Expense**: Modify the description or amount of an existing expense.
- **Delete Expense**: Remove an expense from the tracker.
- **List All Expenses**: View a comprehensive list of all recorded expenses, along with the total sum.
- **View Monthly Summary**: Get a summary of expenses for any specific month of the year.
- **Data Persistence**: All expenses are saved to a `expenses.json` file, so your data is retained even after closing
  the application.

## Technologies Used

- Java 17+
- `org.json` library (for JSON parsing and manipulation)

## How to Use

Follow the prompts to interact with the expense tracker:

- **Add Expense**: Enter a description and amount.
- **Update Expense**: Provide the description of the expense to update, then choose whether to change its description or
  amount.
- **Delete Expense**: Enter the description of the expense to delete and confirm.
- **List Expenses**: Displays all expenses and their total.
- **View Monthly Summary**: Choose a month (1-12) to see expenses for that specific month.
- **Exit**: Close the application.

The application handles basic input validation and provides error messages for invalid inputs or operations.

## Future Enhancements (TODOs from Manager.java)

The following features are planned for future development:

- Add expense categorization and filtering by category.
- Implement budget tracking functionality.
- Add an option to export expenses to a CSV file.

Thanks to roadm
