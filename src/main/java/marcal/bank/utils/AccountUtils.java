package marcal.bank.utils;

import java.time.Year;

public class AccountUtils {

    public static String generateAccountNumber() {

        Year currentYear = Year.now();
        int max = 100000;
        int min = 999999;

        int randomNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        String year = String.valueOf(currentYear);

        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNumber).toString();

    }

    public static String createUserName(String firstName, String lastName, String otherName) {
        return firstName + " " + lastName + " " + otherName;
    }
}
