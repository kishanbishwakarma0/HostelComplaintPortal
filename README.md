# Hostel Room Complaint Portal

The Java Swing application for hostel management digitizes the complaint system. While staying in the rooms, students can file complaints. The hostel management handles these complaints based on their needs.

---

## Screenshots

### Login
![Login](screenshots/login.png)

### Student Dashboard
![Student Dashboard](screenshots/student.png)

### Submit Complaint
![Submit Complaint](screenshots/complaint%20dashboard.png)

### Complaint History
![Complaint History](screenshots/complaint%20history.png)

### Staff Dashboard
![Staff Dashboard](screenshots/staff.png)

### Update Complaint Status
![Update Status](screenshots/complaint%20status.png)

### Complaint Details
![Complaint Details](screenshots/complaint%20info.png)

---

## Features

### Student side
1. Submit complaints with category, description, and **priority** (Normal / Urgent)
2. Live stats in sidebar: Pending, In Progress, Resolved counts
3. Search complaints by keyword — instant filtering as you type
4. Double-click any row to view full complaint details
5. Change password securely from within the app

### Staff side
1. View all complaints across all students
2. Filter by status (Pending / In Progress / Resolved) combined with live search
3. Update complaint status with an optional remark
4. Double-click or use "View Details" for full complaint info
5. Sidebar overview counters auto-refresh on every update
6. Change password securely

### General
1. Role-based login — students and staff see entirely different views
2. All data persists across sessions in plain-text files in `data/`
3. Auto-seeded default users on first run — no setup required
4. No external libraries — pure Java SE and Swing

---

## Java Concepts Demonstrated

| Concept | Where Applied |
|---|---|
| Abstract class | `User` — base class for `Student` and `Staff` |
| Inheritance | `Student extends User`, `Staff extends User` |
| Polymorphism | `getDashboardTitle()` and `toString()` overridden in both subclasses |
| Enum | `ComplaintStatus` (PENDING, IN_PROGRESS, RESOLVED) |
| Collections | `List<Complaint>`, `List<User>`, `Map<ComplaintStatus, Long>` |
| Streams and lambdas | Filtering, searching, and counting complaints |
| File I/O | `BufferedReader` and `PrintWriter` for persistent text file storage |
| Serializable | `User` and `Complaint` implement `Serializable` |
| GUI (Swing) | Login screen, dual dashboards, dialogs, tables, custom renderers |
| Event handling | `ActionListener`, `DocumentListener`, `MouseAdapter` |

---

## Project Structure

```
HostelComplaintPortal/
├── src/
│   ├── Main.java                        Entry point and login screen
│   ├── model/
│   │   ├── User.java                    Abstract base class
│   │   ├── Student.java                 Extends User; holds room and block
│   │   ├── Staff.java                   Extends User; holds department
│   │   ├── Complaint.java               Complaint model with file encode/decode
│   │   └── ComplaintStatus.java         Enum: PENDING, IN_PROGRESS, RESOLVED
│   ├── manager/
│   │   └── ComplaintManager.java        Data layer: file I/O, login, search
│   └── ui/
│       ├── StudentDashboard.java        Student portal
│       ├── StaffDashboard.java          Staff portal
│       ├── ComplaintDetailDialog.java   Read-only detail popup
│       └── ChangePasswordDialog.java    Password update dialog
├── screenshots/                         App screenshots
├── data/                                Auto-created on first run
│   ├── users.txt                        User accounts
│   └── complaints.txt                   Complaint records
├── run.sh                               Build and run script (Linux/Mac)
└── README.md
```

---

## How to Run

### Requirements
- Java JDK 11 or higher

### Linux / Mac
```bash
chmod +x run.sh
./run.sh
```

### Windows
```cmd
mkdir out
mkdir data
javac -d out -sourcepath src src\Main.java src\model\*.java src\manager\*.java src\ui\*.java
cd out
java Main
```

### IDE (IntelliJ / Eclipse / VS Code)
1. Open the `HostelComplaintPortal/` folder as a project
2. Mark `src/` as the Sources Root
3. Run `Main.java`

---

## Default Login Credentials

| Role | User ID | Password | Details |
|---|---|---|---|
| Student | S001 | pass123 | Kishan Bishwakarma, Room 101, Block A |
| Student | S002 | pass123 | Priya Sharma, Room 204, Block B |
| Student | S003 | pass123 | Rahul Verma, Room 312, Block C |
| Staff | ST001 | admin123 | Warden Kumar, Maintenance |
| Staff | ST002 | admin123 | Ms. Patel, Electrical |

---

## Data Storage Format

### data/users.txt
```
STUDENT:S001:Kishan Bishwakarma:pass123:101:Block A
STAFF:ST001:Warden Kumar:admin123:Maintenance
```

### data/complaints.txt
```
CMP001|S001|Kishan Bishwakarma|101|Block A|Plumbing|Tap is leaking|PENDING|2025-03-25 10:30|2025-03-25 10:30|
```

Both files are plain text. Delete them to reset all data — defaults are re-created on next run.

---

## Author

Built by **Kishan Bishwakarma** as the BYOP capstone for the *Programming in Java* course.
