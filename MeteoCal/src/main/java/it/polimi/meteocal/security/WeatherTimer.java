/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.entity.WeatherNotification;
import it.polimi.meteocal.schedule.DateManipulator;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Singleton;


/**
 *
 * @author Andrea
 */
@Singleton
public class WeatherTimer {

    @EJB
    private WeatherChecker weather;
    @EJB
    private EventManager eventManager;
    @EJB
    private NotificationManager notificationManager;

    
    private Calendar calendarSetUp(Calendar cal) {
        cal.setTime(DateManipulator.toDefaultDate(new Date()));
        return cal;
    }

    private List<Event> findEventToCheck(Date d1, Date d2) {
        return eventManager.findByDay(d1, d2);
    }

    private Date lookForOkDay(List<String> allowed, Map<Date, WeatherConditions> forecast, Date date, Event e) {
        //TODO change if conditions
        for (Date d : forecast.keySet()) {
            if (d.after(date) && allowed.contains(forecast.get(d).toString())) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(e.getDate());
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                int second = cal.get(Calendar.SECOND);
                cal.setTime(d);
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.SECOND, second);
                return cal.getTime();
            }
        }
        return null;
    }

    public void checkThreeDayWeather() {
        checkWeather(3, 1, false);
    }

    public void checkLastDayWeather() {
        checkWeather(0, 1, true);
    }

    //schedule
    private void checkWeather(int firstDay, int secondDay, boolean forAll) {
        Calendar cal = Calendar.getInstance();
        calendarSetUp(cal);
        cal.add(Calendar.DATE, firstDay);
        Date d1 = cal.getTime();
        cal.add(Calendar.DATE, secondDay);
        Date d2 = cal.getTime();
        List<Event> events = findEventToCheck(d1, d2);
        for (Event e : events) {
            Map<Date, WeatherConditions> forecast = weather.getForecast(e.getLocation());
            if (forecast != null) {
                e.setWeather(forecast.get(d1).toString());
                List<String> allowed = e.getType().getAllowedCondition();
                if (!allowed.contains(e.getWeather())) {
                    Date d = lookForOkDay(allowed, forecast, d2, e);
                    createNotifications(e, d, forAll);
                }
            }
        }
    }

    private void createNotifications(Event e, Date d, boolean forAll) {
        List<User> participants = eventManager.getNonParticipant(e);
        if (forAll) {
            for (User u : participants) {
                setNotificationParam(u, e, d);
            }
        } else {
            setNotificationParam(e.getEventOwner(), e, d);
        }
    }

    private void setNotificationParam(User u, Event e, Date d) {
        WeatherNotification wn = new WeatherNotification();
        wn.setState(NotificationStatus.UNREADSEEN.toString());
        wn.setId(1);
        wn.setReceiver(u);
        wn.setAbout(e);
        if (u.equals(e.getEventOwner())) {
            wn.setSuggestedDate(d);
        }
        notificationManager.createWeatherNotification(wn);
    }
}
