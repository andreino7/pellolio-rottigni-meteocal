/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui;

import it.polimi.meteocal.schedule.Visibility;
import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.entity.ChangedEventNotification;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.EventCalendar;
import it.polimi.meteocal.entity.EventType;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.boundary.CalendarManager;
import it.polimi.meteocal.boundary.EventManager;
import it.polimi.meteocal.notification.NotificationCleaner;
import it.polimi.meteocal.boundary.NotificationManager;
import it.polimi.meteocal.boundary.UserManager;
import it.polimi.meteocal.schedule.MeteoCalScheduleEvent;
import it.polimi.meteocal.schedule.MeteoCalScheduleModel;
import it.polimi.meteocal.weather.WeatherChecker;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Filippo
 */
@Named(value = "scheduleBean")
@SessionScoped
public class ScheduleBean implements Serializable {

    private static final Integer eventNotInDB = 0;
    private static final int maxCalendars=8;
    private MeteoCalScheduleModel model;
    private User user;
    private boolean scheduleDisplay=true;
    
    @EJB
    private UserManager userManager;

    @EJB
    private CalendarManager calendarManager;

    @EJB
    private EventManager eventManager;
    
    @EJB
    private NotificationManager notificationManager;

    @EJB
    private WeatherChecker weather;

    @EJB
    private EmailSessionBean emailBean;

    
    @EJB
    private NotificationCleaner cleaner;

    
    @PersistenceContext
    private EntityManager em;


    private Integer calendarId;
    private String geoLoc;

    private List<String> visibilities = new LinkedList<String>();
    private List<EventType> userTypes;
    private List<Calendar> userCalendars;
    private List<String> chosenCalendars;
    private List<Event> nextEvents;
    private MeteoCalScheduleEvent event = new MeteoCalScheduleEvent();
    private String title;
    private Calendar newCalendar;

    List<String> colorclass;
    Map<Integer, String> colorForCalendar;

    
    public List<Event> getNextEvents() {
        nextEvents=eventManager.findFutureEventsForCalendars(chosenCalendars);
        return nextEvents;
    }
    
    
    
    
    public boolean isScheduleDisplay() {
        return scheduleDisplay;
    }

    public void setScheduleDisplay(boolean scheduleDisplay) {
        this.scheduleDisplay = scheduleDisplay;
    }
    

    public Calendar getNewCalendar() {
        return newCalendar;
    }

    public void setNewCalendar(Calendar newCalendar) {
        this.newCalendar = newCalendar;
    }

    public String getGeoLoc() {
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

    public void setUserTypes(List<EventType> userTypes) {
        this.userTypes = userTypes;
    }

    public String getClassForCalendar(Calendar c) {
        return colorForCalendar.get(c.getId());
    }

    public String getColorBoxForCalendar(Calendar c) {
        return "<div class=\" colorbox " + getClassForCalendar(c) + "\" ></div>";
    }

    public List<String> getChosenCalendars() {
        return chosenCalendars;
    }

    public void setChosenCalendars(List<String> chosenCalendars) {
        this.chosenCalendars = chosenCalendars;
        updateScheduleModel();
        System.out.println("xxx");
    }

    public Integer getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Integer id) {
        Calendar c;
        c = (Calendar) em.createNamedQuery(Calendar.findById, Calendar.class).setParameter("id", id).getSingleResult();
        event.setCalendar(c);
    }

    public String getTitle() {
        if (event != null) {
            title = event.getTitle();
        }
        return title;
    }

    public List<Calendar> getUserCalendars() {

        return userCalendars;
    }

    public void setTitle(String title) {
        event.setTitle(title);
        System.out.println(title);

        this.title = title;
    }

    public ScheduleBean() {
        model = new MeteoCalScheduleModel();

        System.out.println("aaaaaaa");
    }

    public ScheduleModel getModel() {

        return model;
    }

    @PostConstruct
    private void postConstruct() {
        user = userManager.getLoggedUser();

        
        visibilities.add(Visibility.Private);
        visibilities.add(Visibility.Public);
        event = new MeteoCalScheduleEvent(eventNotInDB, "", null, null, null, null);
        
        userCalendars = (List<Calendar>) em.createNamedQuery(Calendar.findByOwner, Calendar.class).setParameter("ownerEmail", user.getEmail()).getResultList();
        userTypes = (List<EventType>) em.createNamedQuery(EventType.findAllTypesForUser, EventType.class).setParameter("user", user.getEmail()).getResultList();
        chosenCalendars=new LinkedList<>();
        
        int i=0;
        for (Calendar c: userCalendars){
            if (i<maxCalendars){
            chosenCalendars.add(c.getId().toString());
            i++;
            }
        }
        
        updateScheduleModel();
    }

