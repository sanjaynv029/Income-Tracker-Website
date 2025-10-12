package com.example.expensetracker.controller;

import com.example.expensetracker.model.Expense;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class ExpenseController {

    private List<Expense> expenses = new ArrayList<>();
    private AtomicLong idCounter = new AtomicLong();
   
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("expenses", expenses);
        double totalAmount = expenses.stream().mapToDouble(Expense::getAmount).sum();
        model.addAttribute("totalAmount", totalAmount);
        return "index";
    }

    @GetMapping("/add-expense")
    public String showAddExpenseForm(Model model) {
        model.addAttribute("expense", new Expense());
        return "add-expense";
    }

    @PostMapping("/saveExpense")
    public String saveExpense(@ModelAttribute Expense expense) {
        if (expense.getId() == null) {
            expense.setId(idCounter.incrementAndGet());
            expenses.add(expense);
        } else {
            for (int i = 0; i < expenses.size(); i++) {
                if (expenses.get(i).getId().equals(expense.getId())) {
                    expenses.set(i, expense);
                    break;
                }
            }
        }
        return "redirect:/";
    }

    @GetMapping("/editExpense/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        for (Expense expense : expenses) {
            if (expense.getId().equals(id)) {
                model.addAttribute("expense", expense);
                return "update-expense";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/deleteExpense/{id}")
    public String deleteExpense(@PathVariable Long id) {
        expenses.removeIf(expense -> expense.getId().equals(id));
        return "redirect:/";
    }
}
