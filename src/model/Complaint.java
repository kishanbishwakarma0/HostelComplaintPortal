package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Complaint implements Serializable {
    private static final long serialVersionUID = 1L;

    private String complaintId;
    private String studentId;
    private String studentName;
    private String roomNumber;
    private String hostelBlock;
    private String category;
    private String description;
    private ComplaintStatus status;
    private String submittedAt;
    private String updatedAt;
    private String staffRemark;

    public Complaint(String complaintId, String studentId, String studentName,
                     String roomNumber, String hostelBlock,
                     String category, String description) {
        this.complaintId = complaintId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.roomNumber = roomNumber;
        this.hostelBlock = hostelBlock;
        this.category = category;
        this.description = description;
        this.status = ComplaintStatus.PENDING;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.submittedAt = LocalDateTime.now().format(fmt);
        this.updatedAt = this.submittedAt;
        this.staffRemark = "";
    }

    public String getComplaintId() { return complaintId; }
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getRoomNumber() { return roomNumber; }
    public String getHostelBlock() { return hostelBlock; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public ComplaintStatus getStatus() { return status; }
    public String getSubmittedAt() { return submittedAt; }
    public String getUpdatedAt() { return updatedAt; }
    public String getStaffRemark() { return staffRemark; }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.updatedAt = LocalDateTime.now().format(fmt);
    }

    public void setStaffRemark(String remark) { this.staffRemark = remark; }

    public String toFileString() {
        return complaintId + "|" + studentId + "|" + studentName + "|" +
               roomNumber + "|" + hostelBlock + "|" + category + "|" +
               description + "|" + status.name() + "|" + submittedAt + "|" +
               updatedAt + "|" + staffRemark;
    }

    public static Complaint fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 11) return null;
        Complaint c = new Complaint(parts[0], parts[1], parts[2],
                                    parts[3], parts[4], parts[5], parts[6]);
        c.status = ComplaintStatus.valueOf(parts[7]);
        c.submittedAt = parts[8];
        c.updatedAt = parts[9];
        c.staffRemark = parts[10];
        return c;
    }
}