    public MeteoCalScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = (MeteoCalScheduleEvent) event;
    }

    public void addEvent() {
        if (event.getLocation() != null) {
            this.save();
            if (event.getId() == null) {
                model.addEvent(event);

            } else {
                model.updateEvent(event);
            }
        } else {
            System.out.println("No Location");
        }
        event = new MeteoCalScheduleEvent(); //reset dialog form
    }

    public void onEventSelect(SelectEvent e) {
        System.out.println("Event Selected");
        ScheduleEvent ev = (ScheduleEvent) e.getObject();

        event = model.getMeteoEvent(ev.getId());
        geoLoc = event.getLocation();

    }

    public void onDateSelect(SelectEvent e) {
        Date date = (Date) e.getObject();
        event = new MeteoCalScheduleEvent(eventNotInDB, "", date, date, null, null);
        geoLoc = "";
    }

    public void onGeolocation() {
        String City;
        City = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("City");
        String Country;
        Country = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("Country");

        event.setLocation(City + "," + Country);

    }

    public void updateScheduleModel() {
        model = new MeteoCalScheduleModel();
        colorclass = new LinkedList<>(Arrays.asList("red", "green", "aqua", "blue", "yellow", "sienna", "violet", "purple"));
        colorForCalendar = new HashMap<>();
        Random r = new Random();

        if (chosenCalendars != null) {
            for (String c : chosenCalendars) {
                Integer colid = r.nextInt(colorclass.size());
                colorForCalendar.put(Integer.parseInt(c), colorclass.get(colid));
                colorclass.remove(colorclass.get(colid));

                List<Event> evList = (List<Event>) em.createNamedQuery(EventCalendar.findEventsForCalendar, Event.class).setParameter("calendar", Integer.parseInt(c)).getResultList();

                for (Event ev : evList) {
                    MeteoCalScheduleEvent scheduleEvent = new MeteoCalScheduleEvent(ev, calendarManager.findCalendarForId(c));
                    scheduleEvent.setStyleClass(ev.getWeather() + " " + colorForCalendar.get(Integer.parseInt(c)));
                    scheduleEvent.setDescription(ev.getWeather());

                    model.addEvent(scheduleEvent);
                }
            }
        }
        //FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("schedule");

    }

    public void save() {
        Event ev;
        if (eventManager.findEventForId(event.getDbId()) != null) {
            ev = eventManager.findEventForId(event.getDbId());
            ev.setId(event.getDbId());
            ev.setTitle(event.getTitle());
            ev.setDate(event.getStartDate());
            ev.setEndDate(event.getEndDate());
            ev.setType(event.getType());
            ev.setLocation(event.getLocation());
            ev.setVisibility(event.getVisibility());
            ev.setWeather(weather.addWeather(event.getLocation(), event.getStartDate()).toString());
            eventManager.update(ev);
            if (event.getCalendar() != event.getOld()) {
                eventManager.linkToCalendar(ev, event.getCalendar());
                eventManager.toggleLink(ev, event.getOld());
            }
            sendChangeEventNotification(ev);
        } else {
            //TODO location and visibility
            ev = new Event(event.getDbId(), event.getTitle(), "", event.getStartDate(), event.getEndDate(), "");
            ev.setType(event.getType());
            ev.setLocation(event.getLocation());
            ev.setVisibility(event.getVisibility());
            ev.setWeather(weather.addWeather(event.getLocation(), event.getStartDate()).toString());
            ev.setEventOwner(user);
            eventManager.save(ev);
            eventManager.linkToCalendar(ev, event.getCalendar());
            cleaner.setTimer(ev.getId(), ev.getEndDate());
        }
    }

    public void newCalendar() {
        this.newCalendar = new Calendar();
        newCalendar.setOwner(user);
    }

    public void saveNewCalendar() {
        calendarManager.save(newCalendar);
        userCalendars = (List<Calendar>) em.createNamedQuery(Calendar.findByOwner, Calendar.class).setParameter("ownerEmail", user.getEmail()).getResultList();
        this.newCalendar=new Calendar();
    }


    private void sendChangeEventNotification(Event ev) {
        List<User> partecipant = eventManager.getParticipant(ev);
        notificationManager.createChangedEventNotification(ev, partecipant);
    }
    
    
    public void switchView(){
        scheduleDisplay=!scheduleDisplay;
    }

    
    

}
