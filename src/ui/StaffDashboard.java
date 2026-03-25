package ui;

import manager.ComplaintManager;
import model.Complaint;
import model.ComplaintStatus;
import model.Staff;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class StaffDashboard extends JFrame {

    private Staff staff;
    private ComplaintManager manager;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel pendingCount, inProgressCount, resolvedCount;
    private JComboBox<String> filterBox;
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

    public StaffDashboard(Staff staff, ComplaintManager manager) {
        this.staff = staff;
        this.manager = manager;
        setTitle("Staff Portal — " + staff.getName());
        setSize(1100, 680);
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
        JLabel roleTag = new JLabel("  STAFF");
        roleTag.setFont(new Font("SansSerif", Font.BOLD, 10));
        roleTag.setForeground(ACCENT);
        side.add(roleTag);
        side.add(Box.createVerticalStrut(20));

        JPanel infoCard = new JPanel();
        infoCard.setBackground(BG);
        infoCard.setLayout(new BoxLayout(infoCard, BoxLayout.Y_AXIS));
        infoCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER), new EmptyBorder(12, 14, 12, 14)));
        infoCard.setMaximumSize(new Dimension(210, 80));
        JLabel nameL = new JLabel(staff.getName());
        nameL.setFont(new Font("SansSerif", Font.BOLD, 13));
        nameL.setForeground(TEXT);
        JLabel deptL = new JLabel(staff.getDepartment() + " Dept.");
        deptL.setFont(new Font("SansSerif", Font.PLAIN, 11));
        deptL.setForeground(MUTED);
        infoCard.add(nameL);
        infoCard.add(Box.createVerticalStrut(4));
        infoCard.add(deptL);

        JPanel iWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        iWrap.setBackground(CARD);
        iWrap.add(infoCard);
        side.add(iWrap);

        side.add(Box.createVerticalStrut(22));

        JLabel statsTitle = new JLabel("  Overview");
        statsTitle.setFont(new Font("SansSerif", Font.BOLD, 11));
        statsTitle.setForeground(MUTED);
        side.add(statsTitle);
        side.add(Box.createVerticalStrut(8));

        pendingCount    = statLabel("—", DANGER);
        inProgressCount = statLabel("—", WARNING);
        resolvedCount   = statLabel("—", SUCCESS);

        side.add(makeStatRow(pendingCount,    "Pending",     DANGER));
        side.add(Box.createVerticalStrut(5));
        side.add(makeStatRow(inProgressCount, "In Progress", WARNING));
        side.add(Box.createVerticalStrut(5));
        side.add(makeStatRow(resolvedCount,   "Resolved",    SUCCESS));

        side.add(Box.createVerticalGlue());

        JPanel btnWrap = new JPanel();
        btnWrap.setBackground(CARD);
        btnWrap.setLayout(new BoxLayout(btnWrap, BoxLayout.Y_AXIS));
        btnWrap.setBorder(new EmptyBorder(0, 10, 14, 10));

        JButton pwdBtn = sideBtn("Change Password");
        pwdBtn.addActionListener(e -> new ChangePasswordDialog(this, manager, staff).setVisible(true));
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

    private JLabel statLabel(String val, Color color) {
        JLabel l = new JLabel(val);
        l.setFont(new Font("SansSerif", Font.BOLD, 18));
        l.setForeground(color);
        return l;
    }

    private JPanel makeStatRow(JLabel countLabel, String label, Color color) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        row.setBackground(CARD);
        row.setMaximumSize(new Dimension(230, 30));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(MUTED);
        row.add(countLabel);
        row.add(lbl);
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

        JLabel title = new JLabel("All Complaints");
        title.setFont(new Font("Georgia", Font.BOLD, 22));
        title.setForeground(TEXT);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        controls.setBackground(BG);

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

        JLabel filterLabel = new JLabel("Status:");
        filterLabel.setForeground(MUTED);
        filterLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        filterBox = new JComboBox<>(new String[]{"All", "Pending", "In Progress", "Resolved"});
        filterBox.setBackground(CARD);
        filterBox.setForeground(TEXT);
        filterBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
        filterBox.addActionListener(e -> refreshTable());

        controls.add(searchField);
        controls.add(filterLabel);
        controls.add(filterBox);

        topBar.add(title, BorderLayout.WEST);
        topBar.add(controls, BorderLayout.EAST);
        main.add(topBar, BorderLayout.NORTH);

        String[] cols = {"ID", "Student", "Room/Block", "Category", "Description", "Status", "Submitted", "Remark"};
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
        table.getColumnModel().getColumn(4).setPreferredWidth(200);
        table.getColumnModel().getColumn(7).setPreferredWidth(150);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                c.setBackground(sel ? new Color(51,65,85) : (row%2==0 ? CARD : new Color(24,33,50)));
                c.setForeground(TEXT);
                if (col == 5 && val != null) {
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

        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionBar.setBackground(BG);

        JButton updateBtn = new JButton("Update Selected");
        styleActionBtn(updateBtn);
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a complaint first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            openUpdateDialog(tableModel.getValueAt(row, 0).toString());
        });

        JButton viewBtn = new JButton("View Details");
        viewBtn.setBackground(new Color(51, 65, 85));
        viewBtn.setForeground(TEXT);
        viewBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        viewBtn.setBorderPainted(false);
        viewBtn.setFocusPainted(false);
        viewBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        viewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) openDetailForRow(row);
        });

        JLabel hint = new JLabel("Double-click a row to view details");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        hint.setForeground(MUTED);

        actionBar.add(updateBtn);
        actionBar.add(viewBtn);
        actionBar.add(Box.createHorizontalStrut(10));
        actionBar.add(hint);
        main.add(actionBar, BorderLayout.SOUTH);

        refreshTable();
        return main;
    }

    private void styleActionBtn(JButton btn) {
        btn.setBackground(ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void openDetailForRow(int row) {
        if (row < 0) return;
        String id = tableModel.getValueAt(row, 0).toString();
        manager.getAllComplaints().stream()
               .filter(c -> c.getComplaintId().equals(id))
               .findFirst()
               .ifPresent(c -> new ComplaintDetailDialog(this, c).setVisible(true));
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        String filter = filterBox != null ? filterBox.getSelectedItem().toString() : "All";
        String query  = searchField != null ? searchField.getText() : "";

        List<Complaint> list = manager.searchComplaints(query);
        list = list.stream().filter(c -> switch (filter) {
            case "Pending"     -> c.getStatus() == ComplaintStatus.PENDING;
            case "In Progress" -> c.getStatus() == ComplaintStatus.IN_PROGRESS;
            case "Resolved"    -> c.getStatus() == ComplaintStatus.RESOLVED;
            default            -> true;
        }).toList();

        for (Complaint c : list) {
            tableModel.addRow(new Object[]{
                c.getComplaintId(), c.getStudentName(),
                c.getRoomNumber() + " / " + c.getHostelBlock(),
                c.getCategory(), c.getDescription(),
                c.getStatus().getDisplayName(), c.getSubmittedAt(),
                c.getStaffRemark().isEmpty() ? "—" : c.getStaffRemark()
            });
        }

        Map<ComplaintStatus, Long> summary = manager.getStatusSummary();
        if (pendingCount    != null) pendingCount.setText(String.valueOf(summary.getOrDefault(ComplaintStatus.PENDING, 0L)));
        if (inProgressCount != null) inProgressCount.setText(String.valueOf(summary.getOrDefault(ComplaintStatus.IN_PROGRESS, 0L)));
        if (resolvedCount   != null) resolvedCount.setText(String.valueOf(summary.getOrDefault(ComplaintStatus.RESOLVED, 0L)));
    }

    private void openUpdateDialog(String complaintId) {
        JDialog dialog = new JDialog(this, "Update — " + complaintId, true);
        dialog.setSize(440, 290);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setBackground(CARD);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(24, 28, 24, 28));

        JLabel title = new JLabel("Update Status");
        title.setFont(new Font("Georgia", Font.BOLD, 18));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(18));

        panel.add(dlgLbl("New Status"));
        panel.add(Box.createVerticalStrut(6));
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Pending", "In Progress", "Resolved"});
        statusBox.setBackground(BG);
        statusBox.setForeground(TEXT);
        statusBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        statusBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        statusBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(statusBox);

        panel.add(Box.createVerticalStrut(14));
        panel.add(dlgLbl("Remark (optional)"));
        panel.add(Box.createVerticalStrut(6));
        JTextField remarkField = new JTextField();
        remarkField.setBackground(BG);
        remarkField.setForeground(TEXT);
        remarkField.setCaretColor(TEXT);
        remarkField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        remarkField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER), new EmptyBorder(8, 10, 8, 10)));
        remarkField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        remarkField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(remarkField);

        panel.add(Box.createVerticalStrut(20));
        JButton saveBtn = new JButton("Save Update");
        saveBtn.setBackground(ACCENT);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        saveBtn.setBorderPainted(false);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.addActionListener(e -> {
            ComplaintStatus newStatus = switch (statusBox.getSelectedItem().toString()) {
                case "In Progress" -> ComplaintStatus.IN_PROGRESS;
                case "Resolved"    -> ComplaintStatus.RESOLVED;
                default            -> ComplaintStatus.PENDING;
            };
            manager.updateComplaintStatus(complaintId, newStatus, remarkField.getText().trim());
            dialog.dispose();
            refreshTable();
        });
        panel.add(saveBtn);
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
