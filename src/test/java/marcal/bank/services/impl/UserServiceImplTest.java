package marcal.bank.services.impl;

import marcal.bank.entities.User;
import marcal.bank.entities.records.*;
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
import static org.mockito.Mockito.*;


class UserServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount() {

        UserRequest userRequest = new UserRequest("Gabryel", "Marçal", "Almeida", "Sao Paulo", "SP", "gmmarcal2@gmail.com");
        User expectedUser = new User(userRequest.firstName(), userRequest.lastName(), userRequest.otherName(), userRequest.address(), userRequest.stateOfOrigin(), userRequest.email(), AccountUtils.generateAccountNumber(),
                BigDecimal.ZERO, "ACTIVE");

        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        BankResponse response = userService.createAccount(userRequest);

        verify(emailService).sendEmailAlert(any(EmailDetails.class));
        assertNotNull(response);
        assertEquals("003", response.responseCode());
        assertEquals("Account successfully created", response.responseMessage());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void transfer() throws Exception {

        String senderAccountNumber = AccountUtils.generateAccountNumber();
        String receiverAccountNumber = AccountUtils.generateAccountNumber();

        User sender = new User("Gabryel", "Marçal", "Almeida", "Sao Paulo", "SP", "gmmarcal2@gmail.com", senderAccountNumber,
                BigDecimal.valueOf(1000.0), "ACTIVE");
        User receiver = new User("Maria", "Pires", "Silva", "Sao Paulo", "SP", "maria@gmail.com", receiverAccountNumber,
                BigDecimal.valueOf(100.0), "ACTIVE");

        when(userRepository.findByAccountNumber(senderAccountNumber)).thenReturn(sender);
        when(userRepository.findByAccountNumber(receiverAccountNumber)).thenReturn(receiver);
        when(userRepository.existsByAccountNumber(any())).thenReturn(true);

        TransferRequest request = new TransferRequest(sender.getAccountNumber(), receiver.getAccountNumber(), BigDecimal.valueOf(200.0));
        BankResponse response = userService.transfer(request);

        assertEquals(new BigDecimal("800.0"), sender.getAccountBalance());
        assertEquals(new BigDecimal("300.0"), receiver.getAccountBalance());

        verify(userRepository, times(2)).save(any(User.class));
        verify(emailService, times(2)).sendEmailAlert(any(EmailDetails.class));

        assertEquals("855", response.responseCode());
        assertEquals("Transfer success", response.responseMessage());

        verify(transactionService, times(2)).saveTransaction(any(TransactionRecord.class));
    }

    @Test
    void transferShouldThrowException_whenUserNotFound() throws Exception {

        String invalidAccountNumber = "99999";

        when(userRepository.existsByAccountNumber(any())).thenReturn(false);

        TransferRequest request = new TransferRequest(invalidAccountNumber, "12345", BigDecimal.valueOf(200.0));

        assertThrows(Exception.class, () -> {
            userService.transfer(request);
        });
    }


    @Test
    void balanceEnquiry() throws Exception {

        User user = new User("Gabryel", "Marçal", "Almeida", "Sao Paulo", "SP", "gmmarcal2@gmail.com", "123456",
                BigDecimal.valueOf(1000.0), "ACTIVE");

        when(userRepository.findByAccountNumber(any())).thenReturn(user);
        when(userRepository.existsByAccountNumber(any())).thenReturn(true);

        EnquiryRequest enquiryRequest = new EnquiryRequest(user.getAccountNumber());
        BankResponse bankResponse = userService.balanceEnquiry(enquiryRequest);

        assertEquals("004", bankResponse.responseCode());
        assertEquals("Account was successfully found", bankResponse.responseMessage());
        assertEquals("Gabryel Marçal Almeida", bankResponse.accountInfo().accountName());
        assertEquals(BigDecimal.valueOf(1000.0), bankResponse.accountInfo().accountBalance());
        assertEquals("123456", bankResponse.accountInfo().accountNumber());
    }

    @Test
    void deposit() throws Exception {
        User user = new User("Gabryel", "Marçal", "Almeida", "Sao Paulo", "SP", "gmmarcal2@gmail.com", "123456",
                BigDecimal.valueOf(1000.0), "ACTIVE");

        when(userRepository.findByAccountNumber(any())).thenReturn(user);
        when(userRepository.existsByAccountNumber(any())).thenReturn(true);

        DepositWithdrawRequest request = new DepositWithdrawRequest(user.getAccountNumber(), BigDecimal.valueOf(300.0));

        BankResponse response = userService.deposit(request);

        assertEquals("137", response.responseCode());
        assertEquals("Account has been successfully debited", response.responseMessage());

        assertEquals(BigDecimal.valueOf(1300.0), response.accountInfo().accountBalance());
        verify(userRepository, times(1)).save(any(User.class));
        verify(transactionService, times(1)).saveTransaction(any(TransactionRecord.class));
        verify(emailService,times(1)).sendEmailAlert(any(EmailDetails.class));
    }

    @Test
    void withdraw() throws Exception {

        User user = new User("Gabryel", "Marçal", "Almeida", "Sao Paulo", "SP", "gmmarcal2@gmail.com", "123456",
                BigDecimal.valueOf(1000.0), "ACTIVE");

        when(userRepository.findByAccountNumber(any())).thenReturn(user);
        when(userRepository.existsByAccountNumber(any())).thenReturn(true);

        DepositWithdrawRequest request = new DepositWithdrawRequest(user.getAccountNumber(), BigDecimal.valueOf(300.0));

        BankResponse response = userService.withdraw(request);

        assertEquals("138", response.responseCode());
        assertEquals("Withdraw successfully! Current Account balance: 700.0", response.responseMessage());

        assertEquals(BigDecimal.valueOf(700.0), response.accountInfo().accountBalance());
        verify(userRepository, times(1)).save(any(User.class));
        verify(transactionService, times(1)).saveTransaction(any(TransactionRecord.class));
        verify(emailService,times(1)).sendEmailAlert(any(EmailDetails.class));

    }
}