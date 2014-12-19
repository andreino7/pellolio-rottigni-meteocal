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
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Filippo
 */
@Named(value = "eventPageBean")
@ViewScoped
public class eventPageBean implements Serializable{

    @EJB
    private EventManager eventManager;

    private String param;
    private boolean ownedEvent;
    private Event event;
    private List<User> participant;
    private List<User> toInvite;

    public List<User> getToInvite() {
        return toInvite;
    }

    public void setToInvite(List<User> toInvite) {
        this.toInvite = toInvite;
    }

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
        System.out.println("EventBEan Created");
        initParam();
        this.event = eventManager.findEventForId(param);
        ownedEvent = eventManager.isMyEvent(param);
        participant = eventManager.getParticipant(event);
    }

    public void initParam() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        param = request.getParameter("id");

    }

    public List<User> complete(String query) {
        if (query != null & !query.isEmpty()) {
            while (!query.isEmpty() & query.charAt(0) == ' ') {
                query = query.substring(1);
            }
            return eventManager.getNonParticipantByPart(event, query);
        }
        return null;
    }
    
    public void sendInvites(){
        eventManager.inviteUsersToEvent(toInvite, event);
        toInvite=new LinkedList<>();
    }

}
