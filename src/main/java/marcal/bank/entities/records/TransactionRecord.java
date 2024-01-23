package marcal.bank.entities.records;

import java.math.BigDecimal;

public record TransactionRecord(String transactionType,
                                BigDecimal amount,
                                String accountNumber,
                                String status) {
}
