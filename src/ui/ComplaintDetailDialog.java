package ui;

import model.Complaint;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ComplaintDetailDialog extends JDialog {

    private static final Color CARD   = new Color(30, 41, 59);
    private static final Color BG     = new Color(15, 23, 42);
    private static final Color TEXT   = new Color(226, 232, 240);
    private static final Color MUTED  = new Color(100, 116, 139);
    private static final Color BORDER = new Color(51, 65, 85);
    private static final Color SUCCESS = new Color(34, 197, 94);
    private static final Color WARNING = new Color(234, 179, 8);
    private static final Color DANGER  = new Color(239, 68, 68);

    public ComplaintDetailDialog(JFrame parent, Complaint c) {
        super(parent, "Complaint Details — " + c.getComplaintId(), true);
        setSize(500, 420);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setBackground(CARD);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(24, 28, 24, 28));

        panel.add(headerRow(c));
        panel.add(Box.createVerticalStrut(16));
        panel.add(separator());
        panel.add(Box.createVerticalStrut(14));

        panel.add(field("Student", c.getStudentName() + "  (" + c.getStudentId() + ")"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(field("Room / Block", "Room " + c.getRoomNumber() + " · " + c.getHostelBlock()));
        panel.add(Box.createVerticalStrut(10));
        panel.add(field("Category", c.getCategory()));
        panel.add(Box.createVerticalStrut(10));
        panel.add(field("Submitted", c.getSubmittedAt()));
        panel.add(Box.createVerticalStrut(10));
        panel.add(field("Last Updated", c.getUpdatedAt()));
        panel.add(Box.createVerticalStrut(14));

        panel.add(separator());
        panel.add(Box.createVerticalStrut(12));

        JLabel descTitle = new JLabel("Description");
        descTitle.setFont(new Font("SansSerif", Font.BOLD, 11));
        descTitle.setForeground(MUTED);
        descTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(descTitle);
        panel.add(Box.createVerticalStrut(6));

        JTextArea desc = new JTextArea(c.getDescription());
        desc.setBackground(BG);
        desc.setForeground(TEXT);
        desc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);
        desc.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER), new EmptyBorder(8, 10, 8, 10)));
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        desc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panel.add(desc);

        if (!c.getStaffRemark().isEmpty()) {
            panel.add(Box.createVerticalStrut(12));
            panel.add(field("Staff Remark", c.getStaffRemark()));
        }

        setContentPane(panel);
    }

    private JPanel headerRow(Complaint c) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(CARD);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JLabel id = new JLabel(c.getComplaintId());
        id.setFont(new Font("Georgia", Font.BOLD, 18));
        id.setForeground(TEXT);

        Color statusColor = switch (c.getStatus()) {
            case RESOLVED -> SUCCESS;
            case IN_PROGRESS -> WARNING;
            default -> DANGER;
        };
        JLabel badge = new JLabel("  " + c.getStatus().getDisplayName() + "  ");
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setForeground(statusColor);
        badge.setBorder(BorderFactory.createLineBorder(statusColor));
        row.add(id, BorderLayout.WEST);
        row.add(badge, BorderLayout.EAST);
        return row;
    }

    private JSeparator separator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    private JPanel field(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(CARD);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(MUTED);
        lbl.setPreferredSize(new Dimension(110, 20));

        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.PLAIN, 13));
        val.setForeground(TEXT);

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        return row;
    }
}
