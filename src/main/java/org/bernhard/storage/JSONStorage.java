package org.bernhard.storage;

import org.bernhard.entity.Expense;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class JSONStorage {
    private final static Path PATH_TO_JSON = Path.of("src/main/resources/expenses.json");
    private final JSONObject mainJsonFile;
    private final JSONObject allJsonExpenses;

    public JSONStorage() throws IOException {
        if (!Files.exists(PATH_TO_JSON)) {
            Files.createFile(PATH_TO_JSON);

            Files.writeString(PATH_TO_JSON,
                    new JSONObject()
                            .put("nextId", 1)
                            .put("expenses", new JSONObject())
                            .toString(2)
            );
        }

        mainJsonFile = new JSONObject(Files.readString(PATH_TO_JSON));
        allJsonExpenses = mainJsonFile.getJSONObject("expenses");
    }

    public void add(Expense expense) {
        try {
            JSONObject expenseContainer = new JSONObject();

            int id = mainJsonFile.getInt("nextId");

            mainJsonFile.put("nextId", id + 1);

            expenseContainer.put("id", id)
                    .put("description", expense.getDescription())
                    .put("amount", expense.getAmount())
                    .put("date", expense.getMonthOfCreation());

            allJsonExpenses.put(String.valueOf(id), expenseContainer);

            Files.writeString(PATH_TO_JSON, mainJsonFile.toString(2));
        } catch (IOException e) {
            System.err.println("\nError while reading file: " + e.getMessage());
        }
    }

    public List<Expense> loadData() {
        try {
            String jsonExpensesContent = Files.readString(PATH_TO_JSON);
            JSONObject jsonObject = new JSONObject(jsonExpensesContent);
            JSONObject jsonIds = jsonObject.getJSONObject("expenses");

            return gatherAllExpenses(jsonIds);
        } catch (IOException e) {
            System.err.println("\nCould not read JSON file!");
            return List.of();
        }
    }

    public void updateJson(Expense expenseToUpdate) {
        String idOfExpenseToUpdate = String.valueOf(expenseToUpdate.getId());

        if (doesJsonHaveId(idOfExpenseToUpdate)) {
            System.err.println("\nCannot find expense with ID " + idOfExpenseToUpdate + " in JSON.");
            return;
        }

        JSONObject expenseToUpdateJson = allJsonExpenses.getJSONObject(idOfExpenseToUpdate);
        expenseToUpdateJson
                .put("description", expenseToUpdate.getDescription())
                .put("amount", expenseToUpdate.getAmount());

        try {
            Files.writeString(PATH_TO_JSON, mainJsonFile.toString(2));
        } catch (IOException e) {
            System.err.println("\nCannot update and write expense '" + expenseToUpdate.getDescription() + "' in JSON-File.");
        }
    }

    public void deleteExpenseInJson(Expense expenseToDelete) {
        String idOfExpenseToDelete = String.valueOf(expenseToDelete.getId());

        if (doesJsonHaveId(idOfExpenseToDelete)) {
            System.err.println("\nCannot find ID '" + idOfExpenseToDelete + "' in JSON-File.");
            return;
        }

        allJsonExpenses.remove(idOfExpenseToDelete);

        try {
            Files.writeString(PATH_TO_JSON, mainJsonFile.toString(2));
        } catch (IOException e) {
            System.err.println("\nCannot write in JSON file.");
        }
    }

    private List<Expense> gatherAllExpenses(JSONObject jsonObject) {
        List<Expense> foundExpenses = new ArrayList<>();
        JSONObject expensesJson;

        for (String key : jsonObject.keySet()) {
            expensesJson = jsonObject.getJSONObject(key);

            Expense expenseToAdd = new Expense(expensesJson.getString("description"), expensesJson.getDouble("amount"));
            expenseToAdd.setId(expensesJson.getInt("id"));
            expenseToAdd.setMonthOfCreation(Month.valueOf(expensesJson.getString("date")));

            foundExpenses.add(expenseToAdd);
        }
        return foundExpenses;
    }

    private boolean doesJsonHaveId(String id) {
        return !allJsonExpenses.has(id);
    }
}
