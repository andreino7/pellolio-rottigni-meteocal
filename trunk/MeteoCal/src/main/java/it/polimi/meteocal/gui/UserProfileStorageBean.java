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
public class UserProfileStorageBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
     String userId;
    
    public void store(String id){
        userId=id;
    }
    
    public String retrieve(){
        return userId;
    }
}
