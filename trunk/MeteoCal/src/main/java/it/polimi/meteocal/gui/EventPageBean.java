/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui;

import it.polimi.meteocal.weather.DateManipulator;
import it.polimi.meteocal.boundary.EventCalendarManager;
import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.EventCalendar;
import it.polimi.meteocal.entity.EventType;
import it.polimi.meteocal.entity.InviteNotification;
import it.polimi.meteocal.entity.ResponseNotification;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.entity.WeatherNotification;
import it.polimi.meteocal.boundary.CalendarManager;
import it.polimi.meteocal.boundary.EventManager;
import it.polimi.meteocal.boundary.EventTypeManager;
import it.polimi.meteocal.weather.Forecast;
import it.polimi.meteocal.interfaces.Notification;
import it.polimi.meteocal.boundary.NotificationManager;
import it.polimi.meteocal.notification.NotificationStatus;
import it.polimi.meteocal.notification.NotificationType;
import it.polimi.meteocal.boundary.UserManager;
import it.polimi.meteocal.schedule.Visibility;
import it.polimi.meteocal.weather.WeatherChecker;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Filippo
 */
@Named(value = "eventPageBean")
@ViewScoped
public class EventPageBean implements Serializable {

    @EJB
    EventManager eventManager;

    @EJB
    EventTypeManager eventTypeManager;

    @EJB
    WeatherChecker weather;

    @EJB
    CalendarManager calendarManager;

    @EJB
    NotificationManager notifManager;

    @EJB
    UserManager userManager;

    @EJB
    EventCalendarManager ecManager;

    private final List<String> visibilities = new LinkedList<>();
    private String param;
    private String param2;
    private boolean ownedEvent;
    private Event event;
    private List<User> participant;
    private List<User> toInvite;
    private List<EventType> userTypes;
    private String geoLoc;
    private boolean InvitePermission;
    private List<Forecast> forecasts;
    private Notification notification;
    private boolean weatherNotification;
    private boolean inviteNotification;
    private boolean responseNotification;
    private boolean changedEventNotification;
    private boolean adminNotification;
    private Date suggestedDate;
    private Forecast suggestedWeather;
    private Calendar calendar;
    private NotificationType notificationType;
    private boolean presentInMyCalendar;
    private List<Calendar> calendars;
    private boolean notAnsweredYet;
    
    
    /**
     * Creates a new instance of eventPageBean
     */
    public EventPageBean() {
    }

    public String getParam() {
        return param;
    }

    
    public boolean isInvitePermission() {
        return InvitePermission;
    }

    public String getGeoLoc() {
        geoLoc = event.getLocation();
        return geoLoc;
    }

    public void setGeoLoc(String geoLoc) {
        this.geoLoc = geoLoc;

    }

    public boolean isWeatherNotification() {
        return weatherNotification;
    }

    public boolean isInviteNotification() {
        return inviteNotification;
    }

    public boolean isResponseNotification() {
        return responseNotification;
    }

    public boolean isChangedEventNotification() {
        return changedEventNotification;
    }

    public boolean isAdminNotification() {
        return adminNotification;
    }

    public Date getSuggestedDate() {
        return suggestedDate;
    }

