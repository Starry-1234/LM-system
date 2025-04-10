package com.management.view;

import com.management.controller.AdminController;
import com.management.controller.BookController;
import com.management.controller.BorrowRecordController;
import com.management.model.Admin;
import com.management.model.Book;
import com.management.dao.BookDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdminManagementView extends JPanel {
    private JTextField titleField;
    private JTextField authorField;
    private JTextField isbnField;
    private AdminController adminController;
    private BookController bookController;
    private JTable adminTable;// 管理員表
    private JTable bookTable; //书籍表
    private DefaultTableModel bookTableModel;
    private DefaultTableModel adminTableModel;
    private JTextField publisherField;
    private JTextField publicationDateField;
    private JTextField stockQuantityField;
    private JTextField categoryField;
    private JTextField priceField;
    private BorrowRecordController borrowrecordController;

    public AdminManagementView() {
        /* 整体布局采用BorderLayout
         分为四部分
         上方为“管理员管理”
         中部为功能按钮
         东部为当前已有的管理员数据
         底部显示目前已有的书籍*/
        setLayout(new BorderLayout());

        // 上部
        JLabel titleLabel = new JLabel("管理员管理", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // 中部采用GridBagLayout布局将按钮分区
        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 初始化所有字段
        titleField = new JTextField();
        authorField = new JTextField();
        isbnField = new JTextField();
        publisherField = new JTextField();
        publicationDateField = new JTextField();
        stockQuantityField = new JTextField();
        categoryField = new JTextField();
        priceField = new JTextField();

        // 添加管理员按钮
        gbc.gridx = 0;
        gbc.gridy = 0;
        JButton addAdminButton = new JButton("添加管理员");
        addAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddAdminDialog();
            }
        });
        adminPanel.add(addAdminButton, gbc);

        // 删除管理员按钮
        gbc.gridx = 1;
        gbc.gridy = 0;
        JButton deleteAdminButton = new JButton("删除管理员");
        deleteAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAdmin();
            }
        });
        adminPanel.add(deleteAdminButton, gbc);

        // 添加书籍按钮
        gbc.gridx = 0;
        gbc.gridy = 1;
        JButton addBookButton = new JButton("添加书籍");
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddBookDialog();
                loadBooks();
            }
        });
        adminPanel.add(addBookButton, gbc);

        // 修改书籍按钮
        gbc.gridx = 1;
        gbc.gridy = 1;
        JButton updateBookButton = new JButton("修改书籍");
        updateBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUpdateBookDialog();
                loadBooks();
            }
        });
        adminPanel.add(updateBookButton, gbc);

        // 删除书籍按钮
        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton deleteBookButton = new JButton("删除书籍");
        deleteBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });
        adminPanel.add(deleteBookButton, gbc);

        // 中部
        add(adminPanel, BorderLayout.CENTER);

        // 下部
        // Table to display books
        bookTableModel = new DefaultTableModel(new Object[]{"ID", "书名", "作者", "ISBN", "出版社", "出版日期", "库存数量", "分类", "价格"}, 0);
        bookTable = new JTable(bookTableModel);
        JScrollPane bookPane = new JScrollPane(bookTable);
        add(bookPane, BorderLayout.SOUTH);

        bookController = new BookController();
        loadBooks();

        // 东部放目前已有的管理员数据
        adminTableModel = new DefaultTableModel(new Object[]{"管理员ID", "管理员姓名", "管理员密码"}, 0);
        adminTable = new JTable(adminTableModel);
        JScrollPane adminsPane = new JScrollPane(adminTable);
        add(adminsPane, BorderLayout.EAST);

        adminController = new AdminController();
        loadAdmins();
    }

    // 加载书籍数据
    public void loadBooks() {
        // 清空表格模型中的所有行
        bookTableModel.setRowCount(0);
        List<Book> books = bookController.getAllBooks();
        if (books != null) {
            for (Book book : books) {
                bookTableModel.addRow(new Object[]{
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn(),
                        book.getPublisher(),
                        new SimpleDateFormat("yyyy-MM-dd").format(book.getPublicationDate()),
                        book.getStock_Quantity(),
                        book.getCategory(),
                        book.getPrice()
                });
            }
        }
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

    //删除管理员
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

    // 显示添加书籍对话框
    private void showAddBookDialog() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        AddBookDialog addBookDialog = new AddBookDialog(parentFrame, bookController);
        addBookDialog.setVisible(true);
    }

    // 显示修改书籍对话框
    private void showUpdateBookDialog() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要更新的书籍");
            return;
        }

        int id = (int) bookTableModel.getValueAt(selectedRow, 0);
        Book book = bookController.getBookById(id);
        if (book != null) {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            UpdateBookDialog updateBookDialog = new UpdateBookDialog(parentFrame, bookController, book);
            updateBookDialog.setVisible(true);
            loadBooks();
        } else {
            JOptionPane.showMessageDialog(this, "书籍信息获取失败");
        }
    }


    // 删除书籍
    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的书籍");
            return;
        }

        int id = (int) bookTableModel.getValueAt(selectedRow, 0);
        bookController.deleteBook(id);
        JOptionPane.showMessageDialog(this, "书籍已删除");
        loadBooks();

    }


}