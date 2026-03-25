package model;

public class Staff extends User {
    private static final long serialVersionUID = 1L;

    private String department;

    public Staff(String staffId, String name, String password, String department) {
        super(staffId, name, password, "STAFF");
        this.department = department;
    }

    public String getDepartment() { return department; }

    @Override
    public String getDashboardTitle() {
        return "Staff Portal — " + getName();
    }

    @Override
    public String toString() {
        return super.toString() + ":" + department;
    }
}
