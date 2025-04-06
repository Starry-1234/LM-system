package com.management.view;

import com.management.controller.UserController;
import com.management.dao.UserDAO;
import com.management.model.User;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserManagementView extends JPanel {
    private UserController userController;
    private UserDAO userDAO;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private static final Logger logger = Logger.getLogger(UserManagementView.class.getName());

    public UserManagementView(Connection conn) {
        // 修复：通过 Connection 创建 UserDAO，再将 UserDAO 传递给 UserController
        userDAO = new UserDAO(conn); // 创建 UserDAO 实例
        userController = new UserController(userDAO); // 将 UserDAO 传递给 UserController

        setLayout(new BorderLayout());

        // 标题
        JLabel titleLabel = new JLabel("用户管理", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // 按钮面板（添加、删除、查询）
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("添加");
        JButton deleteButton = new JButton("删除");
        JButton queryButton = new JButton("查询");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(queryButton);
        add(buttonPanel, BorderLayout.NORTH);

        // 表格模型
        String[] columnNames = {"选择", "ID", "用户名", "性别", "密码", "角色", "邮箱", "操作"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class; // 第一列为复选框
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 7; // 只有复选框和操作列可编辑
            }
        };

        // 表格
        userTable = new JTable(tableModel);
        userTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer()); // 操作列渲染按钮
        userTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox())); // 操作列编辑按钮
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // 设置表格行高
        userTable.setRowHeight(30); // 设置行高为30像素

        // 调整操作列宽度
        userTable.getColumnModel().getColumn(7).setPreferredWidth(100);

        // 加载用户数据
        loadUserData();

        // 添加按钮事件
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddUserDialog();
            }
        });

        // 删除按钮事件
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedUsers();
            }
        });

        // 查询按钮事件
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showQueryUserDialog();
            }
        });
    }

    // 加载用户数据
    private void loadUserData() {
        tableModel.setRowCount(0); // 清空表格
        List<User> users = userController.getAllUsers();
        if (users == null || users.isEmpty()) {
            logger.warning("未加载到任何用户数据！");
        } else {
            for (User user : users) {
                Object[] rowData = {
                        false, // 复选框
                        user.getId(),
                        user.getUsername(),
                        user.getGender(),
                        "******", // 密码不显示明文
                        user.getRole(),
                        user.getEmail(),
                        "" // 操作列（内容由渲染器生成）
                };
                tableModel.addRow(rowData);
            }
        }
    }

    // 显示添加用户对话框
    private void showAddUserDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "添加用户", true);
        dialog.setSize(350, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        JTextField idField = new JTextField();
        JTextField usernameField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField roleField = new JTextField();
        JTextField emailField = new JTextField();

        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("用户名:"));
        panel.add(usernameField);
        panel.add(new JLabel("性别:"));
        panel.add(genderField);
        panel.add(new JLabel("密码:"));
        panel.add(passwordField);
        panel.add(new JLabel("角色:"));
        panel.add(roleField);
        panel.add(new JLabel("邮箱:"));
        panel.add(emailField);

        JButton confirmButton = new JButton("完成");
        JButton cancelButton = new JButton("取消");

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String username = usernameField.getText();
                String gender = genderField.getText();
                String password = passwordField.getText();
                String role = roleField.getText();
                String email = emailField.getText();

                if (id.isEmpty() || username.isEmpty() || gender.isEmpty() || password.isEmpty() || role.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "所有字段不能为空！");
                    return;
                }

                // 添加用户
                User newUser = new User(id, username, gender, password, role, email);
                userController.addUser(newUser);

                // 刷新表格
                loadUserData();
                dialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        panel.add(confirmButton);
        panel.add(cancelButton);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    // 删除选中的用户
    private void deleteSelectedUsers() {
        int rowCount = tableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
            if (isSelected) {
                String id = (String) tableModel.getValueAt(i, 1); // 获取用户ID
                userController.deleteUser(id);
                tableModel.removeRow(i); // 从表格中移除该行
            }
        }

        // 刷新表格
        loadUserData();
    }

    // 显示查询用户对话框
    private void showQueryUserDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "查询用户", true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel idLabel = new JLabel("用户ID:");
        JTextField idField = new JTextField();

        panel.add(idLabel);
        panel.add(idField);

        JButton confirmButton = new JButton("查询");
        JButton cancelButton = new JButton("取消");

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "用户ID不能为空！");
                    return;
                }

                User user = userController.getUserById(id);
                if (user == null) {
                    JOptionPane.showMessageDialog(dialog, "用户不存在！");
                    return;
                }

                // 显示用户信息
                JOptionPane.showMessageDialog(dialog, "用户名: " + user.getUsername() + "\n性别: " + user.getGender() + "\n角色: " + user.getRole() + "\n邮箱: " + user.getEmail());
                dialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        panel.add(confirmButton);
        panel.add(cancelButton);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    // 操作列按钮渲染器
    private class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton editButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); // 设置按钮间距为0
            editButton = new JButton("编辑");
            editButton.setPreferredSize(new Dimension(80, 25)); // 设置按钮大小
            add(editButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // 操作列按钮编辑器
    private class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton editButton;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); // 设置按钮间距为0
            editButton = new JButton("编辑");
            editButton.setPreferredSize(new Dimension(80, 25)); // 设置按钮大小

            // 编辑按钮事件
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String id = (String) tableModel.getValueAt(currentRow, 1); // 获取用户ID
                    logger.info("编辑按钮点击，用户ID: " + id); // 添加调试信息
                    showEditUserDialog(id);
                    fireEditingStopped();
                }
            });

            panel.add(editButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            logger.info("当前行: " + currentRow); // 添加调试信息
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null; // 返回值可以为null，因为按钮编辑器不需要保存具体值
        }
    }

    // 显示编辑用户对话框
    private void showEditUserDialog(String id) {
        try {
            User user = userController.getUserById(id);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "用户不存在！");
                return;
            }

            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "编辑用户", true);
            dialog.setSize(350, 300);
            dialog.setLocationRelativeTo(this);

            JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
            JTextField idField = new JTextField(user.getId());
            JTextField usernameField = new JTextField(user.getUsername());
            JTextField genderField = new JTextField(user.getGender());
            JTextField passwordField = new JTextField(); // 密码字段留空
            JTextField roleField = new JTextField(user.getRole());
            JTextField emailField = new JTextField(user.getEmail());

            panel.add(new JLabel("ID:"));
            panel.add(idField);
            panel.add(new JLabel("用户名:"));
            panel.add(usernameField);
            panel.add(new JLabel("性别:"));
            panel.add(genderField);
            panel.add(new JLabel("密码:"));
            panel.add(passwordField);
            panel.add(new JLabel("角色:"));
            panel.add(roleField);
            panel.add(new JLabel("邮箱:"));
            panel.add(emailField);

            JButton confirmButton = new JButton("完成");
            JButton cancelButton = new JButton("取消");

            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newId = idField.getText();
                    String username = usernameField.getText();
                    String gender = genderField.getText();
                    String password = passwordField.getText();
                    String role = roleField.getText();
                    String email = emailField.getText();

                    if (newId.isEmpty() || username.isEmpty() || gender.isEmpty() || role.isEmpty() || email.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "所有字段不能为空！");
                        return;
                    }

                    // 更新用户信息
                    User updatedUser;
                    if (password.isEmpty()) {
                        // 如果密码字段为空，则保持原密码不变
                        updatedUser = new User(newId, username, gender, user.getPassword(), role, email);
                    } else {
                        // 如果密码字段不为空，则更新密码
                        updatedUser = new User(newId, username, gender, password, role, email);
                    }
                    userController.editUser(updatedUser);

                    // 刷新表格
                    loadUserData();
                    dialog.dispose();
                }
            });

            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });

            panel.add(confirmButton);
            panel.add(cancelButton);
            dialog.add(panel);
            dialog.setVisible(true);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "加载用户信息时发生错误", ex);
            JOptionPane.showMessageDialog(this, "加载用户信息时发生错误：" + ex.getMessage());
        }
    }
}
