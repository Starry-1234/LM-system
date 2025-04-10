package com.management.view;

import com.management.controller.AdminController;
import com.management.dao.AdminDAO;
import com.management.model.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddAdminDialog extends JDialog {
    private JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
    private JLabel nameLabel = new JLabel("管理员名称：", JLabel.RIGHT);
    private JTextField nameField = new JTextField(); // 新管理员名称
    private JLabel passwordLabel = new JLabel("管理员密码：", JLabel.RIGHT);
    private JTextField passwordField = new JTextField(); // 新管理员密码
    private JLabel emailLabel = new JLabel("邮箱：", JLabel.RIGHT);
    private JTextField emailField = new JTextField(); // 新管理员邮箱
    private JButton addButton = new JButton("添加");// 添加按钮

    public AddAdminDialog(JFrame parentFrame, AdminController adminController) {
        super(parentFrame, "添加管理员", true);

        nameLabel.setPreferredSize(new Dimension(70, 30));
        panel.add(nameLabel);
        nameField.setPreferredSize(new Dimension(200, 30));
        panel.add(nameField);

        passwordLabel.setPreferredSize(new Dimension(70, 30));
        panel.add(passwordLabel);
        passwordField.setPreferredSize(new Dimension(200, 30));
        panel.add(passwordField);

        emailLabel.setPreferredSize(new Dimension(70, 30));
        panel.add(emailLabel);
        emailField.setPreferredSize(new Dimension(200, 30));
        panel.add(emailField);

        panel.add(addButton);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAdmin();
            }
        });

        add(panel);

        setSize(350, 250);
        setLocationRelativeTo(null);
        // DISPOSE_ON_CLOSE：只销毁当前的窗体
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
    private void addAdmin() {
        String adminName = nameField.getText().trim();
        String adminPassword =passwordField.getText().trim();
        String adminEmail = emailField.getText().trim();

        if (adminName.isEmpty() || adminPassword.isEmpty() || adminEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "管理员名称和密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Admin admin = new Admin(adminName, adminPassword, adminEmail);
        AdminController adminController = new AdminController();
        adminController.addAdmin(admin);

        JOptionPane.showMessageDialog(this, "管理员已添加", "成功", JOptionPane.INFORMATION_MESSAGE);
        dispose();

    }
}
