package com.management.view;

import com.management.controller.BookController;
import com.management.controller.BorrowRecordController;
import com.management.model.Book;
import com.management.model.BorrowRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BookManagementView extends JPanel {
    private BookController bookController;
    private BorrowRecordController borrowController;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField isbnField;
    private JTextField publisherField;
    private JTextField publicationDateField;
    private JTextField stockQuantityField;
    private JTextField categoryField;
    private JTextField priceField;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private String loggerInUserName;// 登录用户的用户名

    public BookManagementView(String username) {
        this.loggerInUserName = username;// 初始化用户名
        this.borrowController = new BorrowRecordController();// 初始化借阅记录控制器
        setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("书籍管理", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
/*
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(new JLabel("书名:"), gbc);

        gbc.gridx = 1;
        titleField = new JTextField();
        contentPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(new JLabel("作者:"), gbc);

        gbc.gridx = 1;
        authorField = new JTextField();
        contentPanel.add(authorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(new JLabel("ISBN:"), gbc);

        gbc.gridx = 1;
        isbnField = new JTextField();
        contentPanel.add(isbnField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(new JLabel("出版社:"), gbc);

        gbc.gridx = 1;
        publisherField = new JTextField();
        contentPanel.add(publisherField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(new JLabel("出版日期 (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        publicationDateField = new JTextField();
        contentPanel.add(publicationDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPanel.add(new JLabel("库存数量:"), gbc);

        gbc.gridx = 1;
        stockQuantityField = new JTextField();
        contentPanel.add(stockQuantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        contentPanel.add(new JLabel("分类:"), gbc);

        gbc.gridx = 1;
        categoryField = new JTextField();
        contentPanel.add(categoryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        contentPanel.add(new JLabel("价格:"), gbc);

        gbc.gridx = 1;
        priceField = new JTextField();
        contentPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        JButton addButton = new JButton("添加书籍");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });
        contentPanel.add(addButton, gbc);

        gbc.gridx = 1;
        JButton updateButton = new JButton("更新书籍");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBook();
            }
        });
        contentPanel.add(updateButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        JButton deleteButton = new JButton("删除书籍");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });
        contentPanel.add(deleteButton, gbc);*/

        gbc.gridx = 1;
        gbc.gridy = 1;
        JButton borrowButton = new JButton(" 借阅选中书籍 ");
        borrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrowBook();
            }
        });
        contentPanel.add(borrowButton, gbc);

        // 添加查询相关的 UI 元素
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel searchLabel = new JLabel("查询书名:");
        contentPanel.add(searchLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField searchField = new JTextField();
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchBooks(searchField.getText());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        contentPanel.add(searchField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JButton searchButton = new JButton("查询");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBooks(searchField.getText());
            }
        });
        contentPanel.add(searchButton, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // Table to display books
        tableModel = new DefaultTableModel(new Object[]{"ID", "书名", "作者", "ISBN", "出版社", "出版日期", "库存数量", "分类", "价格"}, 0);
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.SOUTH);

        bookController = new BookController();
        loadBooks();
    }

   /* private void addBook() {
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

        Book book = new Book(0, title, author, isbn, publisher, publicationDate, stock_Quantity, category, price);
        bookController.addBook(book);
        JOptionPane.showMessageDialog(this, "书籍已添加");
        loadBooks();
        clearFields();
    }

    private void updateBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要更新的书籍");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
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

        Book book = new Book(id, title, author, isbn, publisher, publicationDate, stock_Quantity, category, price);
        bookController.updateBook(book);
        JOptionPane.showMessageDialog(this, "书籍已更新");
        loadBooks();
        clearFields();
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的书籍");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        bookController.deleteBook(id);
        JOptionPane.showMessageDialog(this, "书籍已删除");
        loadBooks();
        clearFields();
    }
*/
    private void searchBooks(String title) {
        tableModel.setRowCount(0);
        List<Book> books = bookController.findBooksByTitle(title);
        if (books != null) {
            for (Book book : books) {
                tableModel.addRow(new Object[]{
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

    private void loadBooks() {
        tableModel.setRowCount(0);
        List<Book> books = bookController.getAllBooks();
        if (books != null) {
            for (Book book : books) {
                tableModel.addRow(new Object[]{
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

    // 借阅功能
    private void borrowBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要借阅的书籍");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String isbn = (String) tableModel.getValueAt(selectedRow, 3);
        String bookname = (String) tableModel.getValueAt(selectedRow, 1);
        // 获取当前时间
        Date borrowingTime = new Date();

        Book book = bookController.getBookById(id);

        // 若所选中的书籍的库存数量为0，则提示借阅失败
        if (book.getStock_Quantity() == 0) {
            JOptionPane.showMessageDialog(this, "借阅失败，库存数量为0");
            return;
        }

        borrowController.addBorrowRecord(new BorrowRecord(isbn, bookname, loggerInUserName, borrowingTime));

        // 更新库存数量

        if (book != null) {
            book.setStock_Quantity(book.getStock_Quantity() - 1);
            bookController.updateBook(book);
        }
        loadBooks();
        JOptionPane.showMessageDialog(this, "借阅成功");
    }

/*
    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        publisherField.setText("");
        publicationDateField.setText("");
        stockQuantityField.setText("");
        categoryField.setText("");
        priceField.setText("");
    }
*/

    public static void main(String[] args) {
        JFrame frame = new JFrame("书籍管理系统");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 传递默认用户名"admin"
        frame.add(new BookManagementView("admin"));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
