# Balance Management Application
#### Video Demo:  https://www.youtube.com/watch?v=caL3tuCMelk
#### Description: 

With my application, you can easily track your income and expenses. Let me walk you through the main features and functionalities it offers:

Login and Registration: When you open the application, you will be presented with a login form. If you don't have an account, you can easily register by providing your necessary details such as username, email address, and password.

Dashboard and Balance Report: After successfully logging in, you will be directed to the dashboard. The dashboard primarily consists of a balance report page that displays an overview of your financial situation. It includes information about your total income, total expenses, and the resulting net balance.

Income and Expense Tracking: Within the balance report page, you can navigate to the income and expense sections. Here, you have the ability to add and edit your income and expenses. You can enter details such as the amount, description, category, and date of each transaction. The application will then store this information for future reference.

User Roles and Permissions: The application differentiates between two types of users: admins and regular users. If you are an admin, you will have additional privileges and control over user management. As an admin, you can create, update, and delete user accounts, ensuring the security and integrity of the system. On the other hand, regular users will have limited access and functionality.

User Profile Management: Regular users will have access to a profile page where they can view their personal information and make updates if necessary. The profile page also displays the access logs, providing a history of when and from where the user has logged in.

Password Management: In the change password section, users have the option to modify their existing password. This feature enhances security and allows users to maintain control over their accounts.

Please note that the application does not require manual table creation, as it automatically generates the necessary tables for data storage. Specifically, it utilizes a table named "balance_db" on port 3306 to store and manage the financial information.

Overall, this application provides a convenient and user-friendly interface to effectively track income and expenses, allowing users to maintain control over their financial activities.

### Here are screenshots of my application UI.
___
![Login Page](https://github.com/htunkhainglynn/spring-balance-app/blob/main/assets/login.png)
![Balance Report](https://github.com/htunkhainglynn/spring-balance-app/blob/main/assets/balance-report.png)
![Income / Expense](https://github.com/htunkhainglynn/spring-balance-app/blob/main/assets/income.png)
![User Management](https://github.com/htunkhainglynn/spring-balance-app/blob/main/assets/user-management.png)
![Change Password](https://github.com/htunkhainglynn/spring-balance-app/blob/main/assets/change-pw.png)
![Access Logs](https://github.com/htunkhainglynn/spring-balance-app/blob/main/assets/access-logs.png)
![User Profile](https://github.com/htunkhainglynn/spring-balance-app/blob/main/assets/user-profile.png)

___

#### Usage
* You can register your account.
* Or you can access admin panel by loginId : admin, password: admin
* You can manage your balance

```
# clone this repo
git clone https://github.com/htunkhainglynn/spring-balance-app/tree/main

# create a mysql database named balance_db on port 3306 (I use hibernate, so you don't need to create tables)

# open eclipse or your preferred editor to run this application.
```
