/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.schedule;

import it.polimi.meteocal.security.Notification;
import it.polimi.meteocal.security.NotificationManager;
import it.polimi.meteocal.security.UserManager;
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
    List<Notification> notif;

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
        
    }
    
    public int getNotificationNumber() {
        return notif.size();
    }
    
    public boolean isThereNotification() {
        return !notif.isEmpty();
    }
    
    
    /**
     * Creates a new instance of NotificationBean
     */
    public NotificationBean() {
        model=new DefaultMenuModel();
        
        
    }
    
    public void onItemSelect() {
        System.out.println("item select");
    }
    
    @PostConstruct
    public void postConstruct(){
        
        notif= notificationManager.getNotificationForUser(userManager.getLoggedUser());
        for (Notification n:notif){
            DefaultMenuItem item = new DefaultMenuItem(n.getText());
            item.setOutcome(n.getAbout().getPageLink()+"?id="+n.getAbout().getId());
            item.setParam("notificationID", n.getId());
            item.setParam("notificationType", n.getType());
            if ("UNREAD".equals(n.getState())) {
                item.setStyle("background: #F6E3CE; margin-top: -5px; margin-bottom: 5px; margin-left: -5px; width: 180px;");
            } else {
                item.setStyle("margin-top: -5px; margin-bottom: 5px; margin-left: -5px; width: 180px;");
            }
            model.addElement(item);
        }
    }
    
}
