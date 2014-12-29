/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

import it.polimi.meteocal.entity.Event;

/**
 *
 * @author Filippo
 */
public interface Notification {
    
    public String getText();
    public Event getAbout();
    public String getState();
    public void setState(String state);
    public Integer getId();
}
