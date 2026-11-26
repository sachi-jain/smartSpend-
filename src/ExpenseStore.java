import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class ExpenseStore {
    private final Path file = Path.of("expenses.tsv");

    ArrayList<Expense> load() throws IOException {
        ArrayList<Expense> expenses = new ArrayList<>();
        if (Files.notExists(file)) {
            return expenses;
        }
        try (var reader = Files.newBufferedReader(file)) {
            int lineNumber = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] fields = line.split("\t", -1);
                    if (fields.length != 6) {
                        throw new IllegalArgumentException("Expected six fields.");
                    }
                    List<String> participants = fields[5].isEmpty()
                            ? List.of() : Arrays.asList(fields[5].split(",", -1));
                    expenses.add(new Expense(Long.parseLong(fields[0]), fields[1],
                            LocalDate.parse(fields[2]), fields[3], fields[4], participants));
                } catch (IllegalArgumentException | java.time.DateTimeException error) {
                    System.err.println("Skipping invalid row " + lineNumber + " in " + file + ".");
                }
            }
        }
        return expenses;
    }

    void append(Expense expense) throws IOException {
        String row = String.join("\t", Long.toString(expense.cents()), expense.category(),
                expense.date().toString(), expense.description(), expense.payer(),
                String.join(",", expense.participants()));
        // Separate a new row even if a text editor removed the final newline.
        boolean needsNewline = false;
        if (Files.exists(file) && Files.size(file) > 0) {
            try (var existing = new java.io.RandomAccessFile(file.toFile(), "r")) {
                existing.seek(existing.length() - 1);
                needsNewline = existing.read() != '\n';
            }
        }
        Files.writeString(file, (needsNewline ? "\n" : "") + row + "\n",
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
