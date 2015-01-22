/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.notification;

import it.polimi.meteocal.boundary.EventManager;
import it.polimi.meteocal.boundary.NotificationManager;
import it.polimi.meteocal.entity.Event;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
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
public class NotificationCleaner {
    
    @Resource
    private TimerService timerService;
    
    @EJB
    private EventManager eventManager;
    
    @EJB
    private NotificationManager notificationManager;

    public void setTimer(Integer id, Date endDate) {
        System.out.println("setting timer");
        timerService.createSingleActionTimer(endDate, new TimerConfig(id,false));
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
                if(ev.getEndDate().before(new Date())) {
                    System.out.println("removing");
                    notificationManager.removeAllForEvent(ev);
                } else {
                    setTimer(ev.getId(), ev.getEndDate());
                }
            }
        }
    }
    
 /*   public void cleanAll() {
        List<Event> events = eventManager.finByEndDate(Date endDate);
    } */

    
}

