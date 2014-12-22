/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.schedule;

import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.EventType;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.security.EventManager;
import it.polimi.meteocal.security.EventTypeManager;
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
public class eventPageBean implements Serializable {

    @EJB
    private EventManager eventManager;

    @EJB
    private EventTypeManager eventTypeManager;

    private List<String> visibilities = new LinkedList<String>();
    private String param;
    private boolean ownedEvent;
    private Event event;
    private List<User> participant;
    private List<User> toInvite;
    private List<EventType> userTypes;
    private String geoLoc;
    private boolean InvitePermission;

    public boolean isInvitePermission() {
        return InvitePermission;
    }

    public String getGeoLoc() {
        geoLoc=event.getLocation();
        return geoLoc;
    }

    public void setGeoLoc(String geoLoc) {
        this.geoLoc = geoLoc;
       
    }
    
    

    public List<String> getVisibilities() {
        return visibilities;
    }
    
    public List<EventType> getUserTypes() {
        return userTypes;
    }

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
        userTypes = eventTypeManager.findTypesForUser();
        visibilities.add(Visibility.Private);
        visibilities.add(Visibility.Public);
        InvitePermission=eventManager.invitePermission(event);
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

    public void sendInvites() {
        eventManager.inviteUsersToEvent(toInvite, event);
        toInvite = new LinkedList<>();
    }
    
     public void onGeolocation() {
        String City;
        City = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("City");
        String Country;
        Country = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("Country");

        event.setLocation(City + "," + Country);

    }
     
     public void save(){
         eventManager.update(event);
     }
     
     public void reset(){
         event=eventManager.findEventForId(event.getId());
     }

}
