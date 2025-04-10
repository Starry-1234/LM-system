package com.management.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminLoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private Connection connection;

    public AdminLoginDialog(JFrame parent, Connection connection) {
        super(parent, "管理员登录", true);
        this.connection = connection;
        adminLoginPanel();
    }

    private void adminLoginPanel() {
        setLayout(new BorderLayout());
        setSize(300, 200);
        setLocationRelativeTo(getParent());

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("用户名:"));
        usernameField = new JTextField();
        panel.add(usernameField);
        panel.add(new JLabel("密码:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("登录");
        cancelButton = new JButton("取消");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkAdminLogin()) {
                    dispose();
                    getParent().setVisible(true);
                    ((MainFrame) getParent()).showPage("AdminManagement");
                } else {
                    JOptionPane.showMessageDialog(AdminLoginDialog.this, "用户名或密码错误", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private boolean checkAdminLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        String sql = "SELECT * FROM admins WHERE admin_name = ? AND admin_password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
