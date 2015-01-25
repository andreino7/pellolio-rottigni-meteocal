/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.notification;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author Andrea
 */
@Singleton
@Startup
public class StartUpOperation {
    @EJB
    NotificationCleaner cleaner;
    
    @PostConstruct
    public void cleanNotification() {
        cleaner.cleanAll();
    }
}
