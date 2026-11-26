import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    private final ExpenseStore store = new ExpenseStore();
    private final Scanner input;
    private final ArrayList<Expense> expenses;

    private Main(Scanner input) throws IOException {
        this.input = input;
        expenses = store.load();
    }

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            new Main(input).run();
        } catch (IOException error) {
            System.err.println("Could not read expenses.tsv: " + error.getMessage());
        }
    }

    private void run() {
        System.out.println("SmartSpend");
        while (true) {
            System.out.println("\n1. Add expense   2. View expenses   3. Shared balances"
                    + "\n0. Exit");
            try {
                switch (ask("Choose")) {
                    case "1" -> addExpense();
                    case "2" -> viewExpenses();
                    case "3" -> viewBalances();
                    case "0" -> { return; }
                    default -> System.out.println("Choose a number from the menu.");
                }
            } catch (NoSuchElementException endOfInput) {
                return;
            } catch (DateTimeParseException error) {
                System.out.println("Use a valid date (YYYY-MM-DD).");
            } catch (IllegalArgumentException error) {
                System.out.println(error.getMessage());
            } catch (IOException error) {
                System.out.println("Could not save the expense: " + error.getMessage());
            }
        }
    }

    private void addExpense() throws IOException {
        long cents = Expense.parseCents(ask("Amount in dollars"));
        String category = ask("Category");
        String dateInput = ask("Date (YYYY-MM-DD or Enter for today)");
        LocalDate date = dateInput.isEmpty() ? LocalDate.now() : LocalDate.parse(dateInput);
        String description = ask("Description");
        String payer = ask("Who paid? (Enter for a personal expense)");
        List<String> participants = payer.isEmpty() ? List.of() : Arrays.asList(
                ask("Comma-separated names of everyone sharing, including the payer if applicable")
                        .split(",", -1));
        Expense expense = new Expense(cents, category, date, description, payer, participants);
        store.append(expense);
        expenses.add(expense);
        System.out.println("Expense saved.");
    }

    private void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses yet.");
        }
        for (Expense expense : expenses) {
            String sharing = expense.payer().isEmpty() ? "" : " | paid by " + expense.payer()
                    + " | split among " + String.join(", ", expense.participants());
            System.out.println(expense.date() + " | " + Expense.dollars(expense.cents())
                    + " | " + expense.category() + " | " + expense.description() + sharing);
        }
    }

    private void viewBalances() {
        TreeMap<String, Long> balances = new TreeMap<>();
        expenses.forEach(expense -> expense.addToBalances(balances));
        if (balances.isEmpty()) {
            System.out.println("No shared expenses yet.");
        }
        balances.forEach((name, cents) -> {
            if (cents > 0) {
                System.out.println(name + " is owed " + Expense.dollars(cents));
            } else if (cents < 0) {
                System.out.println(name + " owes " + Expense.dollars(-cents));
            } else {
                System.out.println(name + " is settled.");
            }
        });
    }

    private String ask(String prompt) {
        System.out.print(prompt + ": ");
        return input.nextLine().strip();
    }
}
