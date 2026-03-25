package manager;

import model.Complaint;
import model.ComplaintStatus;
import model.Student;
import model.Staff;
import model.User;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ComplaintManager {

    private static final String COMPLAINTS_FILE = "data/complaints.txt";
    private static final String USERS_FILE = "data/users.txt";

    private List<Complaint> complaints;
    private List<User> users;
    private int complaintCounter;

    public ComplaintManager() {
        complaints = new ArrayList<>();
        users = new ArrayList<>();
        complaintCounter = 1;
        loadUsers();
        loadComplaints();
    }

    private void loadUsers() {
        File f = new File(USERS_FILE);
        if (!f.exists()) {
            seedDefaultUsers();
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(":", -1);
                if (parts[0].equals("STUDENT") && parts.length >= 6) {
                    users.add(new Student(parts[1], parts[2], parts[3], parts[4], parts[5]));
                } else if (parts[0].equals("STAFF") && parts.length >= 5) {
                    users.add(new Staff(parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (IOException e) {
            seedDefaultUsers();
        }
    }

    private void seedDefaultUsers() {
        users.add(new Student("S001", "Kishan Bishwakarma", "pass123", "101", "Block A"));
        users.add(new Student("S002", "Priya Sharma", "pass123", "204", "Block B"));
        users.add(new Student("S003", "Rahul Verma", "pass123", "312", "Block C"));
        users.add(new Staff("ST001", "Warden Kumar", "admin123", "Maintenance"));
        users.add(new Staff("ST002", "Ms. Patel", "admin123", "Electrical"));
        saveUsers();
    }

    private void saveUsers() {
        new File("data").mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User u : users) pw.println(u.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadComplaints() {
        File f = new File(COMPLAINTS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                Complaint c = Complaint.fromFileString(line);
                if (c != null) {
                    complaints.add(c);
                    String id = c.getComplaintId().replace("CMP", "");
                    try {
                        int num = Integer.parseInt(id);
                        if (num >= complaintCounter) complaintCounter = num + 1;
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveComplaints() {
        new File("data").mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(COMPLAINTS_FILE))) {
            for (Complaint c : complaints) pw.println(c.toFileString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User login(String userId, String password) {
        return users.stream()
                .filter(u -> u.getUserId().equals(userId) && u.authenticate(password))
                .findFirst().orElse(null);
    }

    public String submitComplaint(String studentId, String category, String description) {
        User user = users.stream().filter(u -> u.getUserId().equals(studentId)).findFirst().orElse(null);
        if (!(user instanceof Student)) return null;
        Student s = (Student) user;
        String id = String.format("CMP%03d", complaintCounter++);
        Complaint c = new Complaint(id, studentId, s.getName(),
                                     s.getRoomNumber(), s.getHostelBlock(),
                                     category, description);
        complaints.add(c);
        saveComplaints();
        return id;
    }

    public List<Complaint> getComplaintsByStudent(String studentId) {
        return complaints.stream()
                .filter(c -> c.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public List<Complaint> getAllComplaints() {
        return new ArrayList<>(complaints);
    }

    public List<Complaint> getComplaintsByStatus(ComplaintStatus status) {
        return complaints.stream()
                .filter(c -> c.getStatus() == status)
                .collect(Collectors.toList());
    }

    public boolean updateComplaintStatus(String complaintId, ComplaintStatus newStatus, String remark) {
        Optional<Complaint> opt = complaints.stream()
                .filter(c -> c.getComplaintId().equals(complaintId))
                .findFirst();
        if (opt.isEmpty()) return false;
        Complaint c = opt.get();
        c.setStatus(newStatus);
        c.setStaffRemark(remark);
        saveComplaints();
        return true;
    }

    public Map<ComplaintStatus, Long> getStatusSummary() {
        Map<ComplaintStatus, Long> map = new LinkedHashMap<>();
        for (ComplaintStatus s : ComplaintStatus.values()) {
            map.put(s, complaints.stream().filter(c -> c.getStatus() == s).count());
        }
        return map;
    }

    public boolean changePassword(String userId, String newPassword) {
        Optional<User> opt = users.stream().filter(u -> u.getUserId().equals(userId)).findFirst();
        if (opt.isEmpty()) return false;
        User u = opt.get();
        if (u instanceof Student s) {
            users.remove(u);
            users.add(new Student(s.getUserId(), s.getName(), newPassword, s.getRoomNumber(), s.getHostelBlock()));
        } else if (u instanceof Staff st) {
            users.remove(u);
            users.add(new Staff(st.getUserId(), st.getName(), newPassword, st.getDepartment()));
        }
        saveUsers();
        return true;
    }

    public List<Complaint> searchComplaints(String query) {
        String q = query.toLowerCase().trim();
        if (q.isEmpty()) return getAllComplaints();
        return complaints.stream()
                .filter(c -> c.getComplaintId().toLowerCase().contains(q)
                        || c.getStudentName().toLowerCase().contains(q)
                        || c.getCategory().toLowerCase().contains(q)
                        || c.getDescription().toLowerCase().contains(q)
                        || c.getRoomNumber().toLowerCase().contains(q)
                        || c.getHostelBlock().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Complaint> searchComplaintsByStudent(String studentId, String query) {
        String q = query.toLowerCase().trim();
        return complaints.stream()
                .filter(c -> c.getStudentId().equals(studentId))
                .filter(c -> q.isEmpty()
                        || c.getCategory().toLowerCase().contains(q)
                        || c.getDescription().toLowerCase().contains(q)
                        || c.getComplaintId().toLowerCase().contains(q)
                        || c.getStatus().getDisplayName().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<String> getCategories() {
        return Arrays.asList(
            "Plumbing", "Electrical", "Furniture", "Cleanliness",
            "Internet/WiFi", "Water Supply", "Pest Control", "Other"
        );
    }
}
