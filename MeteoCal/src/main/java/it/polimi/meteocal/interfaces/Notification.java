/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.interfaces;

import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.notification.NotificationType;
import java.util.Date;

/**
 *
 * @author Filippo
 */
public interface Notification extends Comparable<Notification> {
    
    public String getText();
    public Event getAbout();
    public String getState();
    public void setState(String state);
    public Integer getId();
    public NotificationType getType();
    public Date getCreationDate();
}
