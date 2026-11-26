# SmartSpend development plan

The source files in this folder form a working reference. If you are building in a course repository, open your own copy and use the sequence below to work through the code. Keep any Git commits you have already made. Commit changes when you actually make and verify them.

## Build order

| When | File or change | Check before committing | Example commit message |
| --- | --- | --- | --- |
| Week 10 | Start with `src/Expense.java` and a minimal `src/Main.java` | Compile and launch the menu | Add expense model and terminal menu |
| Week 10 | Implement entering and viewing expenses in `Main.java` | Add a personal expense and confirm the list displays its fields | Add expense entry and listing |
| Week 10–11 | Add shared-entry prompts and balance calculations in `Main.java` and `Expense.java` | Split $10 among three people and check every cent | Calculate shared balances in cents |
| Week 11 | Add `src/ExpenseStore.java` and connect it to `Main.java`; add `.gitignore` | Exit and relaunch. Check that a missing data file starts empty | Save and reload expense records |
| Week 11 | Add setup, example, and system diagram | Compile and run from a clean checkout | Document setup and expense workflow |
| Week 12 | Create `feature/monthly-summary` from your working MVP and add option 4 | Confirm summary filters by month and add/view/split/restart still work | Summarize monthly spending by category |
| Week 12 | Merge the tested branch into `main` and add the video link | Follow the public repo's README in a fresh clone and open the video link while signed out | Link the SmartSpend walkthrough |

The monthly feature uses the `YearMonth` import, menu option 4 and its switch case, and `viewMonthlySummary()` in `Main.java`. Your homework's Eisenhower Matrix belongs in `docs/` if you have a copy.

## Video outline (3–4 minutes)

- **0:00–0:30** Explain why shared expenses get confusing and show the menu.
- **0:30–1:30** Add the $10 pizza example, view it, and show the three balances. Explain where the extra cent went.
- **1:30–2:15** Restart the app to show persistence. Open `ExpenseStore.java` and point out how loading and saving work.
- **2:15–3:00** Run the monthly summary. Explain how it filters expenses by month and sums each category.
- **3:00–3:40** Show your Git history and feature branch, and explain one real change you made while testing.
