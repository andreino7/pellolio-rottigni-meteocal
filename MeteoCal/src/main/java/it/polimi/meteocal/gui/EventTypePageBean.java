/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui;

import it.polimi.meteocal.entity.EventType;
import it.polimi.meteocal.boundary.EventTypeManager;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Filippo
 */
@Named(value = "eventTypePageBean")
@ViewScoped
public class EventTypePageBean implements Serializable{

    @EJB
    private EventTypeManager evManager;
    
    private String param;
    
    private EventType eventType;
    private boolean mine;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }
    
    /**
     * Creates a new instance of EventTypePageBean
     */
    public EventTypePageBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        initParam();
        this.eventType=evManager.findEventTypeforId(Integer.parseInt(param));
        checkPermissions();
    }
    
     public void initParam() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        param = request.getParameter("id");

    }

    private void checkPermissions() {
       if (eventType.getPersonalized()){
           if(evManager.isMine(eventType)){
               mine= true;
           }
       }
    }
    
    public String converToText(boolean b){
        if (b){
            return "Allowed";
        }
        return "Not Allowed";
    }

    public void save(){
        evManager.update(eventType);
    }
    
    public void reset(){
        this.eventType=evManager.findEventTypeforId(eventType.getId());
    }
    
}
