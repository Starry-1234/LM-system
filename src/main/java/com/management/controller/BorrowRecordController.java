package com.management.controller;

import com.management.dao.BorrowRecordDAO;
import com.management.model.BorrowRecord;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class BorrowRecordController {

    private BorrowRecordDAO borrowRecordDAO;

    public BorrowRecordController() {

        Properties properties = new Properties();
        try {
            // 使用类加载器读取 application.properties 文件
            properties.load(getClass().getClassLoader().getResourceAsStream("db.properties"));

            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setUrl(properties.getProperty("db.url"));
            dataSource.setUsername(properties.getProperty("db.username"));
            dataSource.setPassword(properties.getProperty("db.password"));
            dataSource.setDriverClassName(properties.getProperty("db.driver"));

            Connection conn = dataSource.getConnection();
            borrowRecordDAO = new BorrowRecordDAO(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取所有借阅记录
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordDAO.getAllBorrowRecords();
    }

    // 根据借阅者姓名搜索借阅记录
    public List<BorrowRecord> getBorrowRecordsByBorrower(String borrowerName) {
        return borrowRecordDAO.searchBorrowRecordsByBorrower(borrowerName);
    }

    // 根据图书名称搜索借阅记录
    public List<BorrowRecord> getBorrowRecordsByBookName(String bookName) {
        return borrowRecordDAO.searchBorrowRecordsByBookName(bookName);
    }

    // 根据ISBN搜索借阅记录
    public List<BorrowRecord> getBorrowRecordsByIsbn(String isbn) {
        return borrowRecordDAO.searchBorrowRecordsByisbn(isbn);
    }

}
