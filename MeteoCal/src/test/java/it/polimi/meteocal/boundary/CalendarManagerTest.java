/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.boundary;

import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Filippo
 */
public class CalendarManagerTest {
    CalendarManager calMan;
    @Before
    public void setUp() {
        calMan= new CalendarManager();
        calMan.em= mock(EntityManager.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void badCall(){
        calMan.findCalendarForId(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void badUserCall(){
        calMan.findCalendarForUser(null);
    }
    
    
}
