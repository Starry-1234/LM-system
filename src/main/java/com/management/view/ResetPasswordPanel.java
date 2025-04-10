package com.management.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class ResetPasswordPanel extends JPanel {
    private JTextField usernameField;
    private JTextField emailField;
    private LoginFrame loginFrame;
    private Properties dbProperties;

    public ResetPasswordPanel(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        setLayout(new BorderLayout());

        // 读取 db.properties 文件
        dbProperties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/db.properties")) {
            dbProperties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "无法加载数据库配置文件", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JLabel titleLabel = new JLabel("找回密码", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0)); // 上、左、下、右边距

        JPanel resetPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel usernameLabel = new JLabel("用户名：");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        gbc.gridx = 0;
        gbc.gridy = 0;
        resetPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        usernameField.setPreferredSize(new Dimension(usernameField.getPreferredSize().width, usernameField.getFontMetrics(usernameField.getFont()).getHeight() + 5));
        gbc.gridx = 1;
        gbc.gridy = 0;
        resetPanel.add(usernameField, gbc);

        JLabel emailLabel = new JLabel("邮箱：");
        emailLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        gbc.gridx = 0;
        gbc.gridy = 1;
        resetPanel.add(emailLabel, gbc);

        emailField = new JTextField(15);
        emailField.setPreferredSize(new Dimension(emailField.getPreferredSize().width, emailField.getFontMetrics(emailField.getFont()).getHeight() + 5));
        gbc.gridx = 1;
        gbc.gridy = 1;
        resetPanel.add(emailField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton resetButton = new JButton("重置");
        resetButton.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        JButton backButton = new JButton("返回");
        backButton.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        buttonPanel.add(resetButton);
        buttonPanel.add(backButton);

        add(titleLabel, BorderLayout.NORTH);
        add(resetPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String email = emailField.getText();
                if (username.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(ResetPasswordPanel.this, "请输入完整信息", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        String url = dbProperties.getProperty("db.url");
                        String dbUsername = dbProperties.getProperty("db.username");
                        String dbPassword = dbProperties.getProperty("db.password");
                        String driver = dbProperties.getProperty("db.driver");

                        Class.forName(driver);
                        Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                        String sql = "SELECT * FROM admins WHERE admin_name = ? AND email = ?";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, username);
                        pstmt.setString(2, email);
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                            JOptionPane.showMessageDialog(ResetPasswordPanel.this, "重置密码成功，新密码已发送到您的邮箱", "提示", JOptionPane.INFORMATION_MESSAGE);
                            loginFrame.showPanel("login");
                        } else {
                            JOptionPane.showMessageDialog(ResetPasswordPanel.this, "用户名或邮箱错误", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(ResetPasswordPanel.this, "重置失败", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginFrame.showPanel("login");
            }
        });
    }
}
