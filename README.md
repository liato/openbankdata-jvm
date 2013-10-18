openbankdata-jvm
================

##Examples

###Authenticating

````java
//Basic authentication
BankClient client = new HandelsbankenBankClient();
client.setCredentials("user", "password");
````

###Get a user's bank accounts

The following example prints the account name for each account associated the given user.
````java
AccountService service = new HandelsbankenAccountService();
service.getClient().setCredentials("user", "password");
for(Account account : service.getAccounts()) {
    System.out.println(account.getName());
}
````

###Get an account's transactions
The following example prints all transactions associated with the given account.
````java
BankClient client = new HandelsbankenBankClient("username","password");
AccountService accountService = new HandelsBankenAccountService(client);
TransactionService transactionService = new HandelsbankenTransactionService(client);

List<Account> accounts = accountService.getAccounts();
if(!accounts.isEmpty()) {
    Account account = accounts.get(0);
    for(Transaction transaction : transactionService.getTransactions(account)) {
      System.out.println(transaction.getDescription() +" - "+ transaction.getAmount());
    }
}
````

