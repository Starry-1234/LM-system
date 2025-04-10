package com.management.view;

import com.management.controller.BookController;
import com.management.model.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateBookDialog extends JDialog {
    private JTextField titleField, authorField, isbnField, publisherField, publicationDateField, stockQuantityField, categoryField, priceField;
    private JButton confirmButton, cancelButton;
    private BookController bookController;
    private Book book;

    public UpdateBookDialog(JFrame parent, BookController bookController, Book book) {
        super(parent, "修改书籍", true);
        this.bookController = bookController;
        this.book = book;
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // 创建面板
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.add(new JLabel("书名:"));
        titleField = new JTextField(book.getTitle());
        panel.add(titleField);

        panel.add(new JLabel("作者:"));
        authorField = new JTextField(book.getAuthor());
        panel.add(authorField);

        panel.add(new JLabel("ISBN:"));
        isbnField = new JTextField(book.getIsbn());
        panel.add(isbnField);

        panel.add(new JLabel("出版社:"));
        publisherField = new JTextField(book.getPublisher());
        panel.add(publisherField);

        panel.add(new JLabel("出版日期:"));
        publicationDateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(book.getPublicationDate()));
        panel.add(publicationDateField);

        panel.add(new JLabel("库存:"));
        stockQuantityField = new JTextField(String.valueOf(book.getStock_Quantity()));
        panel.add(stockQuantityField);

        panel.add(new JLabel("分类:"));
        categoryField = new JTextField(book.getCategory());
        panel.add(categoryField);

        panel.add(new JLabel("价格:"));
        priceField = new JTextField(String.valueOf(book.getPrice()));
        panel.add(priceField);

        add(panel, BorderLayout.CENTER);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        confirmButton = new JButton("确认");
        cancelButton = new JButton("取消");

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBook();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateBook() {
        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();
        String publisher = publisherField.getText();
        String publicationDateString = publicationDateField.getText();
        int stock_Quantity = Integer.parseInt(stockQuantityField.getText());
        String category = categoryField.getText();
        double price = Double.parseDouble(priceField.getText());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date publicationDate = null;
        try {
            publicationDate = dateFormat.parse(publicationDateString);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "日期格式错误，请使用 yyyy-MM-dd 格式");
            return;
        }

        // 检查所有字段是否为空
        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || publisher.isEmpty() || price == 0
                || publicationDate == null || category.isEmpty() || stock_Quantity == 0) {
            JOptionPane.showMessageDialog(this, "所有字段不能为空！");
            return;
        }

        // 更新书籍
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPublisher(publisher);
        book.setPublicationDate(publicationDate);
        book.setStock_Quantity(stock_Quantity);
        book.setCategory(category);
        book.setPrice(price);
        bookController.updateBook(book);

        dispose();
    }
}
