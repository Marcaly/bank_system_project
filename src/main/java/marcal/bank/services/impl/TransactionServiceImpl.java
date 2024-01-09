package marcal.bank.services.impl;

import marcal.bank.entities.Transaction;
import marcal.bank.entities.records.TransactionRecord;
import marcal.bank.repositories.TransactionRepository;
import marcal.bank.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(TransactionRecord transactionRecord) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionRecord.transactionType());
        transaction.setAmount(transactionRecord.amount());
        transaction.setAccountNumber(transactionRecord.accountNumber());
        transaction.setStatus("ACTIVE");
        transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully");
    }


    @Override
    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        return transactionRepository.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().equals(start)).filter(transaction -> transaction.getCreatedAt().equals(end)).toList();
    }
}
