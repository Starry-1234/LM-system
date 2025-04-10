package com.management.view;

import com.management.controller.AdminController;
import com.management.model.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminManagementView extends JPanel {
    private AdminController adminController;
    private JTable adminTable; // 管理员表
    private DefaultTableModel adminTableModel;

    public AdminManagementView() {
        setLayout(new BorderLayout());

        JPanel adminPanel = new JPanel(new BorderLayout());
        add(adminPanel, BorderLayout.CENTER);

        // 管理员操作按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addAdminButton = new JButton("添加管理员");
        JButton deleteAdminButton = new JButton("删除管理员");
        JButton updateAdminButton = new JButton("修改管理员");

        addAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddAdminDialog();
            }
        });

        deleteAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAdmin();
            }
        });

        updateAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUpdateAdminDialog();
            }
        });

        buttonPanel.add(addAdminButton);
        buttonPanel.add(deleteAdminButton);
        buttonPanel.add(updateAdminButton);
        adminPanel.add(buttonPanel, BorderLayout.NORTH);

        // 管理员表格
        adminTableModel = new DefaultTableModel(new Object[]{"管理员ID", "管理员姓名", "管理员密码"}, 0);
        adminTable = new JTable(adminTableModel);
        JScrollPane adminsPane = new JScrollPane(adminTable);
        adminPanel.add(adminsPane, BorderLayout.CENTER);

        adminController = new AdminController();
        loadAdmins();
    }

    // 加载管理员数据
    public void loadAdmins() {
        // 清空表格模型中的所有行
        adminTableModel.setRowCount(0);
        List<Admin> admins = adminController.getAllAdmins();
        if (admins != null) {
            for (Admin admin : admins) {
                adminTableModel.addRow(new Object[]{
                        admin.getAdminid(),
                        admin.getAdminname(),
                        admin.getPassword()
                });
            }
        }
    }

    // 显示添加管理员对话框
    private void showAddAdminDialog() {
        JFrame parentFrame2 = (JFrame) SwingUtilities.getWindowAncestor(this);
        AddAdminDialog addAdminDialog = new AddAdminDialog(parentFrame2, adminController);
        addAdminDialog.setVisible(true);
        loadAdmins();
    }

    // 删除管理员
    private void deleteAdmin() {
        int selectedRow = adminTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的管理员");
            return;
        }

        int adminId = (int) adminTableModel.getValueAt(selectedRow, 0);
        adminController.deleteAdmin(adminId);
        JOptionPane.showMessageDialog(this, "管理员已删除");
        loadAdmins();
    }

    // 显示修改管理员对话框
    private void showUpdateAdminDialog() {
        int selectedRow = adminTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要修改的管理员");
            return;
        }

        int adminId = (int) adminTableModel.getValueAt(selectedRow, 0);
        String adminname = (String) adminTableModel.getValueAt(selectedRow, 1);
        String password = (String) adminTableModel.getValueAt(selectedRow, 2);

        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "修改管理员", true);
        dialog.setSize(350, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel adminnameLabel = new JLabel("管理员名:");
        JTextField adminnameField = new JTextField(adminname);
        JLabel passwordLabel = new JLabel("密码:");
        JTextField passwordField = new JTextField(password);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(adminnameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(adminnameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmButton = new JButton("完成");
        JButton cancelButton = new JButton("取消");

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String newAdminName = adminnameField.getText();
                String newPassword = passwordField.getText();

                if (adminname.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "所有字段不能为空！");
                    return;
                }

                // 修改管理员
                Admin updatedAdmin = new Admin(adminId, newAdminName, newPassword);
                adminController.updateAdmin(updatedAdmin);

                JOptionPane.showMessageDialog(dialog, "管理员已修改");
                loadAdmins();
                dialog.dispose();

            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

}