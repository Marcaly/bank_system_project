package marcal.bank.entities.records;

import java.math.BigDecimal;

public record AccountInfo(
        String accountName,
        BigDecimal accountBalance,
        String accountNumber) {
}
