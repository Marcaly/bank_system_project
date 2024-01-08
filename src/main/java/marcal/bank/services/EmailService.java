package marcal.bank.services;

import marcal.bank.entities.records.EmailDetails;

public interface EmailService {

    void sendEmailAlert(EmailDetails emailDetails);

}
