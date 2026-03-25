package ui;

import manager.ComplaintManager;
import model.Complaint;
import model.ComplaintStatus;
import model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentDashboard extends JFrame {

    private Student student;
    private ComplaintManager manager;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;

    private static final Color BG      = new Color(15, 23, 42);
    private static final Color CARD    = new Color(30, 41, 59);
    private static final Color ACCENT  = new Color(99, 102, 241);
    private static final Color TEXT    = new Color(226, 232, 240);
    private static final Color MUTED   = new Color(100, 116, 139);
    private static final Color BORDER  = new Color(51, 65, 85);
    private static final Color SUCCESS = new Color(34, 197, 94);
    private static final Color WARNING = new Color(234, 179, 8);
    private static final Color DANGER  = new Color(239, 68, 68);

    public StudentDashboard(Student student, ComplaintManager manager) {
        this.student = student;
        this.manager = manager;
        setTitle("Student Portal — " + student.getName());
        setSize(960, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMain(), BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setBackground(CARD);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setPreferredSize(new Dimension(230, 0));
        side.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER));

        side.add(Box.createVerticalStrut(28));
        JLabel appName = new JLabel("  Hostel Portal");
        appName.setFont(new Font("Georgia", Font.BOLD, 16));
        appName.setForeground(TEXT);
        side.add(appName);
        side.add(Box.createVerticalStrut(4));
        JLabel roleTag = new JLabel("  STUDENT");
        roleTag.setFont(new Font("SansSerif", Font.BOLD, 10));
        roleTag.setForeground(ACCENT);
        side.add(roleTag);
        side.add(Box.createVerticalStrut(20));

        JPanel infoCard = new JPanel();
        infoCard.setBackground(BG);
        infoCard.setLayout(new BoxLayout(infoCard, BoxLayout.Y_AXIS));
        infoCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER), new EmptyBorder(12, 14, 12, 14)));
        infoCard.setMaximumSize(new Dimension(210, 100));
        infoCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameL = new JLabel(student.getName());
        nameL.setFont(new Font("SansSerif", Font.BOLD, 13));
        nameL.setForeground(TEXT);
        JLabel roomL = new JLabel("Room " + student.getRoomNumber() + " - " + student.getHostelBlock());
        roomL.setFont(new Font("SansSerif", Font.PLAIN, 11));
        roomL.setForeground(MUTED);
        JLabel idL = new JLabel("ID: " + student.getUserId());
        idL.setFont(new Font("SansSerif", Font.PLAIN, 11));
        idL.setForeground(MUTED);
        infoCard.add(nameL);
        infoCard.add(Box.createVerticalStrut(4));
        infoCard.add(roomL);
        infoCard.add(Box.createVerticalStrut(2));
        infoCard.add(idL);

        JPanel iWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        iWrap.setBackground(CARD);
        iWrap.add(infoCard);
        side.add(iWrap);

        side.add(Box.createVerticalStrut(22));
        addSideStats(side);

        side.add(Box.createVerticalGlue());

        JPanel btnWrap = new JPanel();
        btnWrap.setBackground(CARD);
        btnWrap.setLayout(new BoxLayout(btnWrap, BoxLayout.Y_AXIS));
        btnWrap.setBorder(new EmptyBorder(0, 10, 14, 10));

        JButton pwdBtn = sideBtn("Change Password");
        pwdBtn.addActionListener(e -> new ChangePasswordDialog(this, manager, student).setVisible(true));
        JButton logoutBtn = sideBtn("Sign Out");
        logoutBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                try {
                    Class<?> c = Class.forName("Main");
                    JFrame f = (JFrame) c.getDeclaredConstructor().newInstance();
                    f.setVisible(true);
                } catch (Exception ex) { ex.printStackTrace(); }
            });
        });
        btnWrap.add(pwdBtn);
        btnWrap.add(Box.createVerticalStrut(2));
        btnWrap.add(logoutBtn);
        side.add(btnWrap);
        return side;
    }

    private void addSideStats(JPanel side) {
        JLabel statsLbl = new JLabel("  My Stats");
        statsLbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        statsLbl.setForeground(MUTED);
        side.add(statsLbl);
        side.add(Box.createVerticalStrut(8));

        List<Complaint> mine = manager.getComplaintsByStudent(student.getUserId());
        long pending    = mine.stream().filter(c -> c.getStatus() == ComplaintStatus.PENDING).count();
        long inProgress = mine.stream().filter(c -> c.getStatus() == ComplaintStatus.IN_PROGRESS).count();
        long resolved   = mine.stream().filter(c -> c.getStatus() == ComplaintStatus.RESOLVED).count();

        side.add(statRow(String.valueOf(pending),    "Pending",     DANGER));
        side.add(Box.createVerticalStrut(4));
        side.add(statRow(String.valueOf(inProgress), "In Progress", WARNING));
        side.add(Box.createVerticalStrut(4));
        side.add(statRow(String.valueOf(resolved),   "Resolved",    SUCCESS));
    }

    private JPanel statRow(String val, String label, Color color) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        row.setBackground(CARD);
        row.setMaximumSize(new Dimension(230, 26));
        JLabel v = new JLabel(val);
        v.setFont(new Font("SansSerif", Font.BOLD, 16));
        v.setForeground(color);
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setForeground(MUTED);
        row.add(v);
        row.add(l);
        return row;
    }

    private JButton sideBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.PLAIN, 12));
        b.setBackground(CARD);
        b.setForeground(MUTED);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        return b;
    }

    private JPanel buildMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(28, 28, 28, 28));

        JPanel topBar = new JPanel(new BorderLayout(12, 0));
        topBar.setBackground(BG);
        topBar.setBorder(new EmptyBorder(0, 0, 16, 0));

        JLabel title = new JLabel("My Complaints");
        title.setFont(new Font("Georgia", Font.BOLD, 22));
        title.setForeground(TEXT);

        JPanel rightBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightBar.setBackground(BG);

        searchField = new JTextField(16);
        searchField.setBackground(CARD);
        searchField.setForeground(TEXT);
        searchField.setCaretColor(TEXT);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER), new EmptyBorder(6, 10, 6, 10)));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { refreshTable(); }
            public void removeUpdate(DocumentEvent e)  { refreshTable(); }
            public void changedUpdate(DocumentEvent e) { refreshTable(); }
        });

        JButton newBtn = new JButton("+ New Complaint");
        newBtn.setBackground(ACCENT);
        newBtn.setForeground(Color.WHITE);
        newBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        newBtn.setBorderPainted(false);
        newBtn.setFocusPainted(false);
        newBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        newBtn.addActionListener(e -> openSubmitDialog());

        rightBar.add(searchField);
        rightBar.add(newBtn);
        topBar.add(title, BorderLayout.WEST);
        topBar.add(rightBar, BorderLayout.EAST);
        main.add(topBar, BorderLayout.NORTH);

        String[] cols = {"ID", "Category", "Room", "Description", "Status", "Submitted", "Remark"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setBackground(CARD);
        table.setForeground(TEXT);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setRowHeight(34);
        table.setGridColor(BORDER);
        table.setSelectionBackground(new Color(51, 65, 85));
        table.setSelectionForeground(TEXT);
        table.getTableHeader().setBackground(BG);
        table.getTableHeader().setForeground(MUTED);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(6).setPreferredWidth(150);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                c.setBackground(sel ? new Color(51,65,85) : (row%2==0 ? CARD : new Color(24,33,50)));
                c.setForeground(TEXT);
                if (col == 4 && val != null) {
                    c.setForeground(switch (val.toString()) {
                        case "Resolved"    -> SUCCESS;
                        case "In Progress" -> WARNING;
                        default            -> DANGER;
                    });
                }
                ((JLabel) c).setBorder(new EmptyBorder(0, 8, 0, 8));
                return c;
            }
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) openDetailForRow(table.rowAtPoint(e.getPoint()));
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(CARD);
        main.add(scroll, BorderLayout.CENTER);

        JLabel hint = new JLabel("Double-click a row to view full details");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        hint.setForeground(MUTED);
        hint.setBorder(new EmptyBorder(8, 0, 0, 0));
        main.add(hint, BorderLayout.SOUTH);

        refreshTable();
        return main;
    }

    private void openDetailForRow(int row) {
        if (row < 0) return;
        String id = tableModel.getValueAt(row, 0).toString();
        manager.getComplaintsByStudent(student.getUserId()).stream()
               .filter(c -> c.getComplaintId().equals(id))
               .findFirst()
               .ifPresent(c -> new ComplaintDetailDialog(this, c).setVisible(true));
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        String q = searchField != null ? searchField.getText() : "";
        List<Complaint> list = manager.searchComplaintsByStudent(student.getUserId(), q);
        for (Complaint c : list) {
            tableModel.addRow(new Object[]{
                c.getComplaintId(), c.getCategory(),
                c.getRoomNumber() + "/" + c.getHostelBlock(),
                c.getDescription(), c.getStatus().getDisplayName(),
                c.getSubmittedAt(),
                c.getStaffRemark().isEmpty() ? "—" : c.getStaffRemark()
            });
        }
    }

    private void openSubmitDialog() {
        JDialog dialog = new JDialog(this, "Submit New Complaint", true);
        dialog.setSize(480, 390);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setBackground(CARD);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(24, 28, 24, 28));

        JLabel title = new JLabel("New Complaint");
        title.setFont(new Font("Georgia", Font.BOLD, 18));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(18));

        panel.add(dlgLbl("Category"));
        panel.add(Box.createVerticalStrut(6));
        JComboBox<String> categoryBox = new JComboBox<>(manager.getCategories().toArray(new String[0]));
        categoryBox.setBackground(BG);
        categoryBox.setForeground(TEXT);
        categoryBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        categoryBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        categoryBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(categoryBox);

        panel.add(Box.createVerticalStrut(14));
        panel.add(dlgLbl("Priority"));
        panel.add(Box.createVerticalStrut(6));
        JComboBox<String> priorityBox = new JComboBox<>(new String[]{"Normal", "Urgent"});
        priorityBox.setBackground(BG);
        priorityBox.setForeground(TEXT);
        priorityBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        priorityBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        priorityBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(priorityBox);

        panel.add(Box.createVerticalStrut(14));
        panel.add(dlgLbl("Description"));
        panel.add(Box.createVerticalStrut(6));
        JTextArea descArea = new JTextArea(4, 30);
        descArea.setBackground(BG);
        descArea.setForeground(TEXT);
        descArea.setCaretColor(TEXT);
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER), new EmptyBorder(8, 10, 8, 10)));
        JScrollPane ds = new JScrollPane(descArea);
        ds.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        ds.setAlignmentX(Component.LEFT_ALIGNMENT);
        ds.setBorder(null);
        panel.add(ds);

        panel.add(Box.createVerticalStrut(20));
        JButton submit = new JButton("Submit Complaint");
        submit.setBackground(ACCENT);
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("SansSerif", Font.BOLD, 13));
        submit.setBorderPainted(false);
        submit.setFocusPainted(false);
        submit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        submit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        submit.setAlignmentX(Component.LEFT_ALIGNMENT);
        submit.addActionListener(e -> {
            String desc = descArea.getText().trim();
            if (desc.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a description.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String cat = categoryBox.getSelectedItem().toString();
            String priority = priorityBox.getSelectedItem().toString();
            String fullDesc = priority.equals("Urgent") ? "[URGENT] " + desc : desc;
            String id = manager.submitComplaint(student.getUserId(), cat, fullDesc);
            JOptionPane.showMessageDialog(dialog, "Complaint submitted!\nID: " + id, "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            refreshTable();
        });
        panel.add(submit);
        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private JLabel dlgLbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
}
