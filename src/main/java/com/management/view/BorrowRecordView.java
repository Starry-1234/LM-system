package com.management.view;

import com.management.controller.BorrowRecordController;
import com.management.dao.BorrowRecordDAO;
import com.management.model.BorrowRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BorrowRecordView extends JPanel {

    private BorrowRecordController borrowRecordController;
    private JTable AdminTable;
    private DefaultTableModel tableModel;

    public BorrowRecordView() {
        setLayout(new BorderLayout());

        JPanel selectPanel = new JPanel();
        selectPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 添加输入借阅人姓名的标签
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        selectPanel.add(new JLabel("请输入借阅人姓名:"), gbc);

        // 添加输入借阅人姓名的输入框
        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField borrowerField = new JTextField(20);
        selectPanel.add(borrowerField, gbc);

        // 添加查询按钮
        JButton queryButton = new JButton("查询");
        gbc.gridx = 2;
        selectPanel.add(queryButton, gbc);

        // 为通过借阅人查询按钮添加事件监听器
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookName = borrowerField.getText();
                selectBorrowRecordsByBorrower(bookName);
                borrowerField.setText(""); // 清空输入框
            }
        });

        // 添加输入书籍名称的标签
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        selectPanel.add(new JLabel("请输入书籍名称:"), gbc);

        // 添加输入书籍名称的输入框
        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField booknameField = new JTextField(20);
        selectPanel.add(booknameField, gbc);

        // 添加通过书籍名称查询按钮
        JButton selectbyBooknameButton = new JButton("查询");
        gbc.gridx = 2;
        selectPanel.add(selectbyBooknameButton, gbc);

        // 为通过书籍名查询按钮添加事件监听器
        selectbyBooknameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookName = booknameField.getText();
                selectBorrowRecordsByBookName(bookName);
                booknameField.setText(""); // 清空输入框
            }
        });

        // 添加输入isbn的标签
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        selectPanel.add(new JLabel("请输入ISBN:"), gbc);

        // 添加输入书籍名称的输入框
        gbc.gridx = 1;
        gbc.gridy = 2;
        JTextField isbnField = new JTextField(20);
        selectPanel.add(isbnField, gbc);

        // 添加通过书籍名称查询按钮
        JButton selectbyisbnButton = new JButton("查询");
        gbc.gridx = 2;
        selectPanel.add(selectbyisbnButton, gbc);

        // 为通过书籍名查询按钮添加事件监听器
        selectbyisbnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String isbn = isbnField.getText();
                selectBorrowRecordsByisbn(isbn);
                isbnField.setText(""); // 清空输入框
            }
        });

        add(selectPanel, BorderLayout.NORTH);

        // 表格模型
        tableModel = new DefaultTableModel(new Object[]{"ID", "ISBN", "书名", "借阅人", "借阅时间", "归还时间"}, 0);
        AdminTable = new JTable(tableModel);
        JScrollPane BookPane = new JScrollPane(AdminTable);
        add(BookPane, BorderLayout.CENTER);

        borrowRecordController = new BorrowRecordController();
        loadBorrowRecords();
    }

    // 加载所有借阅记录
    private void loadBorrowRecords() {
        tableModel.setRowCount(0); // 清空表格
        List<BorrowRecord> borrowRecords = borrowRecordController.getAllBorrowRecords();
        if (borrowRecords != null) {
            for (BorrowRecord record : borrowRecords) {
                tableModel.addRow(new Object[]{
                        record.getId(),
                        record.getISBN(),
                        record.getBookName(),
                        record.getBorrower(),
                        record.getBorrowingTime(),
                        record.getReturnTime()
                });
            }
        }
    }

    // 通过借阅人查询借阅记录
    private void selectBorrowRecordsByBorrower(String borrowerName) {
        tableModel.setRowCount(0); // 清空表格
        List<BorrowRecord> borrowRecords = borrowRecordController.getBorrowRecordsByBorrower(borrowerName);
        if (borrowRecords != null) {
            for (BorrowRecord record : borrowRecords) {
                tableModel.addRow(new Object[]{
                        record.getId(),
                        record.getISBN(),
                        record.getBookName(),
                        record.getBorrower(),
                        record.getBorrowingTime(),
                        record.getReturnTime()
                });
            }
        }
    }

    // 通过书籍名查询借阅记录
    private void selectBorrowRecordsByBookName(String bookName) {
        tableModel.setRowCount(0); // 清空表格
        List<BorrowRecord> borrowRecords = borrowRecordController.getBorrowRecordsByBookName(bookName);
        if (borrowRecords != null) {
            for (BorrowRecord record : borrowRecords) {
                tableModel.addRow(new Object[]{
                        record.getId(),
                        record.getISBN(),
                        record.getBookName(),
                        record.getBorrower(),
                        record.getBorrowingTime(),
                        record.getReturnTime()
                });
            }
        }
    }

    // 通过ISBN查询借阅记录
    private void selectBorrowRecordsByisbn(String isbn) {
        tableModel.setRowCount(0); // 清空表格
        List<BorrowRecord> borrowRecords = borrowRecordController.getBorrowRecordsByIsbn(isbn);
        if (borrowRecords != null) {
            for (BorrowRecord record : borrowRecords) {
                tableModel.addRow(new Object[]{
                        record.getId(),
                        record.getISBN(),
                        record.getBookName(),
                        record.getBorrower(),
                        record.getBorrowingTime(),
                        record.getReturnTime()
                });
            }
        }
    }
}