/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.weather;

import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.entity.WeatherNotification;
import it.polimi.meteocal.boundary.EventManager;
import it.polimi.meteocal.boundary.NotificationManager;
import it.polimi.meteocal.notification.NotificationStatus;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;


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
    @Resource
    private TimerService timerService;

    
 /*   private Calendar calendarSetUp(Calendar cal) {
        cal.setTime(DateManipulator.toDefaultDate(new Date()));
        return cal;
    }

    private List<Event> findEventToCheck(Date d1, Date d2) {
        return eventManager.findByDay(d1, d2);
    } */

    private Date lookForOkDay(List<String> allowed, Map<Date, WeatherConditions> forecast, Date date, Event e) {
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
/*

    @Schedule (dayOfWeek="*", minute="0", second="0", hour="1", timezone="GMT")
    public void checkWeather() {
        checkWeather(3, 1, false);
        checkWeather(0, 1, true);
    }

    private void checkWeather(int firstDay, int secondDay, boolean forAll) {
        System.out.println("checking weather");
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
    } */
    
    private void checkWeather(Event e, boolean forAll) {
            Map<Date, WeatherConditions> forecast = weather.getForecast(e.getLocation());
            if (forecast != null) {
                e.setWeather(forecast.get(DateManipulator.toDefaultDate(e.getDate())).toString());
                List<String> allowed = e.getType().getAllowedCondition();
                if (!allowed.contains(e.getWeather())) {
                    Date d = lookForOkDay(allowed, forecast, DateManipulator.toDefaultDate(e.getDate()), e);
                    createNotifications(e, d, forAll);
                }
        }
    }

    private void createNotifications(Event e, Date d, boolean forAll) {
        if (forAll) {
            List<User> participants = eventManager.getParticipant(e);
            for (User u : participants) {
                deleteOldWeatherNot(u,e);
                setNotificationParam(u, e, d);
            }
        } else {
            deleteOldWeatherNot(e.getEventOwner(),e);
            setNotificationParam(e.getEventOwner(), e, d);
        }
    }

    private void setNotificationParam(User u, Event e, Date d) {
        System.out.println("set not par: "+ d);
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
    
    public void setTimer(Integer id, Date date) {
        System.out.println("setting timer for weather");
        timerService.createSingleActionTimer(date, new TimerConfig(id,false));
    }
    
    @Timeout
    public void programmaticTimeout(Timer timer) {
        System.out.println("timeout");
        if (timer.getInfo() instanceof Integer) {
            System.out.println("integer");
            Integer id = (Integer) timer.getInfo();
            System.out.println(id);
            Event ev = eventManager.findEventForId(id);
            if (ev != null) {
                if(DateManipulator.toDefaultDate(ev.getDate()).equals(DateManipulator.toDefaultDate(DateManipulator.addDays(new Date(), 3)))) {
                    System.out.println("prog timeout 1");
                    checkWeather(ev, false);
                } else {
                    if(DateManipulator.toDefaultDate(ev.getDate()).equals(DateManipulator.toDefaultDate(DateManipulator.addDays(new Date(), 1)))) {
                        checkWeather(ev, true);
                    }
                }
            }
        }
    }

    private void deleteOldWeatherNot(User u, Event e) {
        List<WeatherNotification> nots = notificationManager.findWeatherNotificationByReceiverAndEvent(e,u);
        for (WeatherNotification n: nots) {
            notificationManager.removeWeatherNotification(n);
        }
    }



}
