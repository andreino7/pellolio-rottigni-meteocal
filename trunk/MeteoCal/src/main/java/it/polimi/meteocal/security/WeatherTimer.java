/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.WeatherNotification;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Schedule;
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
       cal.setTime(new Date());
       cal.set(Calendar.HOUR_OF_DAY, 00);
       cal.set(Calendar.MINUTE, 00);
       cal.set(Calendar.SECOND, 00);
       cal.set(Calendar.MILLISECOND, 00);
       return cal;
   }
   
   private List<Event> findEventToCheck(Date d1, Date d2) {
       return eventManager.findByDay(d1,d2);
   }
   
//   @Schedule(second = "0", minute = "0", hour = "*/00", persistent = false)
   private void checkWeather() {
       System.out.println("timeout");
       Calendar cal = Calendar.getInstance();
       calendarSetUp(cal);
       cal.add(Calendar.DATE, 3);
       Date d1 = cal.getTime();
       cal.add(Calendar.DATE, 1);
       Date d2 = cal.getTime();
       List<Event> events = findEventToCheck(d1, d2);
       for (Event e: events) {
            Map<Date, WeatherConditions> forecast = weather.getForecast(e.getLocation());
            e.setWeather(forecast.get(d1).toString());
            List<String> allowed = e.getType().getAllowedCondition();
            if (!allowed.contains(e.getWeather())) {
                Date d = lookForOkDay(allowed, forecast, d2);
                createOwnerWeatherNotification(e, d);
            }
       }
   }

    private Date lookForOkDay(List<String> allowed, Map<Date, WeatherConditions> forecast, Date date) {
        for (Date d: forecast.keySet()) {
            if (d.after(date) && allowed.contains(forecast.get(d).toString())) {
                return d;
            }
        }
        return null;
    }

    private void createOwnerWeatherNotification(Event e, Date d) {
        WeatherNotification wn = new WeatherNotification();
        wn.setState("PENDING");
        wn.setId(-1);
        wn.setReceiver(e.getEventOwner());
        wn.setAbout(e);
        wn.setSuggestedDate(d);
        notificationManager.createWeatherNotification(wn);
    }

}
