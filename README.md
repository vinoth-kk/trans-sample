# Account Transaction Program

This project aims at providing the below details for an account in a given time frame:
 - Relative Account Balance
 - Number of Transactions
 
## The below Maven commands can be used to run the project in command prompt once the project is built

mvn clean build  
mvn exec:java -Dexec.mainClass="com.me.trans.AccountTransaction"  

**On Executing the project, Account number, From Date & To Date will be prompted. On giving the right value, output like below will get printed**  
Ex:  
Relative balance for the period is: $37.25  
Number of transactions included is: 3  

Note: For the demo purpose, it is assumed that the values will be given in a proper data format like below:  
**dd/MM/yyyy HH:mm:ss**  
**ex: 20/10/2018 09:00:00**  

## Below command can be used to run the test cases

mvn test

**Assumptions:
- CSV file containing transaction data is already available inside the project (inside resources folder)
- This is implementation is pure java, no UI work involved
 