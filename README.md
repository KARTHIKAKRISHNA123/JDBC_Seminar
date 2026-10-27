# MySQL, JDBC and Regex in Java — 90-Minute Lecture Guide

A ready-to-teach, beginner-level lecture. Students should know basic Java (variables, loops, methods, classes). By the end they will:

- write simple SQL to create, read, update and delete rows in MySQL
- connect Java to MySQL with JDBC and run those same queries from code, safely
- use regular expressions to validate and extract text in Java
- see all three working together in a small Student Registration app

Every program in this kit was compiled and run on Java 21 with MySQL 8.0. Each `.java` file ends with an **EXPECTED OUTPUT** comment, so you can check your screen during the class.

---

## Contents

1. [The 90-minute plan](#1-the-90-minute-plan)
2. [Setup — do this the day before](#2-setup--do-this-the-day-before)
3. [Opening: three jobs behind every form (5 min)](#3-opening-three-jobs-behind-every-form-5-min)
4. [Module 1 — MySQL basics (20 min)](#4-module-1--mysql-basics-20-min)
5. [Module 2 — JDBC: Java talks to MySQL (30 min)](#5-module-2--jdbc-java-talks-to-mysql-30-min)
6. [Module 3 — Regex in Java (25 min)](#6-module-3--regex-in-java-25-min)
7. [Wrap-up: mini project, quiz, Q&A (10 min)](#7-wrap-up-mini-project-quiz-qa-10-min)
8. [Cheat sheet](#8-cheat-sheet)
9. [Troubleshooting](#9-troubleshooting)
10. [What's in the kit](#10-whats-in-the-kit)
11. [Videos to watch](#11-videos-to-watch)

---

## 1. The 90-minute plan

| Time | Part | Demo files | Students can… |
|---|---|---|---|
| 0:00–0:05 | Opening: store, connect, check | — | name the three jobs |
| 0:05–0:25 | MySQL basics | `sql/01_setup.sql`, `sql/02_mysql_practice.sql` | write SELECT / INSERT / UPDATE / DELETE with WHERE |
| 0:25–0:55 | JDBC | J01, J03, J04, J06, J07, J08 | connect from Java, do CRUD, explain SQL injection |
| 0:55–1:20 | Regex | R01, R02, R13, R05, R07, R08 | validate a phone number, use `matches()` vs `find()` |
| 1:20–1:30 | Wrap-up | `project/v1-single-file` | see all three in one app; answer the quiz |

**Teaching pattern for every topic:** ask a question → give an everyday analogy → run the demo → point at 2–3 key lines → show the output → show one common mistake.

**Left out on purpose** (mention them as "next steps" if asked): transactions, batch inserts, stored procedures, connection pooling, lookaheads, flags, catastrophic backtracking. The demo files for them are still in the kit (J09–J17, R09–R19).

---

## 2. Setup — do this the day before

Do a full dry run on the laptop you will present from. Most live-demo failures are setup problems.

| # | Install | Check |
|---|---|---|
| 1 | **JDK 17 or 21** (the code uses text blocks `"""`, which need Java 15+) | `java -version` |
| 2 | **MySQL Server 8.4 LTS** with MySQL Installer (Windows). Note the root password. | MySQL Workbench connects |
| 3 | **MySQL Connector/J** — from [dev.mysql.com/downloads/connector/j](https://dev.mysql.com/downloads/connector/j/), choose *Platform Independent*, download the ZIP, copy the `.jar` into `lib/`. Take the newest version; avoid 9.7.0–9.7.1 (July 2026 security advisory). | the jar is in `lib/` |

> **XAMPP labs:** XAMPP ships MariaDB instead of MySQL. Every demo still works; root usually has an empty password, and error messages are worded slightly differently.

**Create the database:** open `sql/01_setup.sql` in MySQL Workbench and click the ⚡ lightning icon. You should see 5 students. Re-run it any time to reset.

**Give Java your password** (never type it into the code). In each new terminal window:

```powershell
# PowerShell
$env:DB_PASSWORD = "your_mysql_root_password"
```
```cmd
:: Command Prompt
set DB_PASSWORD=your_mysql_root_password
chcp 65001
```
```bash
# Mac / Linux
export DB_PASSWORD='your_mysql_root_password'
```

In IntelliJ: *Run → Edit Configurations → Environment variables → `DB_PASSWORD=...`*

**Dry-run checklist**

- [ ] `cd jdbc` → `run J01_ConnectionDemo` prints `Database connected successfully!`
- [ ] `cd regex` → `java R01_PhoneValidation.java` prints VALID / invalid lines
- [ ] Terminal and editor font at 18+ for the projector
- [ ] Re-run `sql/01_setup.sql` just before class so every output matches this guide

---

## 3. Opening: three jobs behind every form (5 min)

**Say:** "Every one of you has filled an admission form online. When you click Submit, where does your phone number go — and who checks it's 10 digits?"

Write three words on the board:

| Job | Tool | One line |
|---|---|---|
| **Store** | MySQL | keeps the data after the program stops |
| **Connect** | JDBC | lets Java send SQL and read the rows back |
| **Check** | Regex | tests whether text has the right shape |

**Ask (answer at the end):** "If two students register with the same email, who should stop it — the Java code or the database?" *Answer: both.*

---

## 4. Module 1 — MySQL basics (20 min)

### The idea

- **Why:** a Java variable vanishes when the program stops. Registration data must survive restarts and be shared by many users at once.
- **What for:** store data permanently, find it quickly, and enforce rules such as "no duplicate email".
- **How it works:** MySQL is a server program running in the background on port 3306. You send it a sentence in SQL ("give me students older than 20"); it finds the rows and sends back a table.
- **Definition:** MySQL is a relational database management system (RDBMS): data lives in tables of rows and columns, and you talk to it in SQL.

**Analogy — the attendance register**

| Register | Database word |
|---|---|
| the register book | database (`college_db`) |
| one page | table (`students`) |
| one line | row (a record) |
| a column heading | column (a field) |
| the roll number | primary key (`id`) — unique, never empty |

### The table we use all lecture

```sql
CREATE TABLE students (
    id          INT          PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    phone       CHAR(10)     UNIQUE,
    age         INT,
    department  VARCHAR(10)  NOT NULL DEFAULT 'CSE'
);
```

After `01_setup.sql` it holds:

| id | name | email | age | department |
|---|---|---|---|---|
| 1 | Arun Kumar | arun@example.com | 20 | CSE |
| 2 | Priya S | priya@example.com | 21 | ECE |
| 3 | Karthik R | karthik@example.com | 22 | CSE |
| 4 | Divya M | divya@example.com | 19 | IT |
| 5 | Meena L | meena@example.com | NULL | MECH |

### Live demo — run one line at a time (Ctrl+Enter in Workbench)

Ask students to **predict** each result before you run it.

```sql
USE college_db;

SELECT * FROM students;                                        -- 5 rows
SELECT name, age FROM students WHERE age > 20;                 -- Priya 21, Karthik 22
SELECT name FROM students WHERE name LIKE 'K%';                -- Karthik R
SELECT name FROM students WHERE age IS NULL;                   -- Meena L
SELECT name, age FROM students ORDER BY age DESC LIMIT 2;      -- Karthik 22, Priya 21
SELECT COUNT(*), COUNT(age) FROM students;                     -- 5, 4

INSERT INTO students (name, email, age) VALUES ('Nila K', 'nila@example.com', 20);
UPDATE students SET age = 21 WHERE id = 1;
DELETE FROM students WHERE email = 'nila@example.com';

-- This one FAILS on purpose:
INSERT INTO students (name, email) VALUES ('Copycat', 'arun@example.com');
-- Error 1062: Duplicate entry 'arun@example.com' for key 'students.email'
```

### Key points

- `WHERE` picks rows. Without it, `UPDATE` and `DELETE` change **every** row.
- `NULL` means "unknown", not zero. Test it with `IS NULL`; `age = NULL` never matches.
- `COUNT(*)` counts rows (5); `COUNT(age)` skips NULLs (4).
- `LIKE 'K%'`: `%` = any number of characters, `_` = exactly one.
- Constraints (`UNIQUE`, `NOT NULL`) let the database refuse bad data by itself.

### Real-world use

Every WordPress website keeps its users and posts in MySQL tables, and Meta runs one of the largest MySQL deployments in the world.

### Advantages and limitations

- **Advantages:** data survives restarts · many users at once · rules enforced in one place · fast searches.
- **Limitations:** the structure is fixed (changing it needs `ALTER TABLE`) · not ideal for unstructured data like images or chat logs.

### Common misconceptions

- "SQL and MySQL are the same thing" — SQL is the language; MySQL is one database that speaks it.
- "DELETE removes the table" — it removes rows; `DROP TABLE` removes the table.

**Check question:** Why does `WHERE age = NULL` return nothing? *(NULL never equals anything — use `IS NULL`.)*

**Bridge:** "We typed SQL by hand. A real app can't ask a human to type SQL. How does Java do it?"

---

## 5. Module 2 — JDBC: Java talks to MySQL (30 min)

### The idea

- **Why:** in Module 1 a human typed the SQL. A registration app must run that SQL by itself, from code.
- **What for:** connect to the database, send SQL, read rows into Java variables, handle errors.
- **How it works:** your code talks only to JDBC **interfaces** (`Connection`, `PreparedStatement`, `ResultSet`). The driver jar (MySQL Connector/J) contains the real classes behind them and speaks MySQL's network language. Change the jar and the URL, and the same Java code talks to another database.
- **Definition:** JDBC (Java Database Connectivity) is the standard Java API in the `java.sql` package for connecting to relational databases, running SQL and reading results.

```text
Your Java code
     │  method calls
     ▼
JDBC API (java.sql)            ← interfaces, ships with the JDK
     │  implemented by
     ▼
MySQL Connector/J (jar)        ← the only MySQL-specific part
     │  SQL out, rows back (port 3306)
     ▼
MySQL Server  →  college_db
```

**Analogy — a phone call:** `DriverManager` dials · `Connection` is the call · `PreparedStatement` is what you say · `ResultSet` is the reply.

### How to run the demos

```cmd
cd jdbc
run J01_ConnectionDemo              (Windows)
./run.sh J01_ConnectionDemo         (Mac/Linux — first time: chmod +x run.sh)
```

`run` compiles every file in the folder (all demos share a small `DB.java` helper that opens the connection) and starts the class you name.

> **Re-run `sql/01_setup.sql` now** — Module 1 changed Arun's age. Then run the demos **in the order below**; the outputs shown depend on it.

### Demo 1 — connect (J01)

```java
String url = "jdbc:mysql://localhost:3306/college_db";
String user = "root";
String password = System.getenv("DB_PASSWORD");      // never hard-code it

try (Connection con = DriverManager.getConnection(url, user, password)) {
    System.out.println("Database connected successfully!");
} catch (SQLException e) {
    System.out.println("Connection failed: " + e.getMessage());
}
```

Output:
```text
Database connected successfully!
Connected to : MySQL 8.x.x   (your server version)
Is it open?  : true
```

Point out:
- URL = `jdbc:mysql://` (which driver) + `localhost` (this machine) + `:3306` (MySQL's port) + `/college_db` (the database).
- `try ( … )` is **try-with-resources**: the connection closes automatically, even if an error happens.

### Demo 2 — INSERT with PreparedStatement (J03)

```java
String sql = "INSERT INTO students (name, email, phone, age, department) VALUES (?, ?, ?, ?, ?)";

try (Connection con = DB.getConnection();
     PreparedStatement ps = con.prepareStatement(sql)) {
    ps.setString(1, "Rahul V");             // 1st ?  (indexes start at 1, not 0)
    ps.setString(2, "rahul@example.com");
    ps.setString(3, "9871234560");
    ps.setInt(4, 23);
    ps.setString(5, "EEE");
    int rows = ps.executeUpdate();          // INSERT / UPDATE / DELETE → rows changed
    System.out.println("Rows inserted: " + rows);
}
```

Output: `Rows inserted: 1`

**Run it a second time on purpose:** `Insert failed: Duplicate entry 'rahul@example.com' for key 'students.email'` — the UNIQUE rule from Module 1 at work.

### Demo 3 — SELECT (J04)

```java
try (Connection con = DB.getConnection();
     Statement st = con.createStatement();
     ResultSet rs = st.executeQuery("SELECT id, name, email, age, department FROM students ORDER BY id")) {
    while (rs.next()) {                     // cursor starts BEFORE row 1
        System.out.println(rs.getInt("id") + " " + rs.getString("name"));
    }
}
```

Output (the file prints all five columns as a table; trimmed here):
```text
ID  NAME        EMAIL                  AGE  DEPT
1   Arun Kumar  arun@example.com       20   CSE
...
5   Meena L     meena@example.com      null MECH
6   Rahul V     rahul@example.com      23   EEE
6 student(s) found.
```

### Demo 4 — UPDATE and DELETE (J06, J07)

```text
J06:  Rows updated (id 1):  1
      Rows updated (id 99): 0      ← 0 means the WHERE matched nothing
J07:  Rows deleted: 1              ← Rahul removed by email
```

### Demo 5 — SQL injection (J08) — the most important demo

Run it **before** explaining. The "user" typed `x' OR '1'='1` into a search box.

```text
UNSAFE SQL sent to MySQL:
  SELECT id, name FROM students WHERE email = 'x' OR '1'='1'
Rows returned: 5   <-- every student leaked!

SAFE SQL sent to MySQL:
  SELECT id, name FROM students WHERE email = ?    with value [x' OR '1'='1]
Rows returned: 0   <-- no student has that literal email
```

**Say:** "When we glue user text into SQL with `+`, the user can write SQL. `'1'='1'` is true for every row. A `PreparedStatement` is like a printed exam form — the student writes only inside the boxes and can't change the questions."

> **The one rule:** user input goes into `?` — never into the SQL string with `+`.

### Key points

- `executeQuery()` → SELECT → returns a `ResultSet`.
- `executeUpdate()` → INSERT / UPDATE / DELETE → returns the number of rows changed.
- Parameter and column indexes start at **1**.
- Call `rs.next()` before reading a row.
- Close everything with try-with-resources.

### Real-world use

Spring Boot, Hibernate and every Java framework still call JDBC underneath. When a Spring app logs `HikariPool-1 - Starting...`, that pool is handing out JDBC `Connection` objects.

### Advantages and limitations

- **Advantages:** standard for every database · full control of the SQL · nothing extra to learn.
- **Limitations:** repetitive code (open, set, execute, loop, close) · you map rows to objects by hand. ORMs like Hibernate reduce this — but they still run on JDBC.

### Common misconceptions

- "JDBC is a database" — it's an API; MySQL is the database; Connector/J is the driver.
- "`Class.forName(...)` is required" — not since Java 6; drivers load themselves.
- "Regex validation stops SQL injection" — it doesn't; `PreparedStatement` does.

**Check question:** `executeUpdate()` returned 0. What happened? *(The WHERE clause matched no row.)*

**Bridge:** "The database protects the data. But we'd like to tell the user nicely that `98765` isn't a phone number — before we even reach the database."

---

## 6. Module 3 — Regex in Java (25 min)

### The idea

- **Why:** checking "10 digits, first digit 6–9" by hand takes a loop, `Character.isDigit`, a length check and an `if` — about 8 lines. A regex does it in one.
- **What for:** validate input (phone, email, ID), find and pull out parts of text (all numbers), clean text (extra spaces), split text on several separators.
- **How it works:** the regex engine slides along your text holding the pattern like a **stencil**, asking at each position "does the pattern fit here?"
- **Definition:** a regular expression is a pattern that describes what text should look like. Java runs it with `java.util.regex.Pattern` and `Matcher`, and with shortcuts on `String` (`matches`, `replaceAll`, `split`).

### Run the demos

No database needed — Java 11+ runs a single source file directly:

```cmd
cd regex
java R01_PhoneValidation.java
```

### Demo 1 — validate a phone number (R01)

```java
String regex = "[6-9][0-9]{9}";
System.out.println("9876543210".matches(regex));
```

| Piece | Means |
|---|---|
| `[6-9]` | one digit from 6 to 9 |
| `[0-9]` | any one digit |
| `{9}` | exactly 9 more of them → 10 digits total |

Output:
```text
9876543210      -> VALID
6123456789      -> VALID
5876543210      -> invalid
987654321       -> invalid
98765432101     -> invalid
98765 43210     -> invalid
+919876543210   -> invalid
```

### The symbols to teach today

| Which character? | How many? | Where? |
|---|---|---|
| `[abc]` one of these | `*` zero or more | `^` start |
| `[a-z]` a range | `+` one or more | `$` end |
| `[^0-9]` anything but a digit | `?` zero or one | |
| `\d` a digit · `\s` whitespace | `{n}` exactly n · `{n,m}` n to m | |
| `\w` letter, digit or `_` · `.` any character | | |

### Two layers of backslashes — explain this slowly

| You want to match | Regex | In Java you type |
|---|---|---|
| a digit | `\d` | `"\\d"` |
| a real dot | `\.` | `"\\."` |

Java reads the string first and turns `\\` into one `\`; the regex engine then receives `\d`. Writing `"\d"` doesn't even compile.

### Demo 2 — `matches()` vs `find()` (R13)

Cover the true/false cells and let students predict.

| Input · pattern `\d{10}` | `matches()` | `lookingAt()` | `find()` |
|---|---|---|---|
| `9876543210` | true | true | true |
| `9876543210 is my number` | false | true | true |
| `My number is 9876543210` | false | false | true |

- `matches()` — the **whole** input must fit → use it to **validate**.
- `find()` — the pattern may be **anywhere** → loop it to **extract**.

### Demo 3 — email-like check (R02)

```java
Pattern EMAIL = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
EMAIL.matcher("kk@example.com").matches();      // true
```

`kk@example.com`, `karthika.krishna@annauniv.edu.in` and `student+java@gmail.com` look valid; `kk@example`, `kk.example.com`, `@example.com`, `kk@@example.com` don't.
Say clearly: this checks the **shape** only — the real proof that an email exists is sending a verification mail.

### Demo 4 — extract, replace, split (R05, R07, R08)

```java
// R05 — find every number
Matcher m = Pattern.compile("\\d+(\\.\\d+)?")
        .matcher("Java 17 and Java 21 are LTS. Python 3.12 is out. Our CGPA target is 8.75!");
while (m.find()) System.out.println(m.group());       // 17, 21, 3.12, 8.75

// R07 — clean extra spaces
"   Karthika    Krishna\t\tM   ".replaceAll("\\s+", " ").strip();   // "Karthika Krishna M"

// R08 — split on many separators
"Java,Python; C++ |SQL,,  MongoDB".split("[,;|\\s]+");   // [Java, Python, C++, SQL, MongoDB]
```

**Trap to show (R07, R08):** `replaceAll` and `split` treat their first argument as a regex.
`"1.2.3".replaceAll(".", "-")` gives `-----` because `.` means *any character*. Use `"\\."` — or `replace(".", "-")`, which is not regex.

### Real-world use

Sign-up forms check phone and email format before calling the server; bank sites check PAN format (`[A-Z]{5}[0-9]{4}[A-Z]`); IDE Find/Replace and the `grep` command are regex engines. This kit even uses a regex inside MySQL: the `chk_phone` constraint is `phone REGEXP '^[0-9]{10}$'`.

### Advantages and limitations

- **Advantages:** very short code · one rule reused everywhere · the same syntax works in Java, JavaScript, Python and SQL.
- **Limitations:** hard to read when long · checks shape, not meaning (`2026-02-30` passes a date pattern; use `java.time` for real dates) · not for parsing HTML or JSON.

### Common misconceptions

- "`matches()` searches inside the string" — it tests the whole string.
- "A regex can fully validate an email" — it can only check the shape.
- "`split(\".\")` splits on dots" — it splits on every character and returns an empty array.

**Check question:** Why do we write `"\\d"` in Java but `\d` in the regex? *(Java removes one layer of backslashes first.)*

---

## 7. Wrap-up: mini project, quiz, Q&A (10 min)

### Mini project demo (5 min) — `project/v1-single-file`

```cmd
cd project\v1-single-file
java -cp "..\..\lib\*" StudentRegistrationApp.java
```

Re-run `sql/01_setup.sql` first. Then show three registrations (choose **1** in the menu):

| You type (name · email · phone · age · dept) | App replies | Point to make |
|---|---|---|
| `Rahul   V` · `RAHUL@example.com` · `9871234560` · `23` · `eee` | `[OK] Registered! Student ID = 6` | spaces cleaned, email lower-cased, regex passed, saved with `PreparedStatement` |
| `r4hul` · `bad-email` · `12345` · `99` · `xyz` | five rule messages | **regex** caught honest mistakes |
| `Test Dup` · `arun@example.com` · `9123123123` · *(blank)* · `CSE` | `[X] That email is already registered.` | regex passed — the **database's UNIQUE** rule caught it |

Answer the opening question: *who stops a duplicate email? Both — Java checks the shape, the database guarantees uniqueness.*

For a cleaner design, show `project/v2-multi-class` as homework: the same app split into `Main`, `Student`, `StudentValidator`, `StudentDAO` and `DBConnection`.

### Quick quiz (3 min) — hands up

| # | Question | Answer |
|---|---|---|
| 1 | What does `WHERE age = NULL` return? | Nothing — use `IS NULL` |
| 2 | `executeUpdate()` returned 0. Why? | The WHERE matched no row |
| 3 | Which stops SQL injection: regex or `PreparedStatement`? | `PreparedStatement` |
| 4 | `"My no 9876543210".matches("\\d{10}")` → ? | `false` — use `find()` |
| 5 | What does `"1.2.3".replaceAll(".", "-")` give? | `-----` |

### Close (2 min)

> **MySQL** stores the data. **SQL** describes what to do with it. **JDBC** carries SQL from Java — values through `?`. **Regex** checks the text has the right shape. **Java** coordinates all of it.

**Homework:** add a "search by department" option to the project using `WHERE department = ?`.

---

## 8. Cheat sheet

**SQL**

| Task | Command |
|---|---|
| Read | `SELECT name FROM students WHERE age > 20 ORDER BY name;` |
| Add | `INSERT INTO students (name, email) VALUES ('Nila K', 'nila@example.com');` |
| Change | `UPDATE students SET age = 21 WHERE id = 1;` |
| Remove | `DELETE FROM students WHERE id = 5;` |
| Pattern search | `WHERE name LIKE 'K%'` |
| Unknown values | `WHERE age IS NULL` |

**JDBC skeleton to remember**

```java
String sql = "SELECT id, name FROM students WHERE department = ?";
try (Connection con = DriverManager.getConnection(url, user, password);
     PreparedStatement ps = con.prepareStatement(sql)) {
    ps.setString(1, "CSE");
    try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " " + rs.getString("name"));
        }
    }
} catch (SQLException e) {
    System.out.println("Error: " + e.getMessage());
}
```

**Regex methods**

| Method | Regex? | Does |
|---|---|---|
| `s.matches(regex)` | yes | whole string fits? |
| `Pattern.compile(regex).matcher(s).find()` | yes | pattern anywhere? (loop for all) |
| `s.replaceAll(regex, x)` | yes | replace every match |
| `s.split(regex)` | yes | cut at every match |
| `s.replace(a, b)` | **no** | plain text replace |

---

## 9. Troubleshooting

| You see | Usually means | Fix |
|---|---|---|
| `No suitable driver found for jdbc:mysql://...` | jar not on the classpath | jar in `lib/`? `-cp "...lib\*"` with the quotes? |
| `Access denied for user 'root'@'localhost' (using password: NO)` | `DB_PASSWORD` not set in **this** terminal | set it again — each new window needs it |
| `Access denied ... (using password: YES)` | wrong password | check it by logging into Workbench |
| `Unknown database 'college_db'` | setup script not run | run `sql/01_setup.sql` |
| `Communications link failure` | MySQL service stopped | Windows: Services → MySQL84 → Start |
| `Public Key Retrieval is not allowed` | you added `useSSL=false` | remove `useSSL=false` |
| `Duplicate entry ... for key 'students.email'` | ran an insert demo twice | expected! re-run `01_setup.sql` to reset |
| `₹` or Tamil text shows as `?` | Windows console code page | run `chcp 65001`, or use IntelliJ's console |
| error near `"""` | Java older than 15 | install JDK 17 or 21 |

---

## 10. What's in the kit

```
jdbc-regex-seminar/
├── README.md                    ← this guide
├── lib/                         ← put mysql-connector-j-9.x.x.jar here
├── sql/
│   ├── 01_setup.sql             ← creates / resets college_db
│   ├── 02_mysql_practice.sql    ← all MySQL demo queries
│   └── 03_create_app_user.sql   ← optional least-privilege login
├── jdbc/                        ← J01–J17 + DB.java + run.bat / run.sh
├── regex/                       ← R01–R19 (no database needed)
└── project/
    ├── v1-single-file/          ← Student Registration System, one file
    └── v2-multi-class/src/      ← same app: Main, Student, StudentDAO, StudentValidator, DBConnection
```

**Used in this 90-minute lecture:** J01, J03, J04, J06, J07, J08 · R01, R02, R05, R07, R08, R13 · project v1.
**For a longer session or self-study:** J09–J17 (transactions, batch, stored procedures, metadata, NULL handling, errors) and R09–R19 (groups, greedy vs reluctant, lookarounds, flags, escaping, ReDoS).

---

## 11. Videos to watch

| Topic | Video | Channel |
|---|---|---|
| SQL / MySQL | [SQL Course for Beginners](https://www.youtube.com/watch?v=7S_tz1z_5bA) | Programming with Mosh |
| JDBC | [JDBC Tutorial](https://www.youtube.com/watch?v=txFAuhHELiE) | Telusko |
| SQL injection | [Hacking Websites with SQL Injection](https://www.youtube.com/watch?v=_jKylhJtPmI) | Computerphile |
| Regex | [Learn Regular Expressions In 20 Minutes](https://www.youtube.com/watch?v=rhzKDrUiJVk) | Web Dev Simplified (JavaScript, same syntax as Java) |

Official references: [MySQL 8.4 Reference Manual](https://dev.mysql.com/doc/refman/8.4/en/) · [Connector/J documentation](https://dev.mysql.com/doc/connector-j/en/) · [Oracle JDBC Basics](https://docs.oracle.com/javase/tutorial/jdbc/basics/) · [java.util.regex.Pattern (Java 21)](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html)
