package marcal.bank.services;

import marcal.bank.entities.Transaction;
import marcal.bank.entities.records.TransactionRecord;

import java.util.List;

public interface TransactionService {

    void saveTransaction(TransactionRecord transactionRecord);

    List<Transaction> generateStatement(String accountNumber, String startDate, String endDate);
}
