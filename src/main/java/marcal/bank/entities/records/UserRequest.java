package marcal.bank.entities.records;

public record UserRequest(String firstName,
                          String lastName,
                          String otherName,
                          String address,
                          String stateOfOrigin,
                          String email){}
