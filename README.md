# revolut-banking-example
# Simple implementation of RESTful API for money transfers between accounts.

## Task
Design and implement a RESTful API (including data model and the backing implementation) for money transfers between accounts.

### Explicit requirements:
1. You can use Java, Scala or Kotlin.
1. Keep it simple and to the point (e.g. no need to implement any authentication).
1. Assume the API is invoked by multiple systems and services on behalf of end users.
1. You can use frameworks/libraries if you like (except Spring), but don't forget about requirement #2 – keep it simple and avoid heavy frameworks.
1. The datastore should run in-memory for the sake of this test.
1. The final result should be executable as a standalone program (should not require a pre-installed container/server).
1. Demonstrate with tests that the API works as expected.

To run the application: run the jar in the target folder with the command:-
java -jar com.revolut.bank-1.0-SNAPSHOT.jar

Services exposed:
GET: http://localhost:8080/account/1 (get account by account id)
POST: http://localhost:8080/account (add new account)
DELETE: http://localhost:8080/account/1 (delete account by account id)
POST: http://localhost:8080/transact/transfer (transfer amount from one account to another)
POST: http://localhost:8080/transact/withdraw (withdraw money)
POST: http://localhost:8080/transact/deposit (deposit money)

Tech stack:
-java 8
-Tomcat server
-Maven
-google/gson
-junit, mockito
-log4j

Sample json for services exposed:

## http://localhost:8080/account
{
    "accountId": "11",
    "userName": "ABC",
    "accountBalance": 19627117.34

}

## http://localhost:8080/transact/transfer
{
    "fromAccount": "112121",
    "toAccount": "112121",
    "amount": 10989
}

## http://localhost:8080/transact/deposit
{
    "toAccount": "112121",
    "amount": 10989
}

## http://localhost:8080/transact/withdraw
{
        "fromAccount": "112121",
        "amount": 10989
}
