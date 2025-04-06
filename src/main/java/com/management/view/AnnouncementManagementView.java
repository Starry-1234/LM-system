package com.management.view;

import com.management.controller.AnnouncementController;
import com.management.dao.AnnouncementDAO;
import com.management.model.Announcement;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnnouncementManagementView extends JPanel {
    private AnnouncementController announcementController;
    private AnnouncementDAO announcementDAO;
    private JTable announcementTable;
    private DefaultTableModel tableModel;
    private static final Logger logger = Logger.getLogger(AnnouncementManagementView.class.getName());

    public AnnouncementManagementView(Connection conn) {
        // 修复：通过 Connection 创建 AnnouncementDAO，再将 AnnouncementDAO 传递给 AnnouncementController
        announcementDAO = new AnnouncementDAO(conn); // 创建 AnnouncementDAO 实例
        announcementController = new AnnouncementController(announcementDAO); // 将 AnnouncementDAO 传递给 AnnouncementController

        setLayout(new BorderLayout());

        // 标题
        JLabel titleLabel = new JLabel("公告管理", SwingConstants.CENTER);
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
        String[] columnNames = {"选择", "ID", "标题", "内容", "发布日期", "操作"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class; // 第一列为复选框
                }
                if (columnIndex == 4) {
                    return Date.class; // 发布日期列为日期
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 5; // 只有复选框和操作列可编辑
            }
        };

        // 表格
        announcementTable = new JTable(tableModel);
        announcementTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer()); // 操作列渲染按钮
        announcementTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox())); // 操作列编辑按钮
        JScrollPane scrollPane = new JScrollPane(announcementTable);
        add(scrollPane, BorderLayout.CENTER);

        // 设置表格行高
        announcementTable.setRowHeight(30); // 设置行高为30像素

        // 调整操作列宽度
        announcementTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        // 加载公告数据
        loadAnnouncementData();

        // 添加按钮事件
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddAnnouncementDialog();
            }
        });

        // 删除按钮事件
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedAnnouncements();
            }
        });

        // 查询按钮事件
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showQueryAnnouncementDialog();
            }
        });
    }

    // 加载公告数据
    private void loadAnnouncementData() {
        tableModel.setRowCount(0); // 清空表格
        List<Announcement> announcements = announcementController.getAllAnnouncements();
        if (announcements == null || announcements.isEmpty()) {
            logger.warning("未加载到任何公告数据！");
        } else {
            for (Announcement announcement : announcements) {
                Object[] rowData = {
                        false, // 复选框
                        announcement.getId(),
                        announcement.getTitle(),
                        announcement.getContent(),
                        announcement.getPublishDate(),
                        "" // 操作列（内容由渲染器生成）
                };
                tableModel.addRow(rowData);
            }
        }
    }

    // 显示添加公告对话框
    private void showAddAnnouncementDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "添加公告", true);
        dialog.setSize(500, 400); // 增加对话框大小
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 设置组件之间的间距

        JTextField idField = new JTextField(20); // 设置列数
        JTextField titleField = new JTextField(20); // 设置列数
        JTextArea contentField = new JTextArea(5, 20); // 设置行数和列数
        contentField.setLineWrap(true);
        contentField.setWrapStyleWord(true);
        JScrollPane contentScrollPane = new JScrollPane(contentField);
        JFormattedTextField publishDateField = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        publishDateField.setValue(new Date());

        // 添加组件到面板
        addComponent(panel, new JLabel("ID:"), gbc, 0, 0, 1, 1);
        addComponent(panel, idField, gbc, 1, 0, 2, 1);
        addComponent(panel, new JLabel("标题:"), gbc, 0, 1, 1, 1);
        addComponent(panel, titleField, gbc, 1, 1, 2, 1);
        addComponent(panel, new JLabel("内容:"), gbc, 0, 2, 1, 1);
        addComponent(panel, contentScrollPane, gbc, 1, 2, 2, 1);
        addComponent(panel, new JLabel("发布日期:"), gbc, 0, 3, 1, 1);
        addComponent(panel, publishDateField, gbc, 1, 3, 2, 1);

        JButton confirmButton = new JButton("完成");
        JButton cancelButton = new JButton("取消");

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idStr = idField.getText();
                String title = titleField.getText();
                String content = contentField.getText();
                Date publishDate = (Date) publishDateField.getValue();

                if (idStr.isEmpty() || title.isEmpty() || content.isEmpty() || publishDate == null) {
                    JOptionPane.showMessageDialog(dialog, "所有字段不能为空！");
                    return;
                }

                int id;
                try {
                    id = Integer.parseInt(idStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "公告ID必须是数字！");
                    return;
                }

                // 添加公告
                Announcement newAnnouncement = new Announcement(id, title, content, publishDate);
                announcementController.addAnnouncement(newAnnouncement);

                // 刷新表格
                loadAnnouncementData();
                dialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(buttonPanel, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // 删除选中的公告
    private void deleteSelectedAnnouncements() {
        int rowCount = tableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
            if (isSelected) {
                int id = (int) tableModel.getValueAt(i, 1); // 获取公告ID
                announcementController.deleteAnnouncement(id);
                tableModel.removeRow(i); // 从表格中移除该行
            }
        }

        // 刷新表格
        loadAnnouncementData();
    }

    // 显示查询公告对话框
    private void showQueryAnnouncementDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "查询公告", true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel idLabel = new JLabel("公告ID:");
        JTextField idField = new JTextField();

        panel.add(idLabel);
        panel.add(idField);

        JButton confirmButton = new JButton("查询");
        JButton cancelButton = new JButton("取消");

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idStr = idField.getText();
                if (idStr.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "公告ID不能为空！");
                    return;
                }

                int id;
                try {
                    id = Integer.parseInt(idStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "公告ID必须是数字！");
                    return;
                }

                Announcement announcement = announcementController.getAnnouncementById(id);
                if (announcement == null) {
                    JOptionPane.showMessageDialog(dialog, "公告不存在！");
                    return;
                }

                // 显示公告信息
                JOptionPane.showMessageDialog(dialog, "标题: " + announcement.getTitle() + "\n内容: " + announcement.getContent() + "\n发布日期: " + announcement.getPublishDate());
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
                    int id = (int) tableModel.getValueAt(currentRow, 1); // 获取公告ID
                    logger.info("编辑按钮点击，公告ID: " + id); // 添加调试信息
                    showEditAnnouncementDialog(id);
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

    // 显示编辑公告对话框
    private void showEditAnnouncementDialog(int id) {
        try {
            Announcement announcement = announcementController.getAnnouncementById(id);
            if (announcement == null) {
                JOptionPane.showMessageDialog(this, "公告不存在！");
                return;
            }

            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "编辑公告", true);
            dialog.setSize(500, 400); // 增加对话框大小
            dialog.setLocationRelativeTo(this);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5); // 设置组件之间的间距

            JTextField idField = new JTextField(String.valueOf(announcement.getId()), 20); // 设置列数
            JTextField titleField = new JTextField(announcement.getTitle(), 20); // 设置列数
            JTextArea contentField = new JTextArea(announcement.getContent(), 5, 20); // 设置行数和列数
            contentField.setLineWrap(true);
            contentField.setWrapStyleWord(true);
            JScrollPane contentScrollPane = new JScrollPane(contentField);
            JFormattedTextField publishDateField = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
            publishDateField.setValue(announcement.getPublishDate());

            // 添加组件到面板
            addComponent(panel, new JLabel("ID:"), gbc, 0, 0, 1, 1);
            addComponent(panel, idField, gbc, 1, 0, 2, 1);
            addComponent(panel, new JLabel("标题:"), gbc, 0, 1, 1, 1);
            addComponent(panel, titleField, gbc, 1, 1, 2, 1);
            addComponent(panel, new JLabel("内容:"), gbc, 0, 2, 1, 1);
            addComponent(panel, contentScrollPane, gbc, 1, 2, 2, 1);
            addComponent(panel, new JLabel("发布日期:"), gbc, 0, 3, 1, 1);
            addComponent(panel, publishDateField, gbc, 1, 3, 2, 1);

            JButton confirmButton = new JButton("完成");
            JButton cancelButton = new JButton("取消");

            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newIdStr = idField.getText();
                    String title = titleField.getText();
                    String content = contentField.getText();
                    Date publishDate = (Date) publishDateField.getValue();

                    if (newIdStr.isEmpty() || title.isEmpty() || content.isEmpty() || publishDate == null) {
                        JOptionPane.showMessageDialog(dialog, "所有字段不能为空！");
                        return;
                    }

                    int newId;
                    try {
                        newId = Integer.parseInt(newIdStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "公告ID必须是数字！");
                        return;
                    }

                    // 更新公告信息
                    Announcement updatedAnnouncement = new Announcement(newId, title, content, publishDate);
                    announcementController.editAnnouncement(updatedAnnouncement);

                    // 刷新表格
                    loadAnnouncementData();
                    dialog.dispose();
                }
            });

            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(confirmButton);
            buttonPanel.add(cancelButton);

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 3;
            gbc.anchor = GridBagConstraints.EAST;
            panel.add(buttonPanel, gbc);

            dialog.add(panel);
            dialog.setVisible(true);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "加载公告信息时发生错误", ex);
            JOptionPane.showMessageDialog(this, "加载公告信息时发生错误：" + ex.getMessage());
        }
    }

    // 辅助方法，用于简化 GridBagConstraints 的设置
    private void addComponent(JPanel panel, JComponent component, GridBagConstraints gbc, int x, int y, int width, int height) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        panel.add(component, gbc);
    }
}
