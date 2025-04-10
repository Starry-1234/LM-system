package com.management.model;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private Date publicationDate;
    private int stock_Quantity;
    private String category;
    private double price;

    // 用于存储被修改的字段及其值
    private Map<String, Object> modifiedFields = new HashMap<>();

    // 默认构造函数
    public Book() {}

    // 带参数的构造函数
    public Book(int id, String title, String author, String isbn, String publisher, Date publicationDate, int stock_Quantity, String category, double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.stock_Quantity = stock_Quantity;
        this.category = category;
        this.price = price;
    }

    // 方法用于设置字段并记录修改
    public void setTitle(String title) {
        this.title = title;
        modifiedFields.put("title", title);
    }

    public void setAuthor(String author) {
        this.author = author;
        modifiedFields.put("author", author);
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
        modifiedFields.put("isbn", isbn);
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
        modifiedFields.put("publisher", publisher);
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
        modifiedFields.put("publication_date", publicationDate);
    }

    public void setStock_Quantity(int stock_Quantity) {
        this.stock_Quantity = stock_Quantity;
        modifiedFields.put("stock_quantity", stock_Quantity);
    }

    public void setCategory(String category) {
        this.category = category;
        modifiedFields.put("category", category);
    }

    public void setPrice(double price) {
        this.price = price;
        modifiedFields.put("price", price);
    }

    // 获取被修改的字段及其值
    public Map<String, Object> getModifiedFields() {
        return modifiedFields;
    }
}
