Expense Split Tracker (Spring Boot)

### 🧾 Overview  
Expense Split Tracker is a Spring Boot–based REST API that helps users *create groups, **add shared expenses, **split costs* (equally, exact, or by percentage), *track balances, and **settle or simplify debts* — all viewable easily through *Postman*.


---

## 🏗 Tech Stack

| Layer | Technology |
|-------|-------------|
| Backend | Spring Boot |
| Language | Java |
| Build Tool | Maven |
| Database | In-memory (HashMaps — no DB needed) |
| Testing | Postman APIs |

---

## ⚙ Project Structure

ExpenseSplitTracker/
├── src/main/java/com/example/ExpenseSplitTracker/
│ ├── controller/
│ │ └── ExpenseController.java
│ ├── model/
│ │ ├── User.java
│ │ ├── Group.java
│ │ └── Expense.java
│ ├── services/
│ │ └── ExpenseService.java
│ ├── datastore/
│ │ └── DataStore.java
│ └── ExpenseSplitTrackerApplication.java
└── pom.xml

yaml
Copy code

---

## 🚀 How to Run the Project

1. *Clone the repo:*
   ```bash
   git clone https://github.com/your-username/ExpenseSplitTracker.git
Open in IntelliJ / VS Code / Eclipse

Run the application:

bash
Copy code
mvn spring-boot:run
or run ExpenseSplitTrackerApplication.java directly.

Test using Postman

Base URL: http://localhost:8080/api

🧩 Step-by-Step Features
🥇 Step 1 — Create Group and Add Users
Create a new group and add users to it.

API Endpoints:
Create Group

pgsql
Copy code
POST /api/group?name=Trip
Add User

bash
Copy code
POST /api/group/Trip/addUser?userName=Alice
POST /api/group/Trip/addUser?userName=Bob
POST /api/group/Trip/addUser?userName=Charlie
🥈 Step 2 — Equal Split Expense
Split expenses equally among all group members.

Example:
arduino
Copy code
POST /api/group/Trip/addExpense
Body (JSON):
{
  "description": "Dinner",
  "amount": 90,
  "paidBy": "Alice",
  "splitType": "EQUAL"
}
✅ Expected:
Each of 3 users owes $30.

🥉 Step 3 — Exact Amount Split
Specify exact amounts for each user.

Example:
bash
Copy code
POST /api/group/Trip/addExpense
Body:
{
  "description": "Hotel",
  "amount": 100,
  "paidBy": "Alice",
  "splitType": "EXACT",
  "splits": {
    "Alice": 70,
    "Bob": 30
  }
}
✅ Expected:
Alice pays $70, Bob pays $30.

🏅 Step 4 — Percentage Split
Split expense by percentages.

Example:
bash
Copy code
POST /api/group/Trip/addExpense
Body:
{
  "description": "Concert",
  "amount": 200,
  "paidBy": "Alice",
  "splitType": "PERCENTAGE",
  "splits": {
    "Alice": 60,
    "Bob": 40
  }
}
✅ Expected:
Alice → $120, Bob → $80.

🧾 Step 5 — View Balances
bash
Copy code
GET /api/group/Trip/balances
✅ Example Output:

yaml
Copy code
Balances for Trip:
Alice: 120.0
Bob: -80.0
💵 Step 6 — Settle Debts
bash
Copy code
POST /api/group/Trip/settle?fromUser=Bob&toUser=Alice&amount=80
✅ Expected Output:

nginx
Copy code
Bob settled $80.0 to Alice successfully!
Updated Balances:

makefile
Copy code
Alice: 40.0
Bob: 0.0
🔁 Step 7 — Simplify Debts
bash
Copy code
GET /api/group/Trip/simplify
✅ Example Output:

pgsql
Copy code
Bob pays $30.0 to Alice
Charlie pays $20.0 to Alice
💡 Reduces number of transactions automatically.

📜 Step 8 — Transaction History & Dashboard
bash
Copy code
GET /api/group/Trip/dashboard
✅ Example Output:

json
Copy code
{
  "balances": {
    "Alice": 40.0,
    "Bob": 0.0,
    "Charlie": 0.0
  },
  "expenses": [
    {"description": "Dinner", "amount": 90, "splitType": "EQUAL"},
    {"description": "Hotel", "amount": 100, "splitType": "EXACT"}
  ],
  "settlements": [
    "Bob paid $80.0 to Alice"
  ],
  "simplifiedDebts": "No debts to simplify!"
}
🧠 Test Cases Summary
Test Case	Scenario	Expected Result
1. Equal Split	3 users, $90 expense	$30 each
2. Exact Split	User A $70, User B $30	Balances updated correctly
3. Percentage Split	$200 → 60%-40%	User A $120, User B $80
4. Settle Debt	A owes $50, pays $50	Balance becomes 0
5. Simplify Debts	A owes $30, B owes $20	Simplified to one transaction
6. Invalid Settlement	Trying to pay more than owed	Error: "Cannot settle more than owed"
7. Currency Validation (optional)	Different currencies	System rejects or converts automatically

✅ Validations Implemented
Prevent overpayment

Handle floating-point rounding

Validate group and user existence

Prevent invalid splits or missing members

🌟 Future Improvements
Currency conversion API integration

Email/notification when debts are settled

Simple React frontend dashboard

Database integration (MySQL or MongoDB)

🧑‍💻 Author
Mandala Lalith Kumar
🚀 Aspiring Software Engineer passionate about building scalable backend systems.
📧 lalithkumar.m01@gmail.com













ChatGPT can make mistakes. Check important info. See Cookie Preferences.
