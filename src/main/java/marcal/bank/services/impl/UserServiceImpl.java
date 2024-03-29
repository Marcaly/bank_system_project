package marcal.bank.services.impl;

import marcal.bank.entities.User;
import marcal.bank.entities.records.*;
import marcal.bank.repositories.UserRepository;
import marcal.bank.services.TransactionService;
import marcal.bank.services.UserService;
import marcal.bank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    TransactionService transactionService;


    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        User newUser = new User(userRequest.firstName(), userRequest.lastName(), userRequest.otherName(), userRequest.email(),
                userRequest.address(), userRequest.stateOfOrigin(), AccountUtils.generateAccountNumber(),
                BigDecimal.ZERO, "ACTIVE");

        userRepository.save(newUser);

        EmailDetails emailDetails = new EmailDetails(userRequest.email(), "Account successfully created.\nYour account number is: " + newUser.getAccountNumber(),"ACCOUNT CREATION");
        emailService.sendEmailAlert(emailDetails);

        return new BankResponse("003", "Account successfully created", new AccountInfo(AccountUtils.createUserName(newUser.getFirstName(), newUser.getLastName(),newUser.getOtherName()), newUser.getAccountBalance(), newUser.getAccountNumber()));
    }


    @Override
    public void verifyIfUserExists(String accountNumber) throws Exception {
        boolean isAccountExistsByAccountNumber = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExistsByAccountNumber) {
            throw new Exception("Account with this number doesn't exists");
        }
    }



    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) throws Exception {
        verifyIfUserExists(enquiryRequest.accountNumber());
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.accountNumber());

        return new BankResponse("004", "Account was successfully found",
                new AccountInfo(AccountUtils.createUserName(foundUser.getFirstName(), foundUser.getLastName(), foundUser.getOtherName()),
                        foundUser.getAccountBalance(), foundUser.getAccountNumber()));
    }

    @Override
    public void validateWithdraw(String accountNumber, BigDecimal amount) throws Exception {
        User user = userRepository.findByAccountNumber(accountNumber);
        verifyIfUserExists(accountNumber);

        if (user.getAccountBalance().compareTo(amount) < 0) {
            throw new Exception("Insufficient balance");
        }
    }

    @Override
    public BankResponse deposit(DepositWithdrawRequest deposit) throws Exception {
        verifyIfUserExists(deposit.accountNumber());
        User user = userRepository.findByAccountNumber(deposit.accountNumber());

        user.setAccountBalance(user.getAccountBalance().add(deposit.amount()));
        userRepository.save(user);

        EmailDetails emailDetails = new EmailDetails(user.getEmail(),"You deposited successfully " + deposit.amount() + " in you account!\nNew account balance: $"  + user.getAccountBalance() + user.getAccountNumber(),"DEPOSIT SUCCESS");
        emailService.sendEmailAlert(emailDetails);
        TransactionRecord transactionRecord = new TransactionRecord("Deposit", deposit.amount(), user.getAccountNumber(), user.getStatus());
        transactionService.saveTransaction(transactionRecord);

        return new BankResponse("137", "Account has been successfully debited",
                new AccountInfo(AccountUtils.createUserName(user.getFirstName(), user.getLastName(),user.getOtherName()), user.getAccountBalance(), user.getAccountNumber()));
    }

    @Override
    public BankResponse withdraw(DepositWithdrawRequest withdraw) throws Exception {
        validateWithdraw(withdraw.accountNumber(), withdraw.amount());

        User user = userRepository.findByAccountNumber(withdraw.accountNumber());

        user.setAccountBalance(user.getAccountBalance().subtract(withdraw.amount()));
        userRepository.save(user);

        EmailDetails emailDetails = new EmailDetails(user.getEmail(),"You have successfully withdrawn " + withdraw.amount() + " from your account!\nNew account balance: $" + user.getAccountBalance() + user.getAccountNumber(),"WITHDRAW SUCCESS");
        emailService.sendEmailAlert(emailDetails);

        TransactionRecord transactionRecord = new TransactionRecord("Withdraw", withdraw.amount(), user.getAccountNumber(), user.getStatus());
        transactionService.saveTransaction(transactionRecord);

        return new BankResponse("138", "Withdraw successfully! Current Account balance: " + user.getAccountBalance(),
                new AccountInfo(AccountUtils.createUserName(user.getFirstName(),user.getLastName(),user.getOtherName()),user.getAccountBalance(),user.getAccountNumber()));
    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) throws Exception {
        User sender = userRepository.findByAccountNumber(transferRequest.sourceAccountNumber());
        User receiver = userRepository.findByAccountNumber(transferRequest.destinationAccountNumber());

        validateWithdraw(sender.getAccountNumber(),transferRequest.amount());

        sender.setAccountBalance(sender.getAccountBalance().subtract(transferRequest.amount()));
        receiver.setAccountBalance(receiver.getAccountBalance().add(transferRequest.amount()));

        userRepository.save(sender);
        userRepository.save(receiver);

        EmailDetails senderEmailAlert = new EmailDetails(sender.getEmail(),"You have successfully transferred $" + transferRequest.amount() + " to " + AccountUtils.createUserName(receiver.getFirstName(), receiver.getLastName(),receiver.getOtherName()) + "!\nNew account balance: $" + sender.getAccountBalance() + sender.getAccountNumber(),"TRANSACTION SUCCESS");
        emailService.sendEmailAlert(senderEmailAlert);

        EmailDetails receiverEmailAlert = new EmailDetails(receiver.getEmail(),"you received a transaction worth $" + transferRequest.amount() + " from " + AccountUtils.createUserName(sender.getFirstName(), sender.getLastName(),sender.getOtherName()) + "!\nNew account balance: $" + receiver.getAccountBalance() + receiver.getAccountNumber(),"TRANSACTION RECEIVED SUCCESSFULLY");
        emailService.sendEmailAlert(receiverEmailAlert);

        TransactionRecord senderTransaction = new TransactionRecord("Transfer", transferRequest.amount(), sender.getAccountNumber(), sender.getStatus());
        TransactionRecord receiverTransaction = new TransactionRecord("Transfer", transferRequest.amount(), receiver.getAccountNumber(), receiver.getStatus());

        transactionService.saveTransaction(senderTransaction);
        transactionService.saveTransaction(receiverTransaction);


        return new BankResponse("855", "Transfer success", null);
    }


}
