package marcal.bank.entities.records;

public record BankResponse(String responseCode,
                           String responseMessage,
                           AccountInfo accountInfo) {
}
