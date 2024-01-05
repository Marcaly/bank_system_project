package marcal.bank.controllers;

import marcal.bank.entities.records.BankResponse;
import marcal.bank.entities.records.DepositWithdrawRequest;
import marcal.bank.entities.records.EnquiryRequest;
import marcal.bank.entities.records.UserRequest;
import marcal.bank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @GetMapping("balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request) throws Exception {
        return userService.balanceEnquiry(request);
    }

    @PostMapping("deposit")
    public BankResponse deposit(@RequestBody DepositWithdrawRequest deposit) throws Exception {
        return userService.deposit(deposit);
    }
}
