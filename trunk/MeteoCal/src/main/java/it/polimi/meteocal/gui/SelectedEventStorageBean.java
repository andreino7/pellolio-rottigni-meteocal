/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui;

import javax.ejb.Stateful;

/**
 *
 * @author Filippo
 */
@Stateful
public class SelectedEventStorageBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    String eventId;
    
    public void store(String id){
        eventId=id;
    }
    
    public String retrieve(){
        return eventId;
    }
}
