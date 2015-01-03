/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.schedule;

import it.polimi.meteocal.entity.Calendar;
import it.polimi.meteocal.security.CalendarManager;
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
@FacesConverter("CalendarConverter")
public class CalendarConverter implements Converter {

    @EJB
    CalendarManager calManager;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            FacesMessage msg = new FacesMessage("prova");
            context.addMessage(component.getClientId(), msg);
            return null;
        }
        return calManager.findCalendarForId(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || !(value instanceof Calendar)) {
            return "";
        }
        return ((Calendar) value).getId().toString();
    }
    
}
