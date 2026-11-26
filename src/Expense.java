import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;

record Expense(long cents, String category, LocalDate date, String description,
               String payer, List<String> participants) {

    Expense {
        if (cents <= 0 || cents > 100_000_000) {
            throw new IllegalArgumentException("Enter an amount from 0.01 to 1000000.00.");
        }
        category = field(category).toLowerCase(Locale.ROOT);
        description = field(description);
        payer = payer.isBlank() ? "" : name(payer);
        participants = participants.stream().map(Expense::name).distinct().toList();
        if (payer.isEmpty() != participants.isEmpty()) {
            throw new IllegalArgumentException("A shared expense needs a payer and participants.");
        }
    }

    static long parseCents(String input) {
        if (!input.matches("[0-9]{1,7}(\\.[0-9]{1,2})?")) {
            throw new IllegalArgumentException("Use a dollar amount like 12.50.");
        }
        return new BigDecimal(input).movePointRight(2).longValueExact();
    }

    static String dollars(long cents) {
        return (cents < 0 ? "-$" : "$") + BigDecimal.valueOf(cents, 2).abs().toPlainString();
    }

    void addToBalances(Map<String, Long> balances) {
        if (participants.isEmpty()) {
            return;
        }
        balances.merge(payer, cents, Long::sum);
        long baseShare = cents / participants.size();
        long leftover = cents % participants.size();
        // Give the first participants the extra pennies so the shares add up exactly.
        for (int i = 0; i < participants.size(); i++) {
            long share = baseShare + (i < leftover ? 1 : 0);
            balances.merge(participants.get(i), -share, Long::sum);
        }
    }

    private static String field(String value) {
        if (value.isBlank() || value.chars().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException("Text fields need a value and cannot contain control characters.");
        }
        return value.strip();
    }

    private static String name(String value) {
        String normalized = field(value).toLowerCase(Locale.ROOT);
        if (normalized.contains(",")) {
            throw new IllegalArgumentException("Names cannot contain commas.");
        }
        return normalized;
    }
}
