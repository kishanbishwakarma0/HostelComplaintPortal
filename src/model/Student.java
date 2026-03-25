package model;

public class Student extends User {
    private static final long serialVersionUID = 1L;

    private String roomNumber;
    private String hostelBlock;

    public Student(String studentId, String name, String password, String roomNumber, String hostelBlock) {
        super(studentId, name, password, "STUDENT");
        this.roomNumber = roomNumber;
        this.hostelBlock = hostelBlock;
    }

    public String getRoomNumber() { return roomNumber; }
    public String getHostelBlock() { return hostelBlock; }

    @Override
    public String getDashboardTitle() {
        return "Student Portal — " + getName();
    }

    @Override
    public String toString() {
        return super.toString() + ":" + roomNumber + ":" + hostelBlock;
    }
}
