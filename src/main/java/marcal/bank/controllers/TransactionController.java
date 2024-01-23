package marcal.bank.controllers;

import marcal.bank.entities.Transaction;
import marcal.bank.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber, @RequestParam String startDate, @RequestParam String endDate)  {
        return  transactionService.generateStatement(accountNumber, startDate, endDate);
    }
}
