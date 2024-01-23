package marcal.bank.entities.records;

import java.math.BigDecimal;

public record DepositWithdrawRequest (String accountNumber, BigDecimal amount){
}
