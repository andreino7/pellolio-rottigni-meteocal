/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.schedule;

import java.util.LinkedList;
import java.util.List;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Filippo
 */
public class MeteoCalScheduleModel extends DefaultScheduleModel{

    List<MeteoCalScheduleEvent> events= new LinkedList<>();
   
    public void addEvent(MeteoCalScheduleEvent event) {
        super.addEvent(event); 
        events.add(event);
    }
    
    public MeteoCalScheduleEvent getMeteoEvent(String id){
        for (MeteoCalScheduleEvent e:events){
            if (e.getId().equals(id)){
                return e;
            }
        }
        return null;
    }
 
    
   
}
