package marcal.bank.services.impl;

import marcal.bank.entities.records.BankResponse;
import marcal.bank.entities.records.EnquiryRequest;
import marcal.bank.entities.records.UserRequest;
import marcal.bank.repositories.UserRepository;
import marcal.bank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public void verifyIfUserAlreadyExists(EnquiryRequest enquiryRequest) {
        boolean isAccountExistsByEmail = userRepository.existsByEmail(enquiryRequest.email());
        boolean isAccountExistsByAccountNumber = userRepository.existsByAccountNumber(enquiryRequest.accountNumber());
        if (!isAccountExistsByEmail) {
            new BankResponse("001", "Account with this email not exists", null);
        }
        if (!isAccountExistsByAccountNumber) {
            new BankResponse("002", "Account with this Account number not exists", null);
        }

    }
}
