package marcal.bank.services.impl;

import marcal.bank.entities.User;
import marcal.bank.entities.records.AccountInfo;
import marcal.bank.entities.records.BankResponse;
import marcal.bank.entities.records.EmailDetails;
import marcal.bank.entities.records.UserRequest;
import marcal.bank.repositories.UserRepository;
import marcal.bank.services.TransactionService;
import marcal.bank.utils.AccountUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private TransactionService transactionService;
    @Mock
    private AccountUtils accountUtils;

    @InjectMocks
    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void createAccount() {
        

    }
}

//    @Override
//    public BankResponse createAccount(UserRequest userRequest) {
//
//        User newUser = new User(userRequest.firstName(), userRequest.lastName(), userRequest.otherName(), userRequest.email(),
//                userRequest.address(), userRequest.stateOfOrigin(), AccountUtils.generateAccountNumber(),
//                BigDecimal.ZERO, "ACTIVE");
//
//        userRepository.save(newUser);
//
//        EmailDetails emailDetails = new EmailDetails(userRequest.email(), "Account successfully created.\nYour account number is: " + newUser.getAccountNumber(),"ACCOUNT CREATION");
//        emailService.sendEmailAlert(emailDetails);
//
//        return new BankResponse("003", "Account successfully created", new AccountInfo(accountUtils.createUserName(newUser.getFirstName(), newUser.getLastName(),newUser.getOtherName()), newUser.getAccountBalance(), newUser.getAccountNumber()));
//    }