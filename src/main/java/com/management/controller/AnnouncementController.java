package com.management.controller;

import com.management.dao.AnnouncementDAO;
import com.management.model.Announcement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnnouncementController {
    private AnnouncementDAO announcementDAO;
    private List<Announcement> announcementList;
    private static final Logger logger = Logger.getLogger(AnnouncementController.class.getName());

    public AnnouncementController(AnnouncementDAO announcementDAO) {
        this.announcementDAO = announcementDAO;
        announcementList = new ArrayList<>();
        loadAnnouncementsFromDatabase();
    }

    // 获取所有公告
    public List<Announcement> getAllAnnouncements() {
        return announcementList;
    }

    // 添加公告
    public void addAnnouncement(Announcement announcement) {
        announcementList.add(announcement);
        announcementDAO.addAnnouncement(announcement);
    }

    // 编辑公告
    public void editAnnouncement(Announcement updatedAnnouncement) {
        for (Announcement announcement : announcementList) {
            if (announcement.getId() == updatedAnnouncement.getId()) {
                announcement.setTitle(updatedAnnouncement.getTitle());
                announcement.setContent(updatedAnnouncement.getContent());
                announcement.setPublishDate(updatedAnnouncement.getPublishDate());
                announcementDAO.editAnnouncement(updatedAnnouncement);
                break;
            }
        }
    }

    // 删除公告
    public void deleteAnnouncement(int id) {
        announcementList.removeIf(announcement -> announcement.getId() == id);
        announcementDAO.deleteAnnouncement(id);
    }

    // 根据ID查找公告
    public Announcement getAnnouncementById(int id) {
        for (Announcement announcement : announcementList) {
            if (announcement.getId() == id) {
                return announcement;
            }
        }
        return announcementDAO.getAnnouncementById(id);
    }

    // 从数据库加载公告数据
    private void loadAnnouncementsFromDatabase() {
        announcementList = announcementDAO.getAllAnnouncements();
    }
}
