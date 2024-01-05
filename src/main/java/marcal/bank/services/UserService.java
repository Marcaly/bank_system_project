package marcal.bank.services;

import marcal.bank.entities.User;
import marcal.bank.entities.records.*;

import java.math.BigDecimal;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);

    void verifyIfUserExists(String accountNumber) throws Exception;
    String createUserName(String firstName, String LastName);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) throws Exception;

    void validateWithdraw(DepositWithdrawRequest request) throws Exception;

    BankResponse deposit (DepositWithdrawRequest deposit) throws Exception;

}
