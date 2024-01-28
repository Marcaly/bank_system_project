# BANKING SYSTEM

Banking System Project that is capable of creating a user, conducting deposits, withdrawals, performing transfers between users, checking user balances, and consult the transaction history of a user over a specific time period.

## Technologies

* Java - version 21
* Spring Boot - version 3.2.1
* Docker
* H2 Database

## Dependencies

* Maven 
* Jpa / Hibernate
* Junit e Mockito
* JavaMailSender


## How to Run the Project ✅

With Docker open, open a command prompt and paste the command to download a project image:

```
docker pull marcalx/bank-app:0.0.2
```

Then, to start the application, run the following command:

```
docker run -p 8080:8080 marcalx/bank-app:0.0.2
```


## Endpoints

```
Método POST
http://localhost:8080/users/createUser

Creates a user and saves it in the database.

Returns a 200 code and sends an email to the registered email.
```
![Foto 1](https://github.com/Marcaly/bank_system_project/blob/main/imgs/create.png)

```
Método POST
http://localhost:8080/users/deposit

Deposits an amount into the desired account.

Returns a 200 code and sends an email notifying the user that the deposit was successful. 
```
![Foto 2](https://github.com/Marcaly/bank_system_project/blob/main/imgs/deposit.png)

```
Método POST
http://localhost:8080/users/withdraw

Withdraws an amount from the desired account.

Returns a 200 code and sends an email notifying the user that the withdrawal was successful. 
```
![Foto 3](https://github.com/Marcaly/bank_system_project/blob/main/imgs/withdraw.png)



```
Método POST
http://localhost:8080/users/transfer

Performs a transfer between two users.

Returns a 200 code and sends an email notifying the users that the transfer was successful. 
```
![Foto 4](https://github.com/Marcaly/bank_system_project/blob/main/imgs/transfer.png)



```
Método GET
http://localhost:8080/users/balanceEnquiry

Returns a 200 code with the data of the desired account. 
```
![Foto 5](https://github.com/Marcaly/bank_system_project/blob/main/imgs/transfer.png)

```
Método GET
http://localhost:8080/users/transactions

Returns all transactions of a user within a time range, given a start date, an end date, and the user's account number. 

```
![Foto 4](https://github.com/Marcaly/bank_system_project/blob/main/imgs/transactions.png)



## ⏭️ Next Steps

* Add JWT authentication
* Improve unit tests
  