    public Forecast getSuggestedWeather() {
        return suggestedWeather;
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

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    public List<Calendar> getCalendars() {
        return calendars;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public boolean isPresentInMyCalendar() {
        return presentInMyCalendar;
    }

    public boolean isThereSuggestedDate() {
        return suggestedDate != null;
    }

    public boolean isNotAnsweredYet() {
        return notAnsweredYet;
    }



    @PostConstruct
    public void postConstruct() {
        System.out.println("EventBEan Created");
        initParam();
        this.event = eventManager.findEventForId(param);
        System.out.println(event);
        ownedEvent = (eventManager.isMyEvent(param)&& !(event.getDate().before(new Date())));
        participant = eventManager.getParticipant(event);
        userTypes = eventTypeManager.findTypesForUser();
        visibilities.add(Visibility.Private);
        visibilities.add(Visibility.Public);
        InvitePermission = eventManager.invitePermission(event);
        calendars = calendarManager.findCalendarForUser(userManager.getLoggedUser());
        updatePresentInMyCalendar();
        checkIfAlreadyAnsewerd();
        if (isNotFaraway()) {
            forecasts = weather.getWeatherForecast(event.getLocation());
            if (forecasts != null) {
                updateSuggestedDate();
                updateWeather();
            }
        }
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

    public void save() {
        eventManager.update(event);
    }

    public void reset() {
        event = eventManager.findEventForId(event.getId());
    }

    public String joinEvent() {
        EventCalendar eventCal = new EventCalendar();
        eventCal.setId(-1);
        eventCal.setEvent(event);
        eventCal.setCalendar(calendar);
        ecManager.save(eventCal);
        notifManager.createResponseNotification(setResponseNotificationParameter(true));
        return "home?faces-redirect=true";
    }

    public String declineInvite() {
        notifManager.createResponseNotification(setResponseNotificationParameter(false));
        return "home?faces-redirect=true";
    }

    public String postpone() {
        System.out.println("postpone: " + suggestedDate);
        if (suggestedDate != null) {
            Date endDate = DateManipulator.toNewEndDate(event.getDate(), suggestedDate, event.getEndDate());
            event.setDate(suggestedDate);
            event.setEndDate(endDate);
            eventManager.update(event);
            notifManager.createChangedEventNotification(event, participant);
            notifManager.removeWeatherNotification((WeatherNotification) notification);
        }
        return "home?faces-redirect=true";
    }

    private void updateWeather() {
        System.out.println("update weather");
        Date eventDate = DateManipulator.toDefaultDate(event.getDate());
        System.out.println(eventDate);
        for (Forecast f : forecasts) {
            Date forecastDate = DateManipulator.toDefaultDate(f.getDate());
            if (forecastDate.equals(eventDate)) {
                System.out.println("if");
                event.setWeather(f.getCondition().toUpperCase());
                eventManager.update(event);
            }
        }
    }

    private void initParam() {
        HttpServletRequest request = getServletRequest();
        param = request.getParameter("id");
        String notType = request.getParameter("notificationType");
        if (notType != null) {
            notificationType = NotificationType.valueOf(notType);
            notification = notifManager.findNotificationByIdAndType(request.getParameter("notificationID"), notificationType);
            if (notification != null) {
                checkNotifParam();
                notification.setState("READ");
                notifManager.updateNotification(notification, notificationType);
            }
        }
    }
    
    private HttpServletRequest getServletRequest() {
        if (FacesContext.getCurrentInstance() != null) {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            System.out.println("not null");
            return request;
        } else {
            throw new NullPointerException("Invalid faces context");
        }
    }
    
    private void checkNotifParam() {
        switch (notificationType) {
            case WEATHER:
                weatherNotification = true;
                break;
            case INVITE:
                inviteNotification = true;
                break;
            case RESPONSE:
                responseNotification = true;
                break;
            case CHANGED:
                changedEventNotification = true;
                break;
            case ADMIN:
                adminNotification = true;
                break;
        }
    }

    private void updateSuggestedDate() {
        if (weatherNotification && ownedEvent) {
            System.out.println("update suggested date");
            this.suggestedDate = ((WeatherNotification) notification).getSuggestedDate();
            if (suggestedDate != null) {
                System.out.println("suggested date!=null");
                this.suggestedWeather = getWeather(this.suggestedDate);
            } else {
                System.out.println("suggested date=null");
            }
        }
    }

    private Forecast getWeather(Date suggestedDate) {
        Date d = DateManipulator.toDefaultDate(suggestedDate);
        System.out.println(d);
        for (Forecast f : forecasts) {
            if (d.equals(f.getDate())) {
                System.out.println("data trovata");
                return f;
            }
        }
        System.out.println("forecast=null");
        return null;
    }

    private ResponseNotification setResponseNotificationParameter(Boolean response) {
        ResponseNotification respNotification = new ResponseNotification();
        InviteNotification invite = (InviteNotification) notification;
        respNotification.setId(0);
        respNotification.setAbout(event);
        respNotification.setAnswer(response);
        respNotification.setRefers(invite);
        respNotification.setReceiver(invite.getSender());
        respNotification.setState(NotificationStatus.UNREADSEEN.toString());
        respNotification.setSender(userManager.getLoggedUser());
        return respNotification;
    }

    private void updatePresentInMyCalendar() {
        this.presentInMyCalendar = ecManager.findAllEventForCalendars(calendars).contains(event);
    }

    private boolean isNotFaraway() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(java.util.Calendar.DATE, 11);
        Date d = cal.getTime();
        return event.getDate().after(new Date()) && event.getDate().before(d);
    }

    private void checkIfAlreadyAnsewerd() {
        if (inviteNotification) {
            notAnsweredYet = notifManager.notAnswered(event, userManager.getLoggedUser());
        }
    }

}
