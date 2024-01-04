package marcal.bank.services;

import marcal.bank.entities.records.EnquiryRequest;
import marcal.bank.entities.records.UserRequest;

public interface UserService {

    void verifyIfUserAlreadyExists(EnquiryRequest enquiryRequest);

}
