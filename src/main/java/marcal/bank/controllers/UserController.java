package marcal.bank.controllers;

import marcal.bank.entities.records.*;
import marcal.bank.services.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<BankResponse> createAccount(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok().body(userService.createAccount(userRequest));
    }

    @GetMapping("balanceEnquiry")
    public ResponseEntity<BankResponse> balanceEnquiry(@RequestBody EnquiryRequest request) throws Exception {
        return ResponseEntity.ok(userService.balanceEnquiry(request));
    }

    @PostMapping("deposit")
    public BankResponse deposit(@RequestBody DepositWithdrawRequest deposit) throws Exception {
        return userService.deposit(deposit);
    }

    @PostMapping("withdraw")
    public BankResponse withdraw(@RequestBody DepositWithdrawRequest withdraw) throws Exception {
        return userService.withdraw(withdraw);
    }

    @PostMapping("transfer")
    public BankResponse transfer(@RequestBody TransferRequest transferRequest) throws Exception {
        return userService.transfer(transferRequest);
    }
}
