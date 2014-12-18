/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.schedule;

import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.security.EventManager;
import it.polimi.meteocal.security.UserManager;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Filippo
 */
@Named(value = "eventPageBean")
@RequestScoped
public class eventPageBean {
    
    @EJB
    private EventManager eventManager;

    
    
    private String param;
    private boolean ownedEvent;
    private Event event;
    private List<User> participant;

    public List<User> getParticipant() {
        return participant;
    }

    public void setParticipant(List<User> participant) {
        this.participant = participant;
    }

    public boolean isOwnedEvent() {
        return ownedEvent;
    }

    public void setOwnedEvent(boolean ownedEvent) {
        this.ownedEvent = ownedEvent;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    /**
     * Creates a new instance of eventPageBean
     */
    
    
    
    public eventPageBean() {
    }
    @PostConstruct
    public void postConstruct() {
        initParam();
        this.event=eventManager.findEventForId(param);
        ownedEvent = eventManager.isMyEvent(param);
        participant= eventManager.getParticipant(event);
    }
    
    public void initParam() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        param = request.getParameter("id");

    }
    
}
