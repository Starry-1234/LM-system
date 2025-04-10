package com.management.dao;

import com.management.model.BorrowRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BorrowRecordDAO {
    private Connection conn;
    private static final Logger logger = Logger.getLogger(BorrowRecordDAO.class.getName());

    public BorrowRecordDAO(Connection conn) {
        this.conn = conn;
    }

    // 获取所有借阅记录
    public List<BorrowRecord> getAllBorrowRecords() {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        String sql = "SELECT * FROM library.BorrowRecord";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                        rs.getInt("id"),
                        rs.getString("isbn"),
                        rs.getString("book_name"),
                        rs.getString("borrower"),
                        rs.getDate("borrowing_time"),
                        rs.getDate("return_time")
                );
                borrowRecords.add(record);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "加载借阅记录数据失败：" + e.getMessage());
            e.printStackTrace();
        }
        return borrowRecords;
    }

    // 根据借阅者姓名搜索借阅记录
    public List<BorrowRecord> searchBorrowRecordsByBorrower(String borrower) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM library.BorrowRecord ");
        List<Object> params = new ArrayList<>();

        if (borrower != null && !borrower.isEmpty()) {
            sql.append(" WHERE borrower like ?");
            params.add("%" + borrower + "%");
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                        rs.getInt("id"),
                        rs.getString("isbn"),
                        rs.getString("book_name"),
                        rs.getString("borrower"),
                        rs.getDate("borrowing_time"),
                        rs.getDate("return_time")
                );
                borrowRecords.add(record);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "查询借阅记录数据失败：" + e.getMessage());
            e.printStackTrace();
        }
        return borrowRecords;
    }

    // 根据书名搜索借阅记录
    public List<BorrowRecord> searchBorrowRecordsByBookName(String bookName) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM library.BorrowRecord ");
        List<Object> params = new ArrayList<>();

        if (bookName != null && !bookName.isEmpty()) {
            sql.append(" WHERE book_name like ?");
            params.add("%" + bookName + "%");
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                        rs.getInt("id"),
                        rs.getString("isbn"),
                        rs.getString("book_name"),
                        rs.getString("borrower"),
                        rs.getDate("borrowing_time"),
                        rs.getDate("return_time")
                );
                borrowRecords.add(record);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "查询借阅记录数据失败：" + e.getMessage());
            e.printStackTrace();
        }
        return borrowRecords;
    }

    // 根据ISBN搜索借阅记录
    public List<BorrowRecord> searchBorrowRecordsByisbn(String isbn) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM library.BorrowRecord ");
        List<Object> params = new ArrayList<>();

        // 不为空时才作用
        if (isbn != null && !isbn.isEmpty()) {
            sql.append(" WHERE isbn like ?");
            params.add("%" + isbn + "%");
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                        rs.getInt("id"),
                        rs.getString("ISBN"),
                        rs.getString("book_name"),
                        rs.getString("Borrower"),
                        rs.getDate("Borrowing_time"),
                        rs.getDate("Return_time")
                );
                borrowRecords.add(record);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "查询借阅记录数据失败：" + e.getMessage());
            e.printStackTrace();
        }
        return borrowRecords;
    }

    // 添加借阅记录
    public void addBorrowRecord(BorrowRecord borrowRecord) {
        String sql = "INSERT INTO library.BorrowRecord (isbn, book_name, borrower, borrowing_time) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, borrowRecord.getISBN());
            pstmt.setString(2, borrowRecord.getBookName());
            pstmt.setString(3, borrowRecord.getBorrower());
            pstmt.setDate(4, new java.sql.Date(borrowRecord.getBorrowingTime().getTime()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "添加借阅记录失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}

