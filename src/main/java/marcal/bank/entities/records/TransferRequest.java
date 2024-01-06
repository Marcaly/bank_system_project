package marcal.bank.entities.records;

import java.math.BigDecimal;

public record TransferRequest (String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount){
}
