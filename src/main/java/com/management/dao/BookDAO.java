package com.management.dao;

import com.management.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BookDAO {
    private Connection conn;

    public BookDAO(Connection conn) {
        this.conn = conn;
    }

    public void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, isbn, publisher, publication_date, stock_quantity, category, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getPublisher());
            stmt.setDate(5, new java.sql.Date(book.getPublicationDate().getTime()));
            stmt.setInt(6, book.getStock_Quantity());
            stmt.setString(7, book.getCategory());
            stmt.setDouble(8, book.getPrice());
            stmt.executeUpdate();
        }
    }

    public void updateBook(Book book) throws SQLException {
        Map<String, Object> modifiedFields = book.getModifiedFields();
        if (modifiedFields.isEmpty()) {
            return; // 没有字段被修改，直接返回
        }

        StringBuilder sql = new StringBuilder("UPDATE books SET ");
        int paramIndex = 1;
        for (String fieldName : modifiedFields.keySet()) {
            sql.append(fieldName).append(" = ?, ");
        }
        // 移除最后一个逗号和空格
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE id = ?");

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (Object value : modifiedFields.values()) {
                if (value instanceof Date) {
                    stmt.setDate(paramIndex++, new java.sql.Date(((Date) value).getTime()));
                } else {
                    stmt.setObject(paramIndex++, value);
                }
            }
            stmt.setInt(paramIndex, book.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Book getBookById(int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("publisher"),
                        convertSqlDateToUtilDate(rs.getDate("publication_date")),
                        rs.getInt("stock_quantity"),
                        rs.getString("category"),
                        rs.getDouble("price")
                );
            }
        }
        return null;
    }

    public List<Book> getAllBooks() throws SQLException {
        String sql = "SELECT * FROM books";
        List<Book> books = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("publisher"),
                        convertSqlDateToUtilDate(rs.getDate("publication_date")),
                        rs.getInt("stock_quantity"),
                        rs.getString("category"),
                        rs.getDouble("price")
                ));
            }
        }
        return books;
    }

    public List<Book> findBooksByTitle(String title) throws SQLException {
        String sql = "SELECT * FROM books WHERE title LIKE ?";
        List<Book> books = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("publisher"),
                        convertSqlDateToUtilDate(rs.getDate("publication_date")),
                        rs.getInt("stock_quantity"),
                        rs.getString("category"),
                        rs.getDouble("price")
                ));
            }
        }
        return books;
    }

    private Date convertSqlDateToUtilDate(java.sql.Date sqlDate) {
        return sqlDate != null ? new Date(sqlDate.getTime()) : null;
    }
}
