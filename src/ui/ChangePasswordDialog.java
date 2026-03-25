package ui;

import manager.ComplaintManager;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChangePasswordDialog extends JDialog {

    private static final Color CARD = new Color(30, 41, 59);
    private static final Color BG   = new Color(15, 23, 42);
    private static final Color TEXT = new Color(226, 232, 240);
    private static final Color MUTED = new Color(100, 116, 139);
    private static final Color BORDER = new Color(51, 65, 85);
    private static final Color ACCENT = new Color(99, 102, 241);

    public ChangePasswordDialog(JFrame parent, ComplaintManager manager, User user) {
        super(parent, "Change Password", true);
        setSize(400, 280);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setBackground(CARD);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(24, 28, 24, 28));

        JLabel title = new JLabel("Change Password");
        title.setFont(new Font("Georgia", Font.BOLD, 17));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(18));

        panel.add(lbl("Current Password"));
        panel.add(Box.createVerticalStrut(5));
        JPasswordField current = styledPwd();
        panel.add(current);

        panel.add(Box.createVerticalStrut(12));
        panel.add(lbl("New Password"));
        panel.add(Box.createVerticalStrut(5));
        JPasswordField newPwd = styledPwd();
        panel.add(newPwd);

        panel.add(Box.createVerticalStrut(12));
        panel.add(lbl("Confirm New Password"));
        panel.add(Box.createVerticalStrut(5));
        JPasswordField confirm = styledPwd();
        panel.add(confirm);

        panel.add(Box.createVerticalStrut(18));

        JButton save = new JButton("Update Password");
        save.setBackground(ACCENT);
        save.setForeground(Color.WHITE);
        save.setFont(new Font("SansSerif", Font.BOLD, 13));
        save.setBorderPainted(false);
        save.setFocusPainted(false);
        save.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        save.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        save.setAlignmentX(Component.LEFT_ALIGNMENT);
        save.addActionListener(e -> {
            String cur = new String(current.getPassword());
            String np  = new String(newPwd.getPassword());
            String cf  = new String(confirm.getPassword());
            if (!user.authenticate(cur)) {
                msg("Current password is incorrect.", "Error"); return;
            }
            if (np.length() < 6) {
                msg("New password must be at least 6 characters.", "Error"); return;
            }
            if (!np.equals(cf)) {
                msg("Passwords do not match.", "Error"); return;
            }
            manager.changePassword(user.getUserId(), np);
            msg("Password updated successfully.", "Success");
            dispose();
        });
        panel.add(save);
        setContentPane(panel);
    }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JPasswordField styledPwd() {
        JPasswordField f = new JPasswordField();
        f.setBackground(BG);
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER), new EmptyBorder(7, 10, 7, 10)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        return f;
    }

    private void msg(String text, String title) {
        int type = title.equals("Error") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
        JOptionPane.showMessageDialog(this, text, title, type);
    }
}
