/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.schedule;

import it.polimi.meteocal.entity.EventType;
import it.polimi.meteocal.security.EventTypeManager;
import it.polimi.meteocal.security.NotificationManager;
import it.polimi.meteocal.security.UserManager;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

/**
 *
 * @author Filippo
 */
@Named(value = "adminHomeBean")
@SessionScoped
public class AdminHomeBean implements Serializable {
    private final static Integer newInDB=0;

    @EJB
    private UserManager userManager;
    
    @EJB
    private NotificationManager notificationManager;
    
    @EJB
    private EventTypeManager etManager;
    
    private List<EventType> eventTypes;
    private EventType selected;

    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    public EventType getSelected() {
        return selected;
    }

    public void setSelected(EventType selected) {
        this.selected = selected;
    }

    public void setEventTypes(List<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }
    /**
     * Creates a new instance of AdminHomeBean
     */
    
    
    public AdminHomeBean() {
    }
    @PostConstruct
    private void postConstruct(){
        eventTypes= etManager.findDefaultTypes();
        selected=new EventType(newInDB);
        selected.setTitle("Name");

    }
    
    public void save(){
        if (selected.getId()!=newInDB){
            etManager.update(selected);
            notificationManager.createAdminNotification(selected);
        }else{
            etManager.save(selected);
        }
    }
    
    public void reset(){
        if (selected.getId()!=newInDB){
            selected=etManager.findEventTypeforId(selected.getId());
        }else{
            selected= new EventType(newInDB);
            selected.setTitle("Name");
        }
    }
    
    public void create(){
        selected=new EventType(newInDB);
        selected.setTitle("Name");

    }
    
    
    
}
