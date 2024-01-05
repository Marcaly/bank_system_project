package marcal.bank.services.impl;

import marcal.bank.entities.User;
import marcal.bank.entities.records.*;
import marcal.bank.repositories.UserRepository;
import marcal.bank.services.UserService;
import marcal.bank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        User newUser = new User(userRequest.firstName(), userRequest.lastName(), userRequest.email(),
                userRequest.address(), userRequest.stateOfOrigin(), AccountUtils.generateAccountNumber(),
                BigDecimal.ZERO, "ACTIVE");

        userRepository.save(newUser);

        return new BankResponse("003", "Account successfully created", new AccountInfo(createUserName(newUser.getFirstName(), newUser.getLastName()), newUser.getAccountBalance(), newUser.getAccountNumber()));
    }


    @Override
    public void verifyIfUserExists(String accountNumber) throws Exception {
        boolean isAccountExistsByAccountNumber = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExistsByAccountNumber) {
            throw new Exception("Account with this number not exists");
        }
    }

    @Override
    public String createUserName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) throws Exception {
        verifyIfUserExists(enquiryRequest.accountNumber());
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.accountNumber());

        return new BankResponse("004", "Account was successfully found",
                new AccountInfo(createUserName(foundUser.getFirstName(), foundUser.getLastName()),
                        foundUser.getAccountBalance(), foundUser.getAccountNumber()));
    }

    @Override
    public void validateWithdraw(DepositWithdrawRequest request) throws Exception {
        User user = userRepository.findByAccountNumber(request.accountNumber());
        verifyIfUserExists(request.accountNumber());

        if (user.getAccountBalance().compareTo(request.amount()) < 0) {
            throw new Exception("Insuficient balance");
        }
    }

    @Override
    public BankResponse deposit(DepositWithdrawRequest deposit) throws Exception {
        verifyIfUserExists(deposit.accountNumber());
        User user = userRepository.findByAccountNumber(deposit.accountNumber());

        user.setAccountBalance(user.getAccountBalance().add(deposit.amount()));
        userRepository.save(user);

        return new BankResponse("137", "Account has been successfully debited", new AccountInfo(createUserName(user.getFirstName(),user.getLastName()),user.getAccountBalance(),user.getAccountNumber()));
    }


}
