package com.management.dao;

import com.management.model.Announcement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnnouncementDAO {
    private Connection connection;

    public AnnouncementDAO(Connection connection) {
        this.connection = connection;
    }

    // 获取所有公告
    public List<Announcement> getAllAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();
        String sql = "SELECT * FROM announcement";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Announcement announcement = new Announcement();
                announcement.setId(rs.getInt("id"));
                announcement.setTitle(rs.getString("title"));
                announcement.setContent(rs.getString("content"));
                announcement.setPublishDate(rs.getDate("publishDate"));
                announcements.add(announcement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return announcements;
    }

    // 添加公告
    public void addAnnouncement(Announcement announcement) {
        String sql = "INSERT INTO announcement (title, content, publishDate) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, announcement.getTitle());
            pstmt.setString(2, announcement.getContent());
            pstmt.setDate(3, new java.sql.Date(announcement.getPublishDate().getTime()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 编辑公告
    public void editAnnouncement(Announcement updatedAnnouncement) {
        String sql = "UPDATE announcement SET title = ?, content = ?, publishDate = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, updatedAnnouncement.getTitle());
            pstmt.setString(2, updatedAnnouncement.getContent());
            pstmt.setDate(3, new java.sql.Date(updatedAnnouncement.getPublishDate().getTime()));
            pstmt.setInt(4, updatedAnnouncement.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除公告
    public void deleteAnnouncement(int id) {
        String sql = "DELETE FROM announcement WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 根据ID查找公告
    public Announcement getAnnouncementById(int id) {
        String sql = "SELECT * FROM announcement WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Announcement announcement = new Announcement();
                announcement.setId(rs.getInt("id"));
                announcement.setTitle(rs.getString("title"));
                announcement.setContent(rs.getString("content"));
                announcement.setPublishDate(rs.getDate("publishDate"));
                return announcement;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
