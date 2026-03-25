import manager.ComplaintManager;
import model.Staff;
import model.Student;
import model.User;
import ui.StaffDashboard;
import ui.StudentDashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {

    private ComplaintManager manager;
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JLabel statusLabel;

    private static final Color BG = new Color(15, 23, 42);
    private static final Color CARD = new Color(30, 41, 59);
    private static final Color ACCENT = new Color(99, 102, 241);
    private static final Color ACCENT_HOVER = new Color(79, 82, 221);
    private static final Color TEXT = new Color(226, 232, 240);
    private static final Color MUTED = new Color(100, 116, 139);
    private static final Color BORDER = new Color(51, 65, 85);
    private static final Color INPUT_BG = new Color(15, 23, 42);

    public Main() {
        manager = new ComplaintManager();
        setTitle("Hostel Complaint Portal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(460, 520);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        JPanel header = new JPanel();
        header.setBackground(BG);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(40, 40, 20, 40));

        JLabel logo = new JLabel("⌂");
        logo.setFont(new Font("Serif", Font.PLAIN, 36));
        logo.setForeground(ACCENT);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Hostel Complaint Portal");
        title.setFont(new Font("Georgia", Font.BOLD, 22));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sign in to continue");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(logo);
        header.add(Box.createVerticalStrut(10));
        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(30, 36, 30, 36)
        ));

        card.add(makeLabel("User ID"));
        card.add(Box.createVerticalStrut(6));
        userIdField = makeTextField("Enter your ID (e.g. S001, ST001)");
        card.add(userIdField);

        card.add(Box.createVerticalStrut(16));
        card.add(makeLabel("Password"));
        card.add(Box.createVerticalStrut(6));
        passwordField = new JPasswordField();
        styleField(passwordField);
        card.add(passwordField);

        card.add(Box.createVerticalStrut(24));

        JButton loginBtn = new JButton("Sign In");
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginBtn.setBackground(ACCENT);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginBtn.setPreferredSize(new Dimension(300, 44));
        loginBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { loginBtn.setBackground(ACCENT_HOVER); }
            public void mouseExited(MouseEvent e) { loginBtn.setBackground(ACCENT); }
        });
        loginBtn.addActionListener(e -> doLogin());
        card.add(loginBtn);

        card.add(Box.createVerticalStrut(16));
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(239, 68, 68));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(statusLabel);

        JPanel hint = new JPanel();
        hint.setBackground(BG);
        hint.setBorder(new EmptyBorder(16, 0, 20, 0));
        JLabel hintLabel = new JLabel("<html><center><span style='color:#64748b'>Students: S001–S003 &nbsp;|&nbsp; Staff: ST001–ST002<br>Default password: pass123 / admin123</span></center></html>");
        hintLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        hint.add(hintLabel);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BG);
        center.setBorder(new EmptyBorder(0, 40, 0, 40));
        center.add(card);

        getRootPane().setDefaultButton(loginBtn);
        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(hint, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextField makeTextField(String placeholder) {
        JTextField f = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    g.setColor(MUTED);
                    g.setFont(getFont().deriveFont(Font.ITALIC));
                    g.drawString(placeholder, 10, getHeight() / 2 + 5);
                }
            }
        };
        styleField(f);
        return f;
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBackground(INPUT_BG);
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void doLogin() {
        String userId = userIdField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (userId.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both ID and password.");
            return;
        }
        User user = manager.login(userId, password);
        if (user == null) {
            statusLabel.setText("Invalid credentials. Please try again.");
            passwordField.setText("");
            return;
        }
        dispose();
        if (user instanceof Student) {
            new StudentDashboard((Student) user, manager).setVisible(true);
        } else if (user instanceof Staff) {
            new StaffDashboard((Staff) user, manager).setVisible(true);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
