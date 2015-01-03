/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.schedule;

import it.polimi.meteocal.entity.EventType;
import it.polimi.meteocal.security.EventTypeManager;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Filippo
 */
@FacesConverter("EventTypeConverter")
public class EventTypeConverter implements Converter{

    @EJB
    EventTypeManager typeManager;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {  
        if (value == null || value.isEmpty()) {
            return null;
        } 
        return typeManager.findEventTypeforId(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || !(value instanceof EventType)) {
            return "";
        }  
        return ((EventType) value).getId().toString();
    }
    
}
