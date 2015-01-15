/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.schedule;

import it.polimi.meteocal.entity.ResponseNotification;
import it.polimi.meteocal.security.Notification;
import it.polimi.meteocal.security.NotificationManager;
import it.polimi.meteocal.security.NotificationStatus;
import it.polimi.meteocal.security.NotificationType;
import it.polimi.meteocal.security.UserManager;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author Filippo
 */
@Named(value = "notificationBean")
@RequestScoped
public class NotificationBean {

    @EJB
    NotificationManager notificationManager; 
    @EJB
    UserManager userManager;        
    
    MenuModel model;
    private List<Notification> notif;
    private final List<Notification> unReadSeen = new LinkedList<>();

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
        
    }
    
    public int getUnReadSeenNotificationNumber() {
        return unReadSeen.size();
    }
    
    public boolean isThereNotificationUnReadSeen() {
        return !unReadSeen.isEmpty();
    }
    
    public void updateUnReadSeen() {
        System.out.println("update");
        for (Notification n: notif) {
            if (n.getState().equals(NotificationStatus.UNREADSEEN.toString())) {
                n.setState(NotificationStatus.SEEN.toString());
                notificationManager.updateNotification(n, n.getType());
                updateUnReadSeenList();
            }
        }
    }
    
    /**
     * Creates a new instance of NotificationBean
     */
    public NotificationBean() {
        model=new DefaultMenuModel();
    }
    

    @PostConstruct
    public void postConstruct(){
        notif = notificationManager.getNotificationForUser(userManager.getLoggedUser());
        orderNotification();
        updateUnReadSeenList();
        for (Notification n : notif) {
            DefaultMenuItem item = getItemInstanceSetted(n);
            model.addElement(item);
        }
    }

    private void updateUnReadSeenList() {
        for (Notification n: notif) {
            if (n.getState().equals(NotificationStatus.UNREADSEEN.toString())) {
                unReadSeen.add(n);
            }
        }    
    }

    private DefaultMenuItem getItemInstanceSetted(Notification n) {
        DefaultMenuItem item = new DefaultMenuItem(n.getText());
        item.setOutcome(n.getAbout().getPageLink() + "?id=" + n.getAbout().getId());
        item.setParam("notificationID", n.getId());
        item.setParam("notificationType", n.getType());
        setItemStyle(item, n);
        return item;
    }

    private void orderNotification() {
        notif.sort(null);
        Collections.reverse(notif);
    }

    private void setItemStyle(DefaultMenuItem item, Notification n) {
        if (n.getType().equals(NotificationType.WEATHER)) {
            item.setIcon("notification"+n.getAbout().getWeather());
        } 
        setIcon(n, item);
        item.setIconPos("right");
        if (!NotificationStatus.READ.toString().equals(n.getState())) {
            item.setStyle("background: #F6E3CE; margin-top: -5px; margin-bottom: 5px; margin-left: -5px; width: 180px;");
        } else {
            item.setStyle("margin-top: -5px; margin-bottom: 5px; margin-left: -5px; width: 180px;");
        }
    }

    private void setIcon(Notification n, DefaultMenuItem item) {
        switch (n.getType()) {
            case WEATHER:
                item.setIcon("notification"+n.getAbout().getWeather());
                break;
            case INVITE:
                item.setIcon(NotificationType.INVITE.toString().toLowerCase());
                break;
            case ADMIN:
                item.setIcon(NotificationType.ADMIN.toString().toLowerCase());
                break;
            case RESPONSE:
                if (((ResponseNotification)n).getAnswer()) {
                    item.setIcon(NotificationType.RESPONSE.toString().toLowerCase()+"Yes");
                } else {
                    item.setIcon(NotificationType.RESPONSE.toString().toLowerCase()+"No");                    
                }
                break;
            case CHANGED:
                item.setIcon(NotificationType.CHANGED.toString().toLowerCase());
                break;                
        }
    }
    
}
