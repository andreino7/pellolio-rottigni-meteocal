/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui;

import it.polimi.meteocal.boundary.UserManager;
import it.polimi.meteocal.entity.User;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Andrea
 */
public class RegistrationBeanTest {
    
    public RegistrationBeanTest() {
    }

    private RegistrationBean regBean;
    
    @Before
    public void setUp() {
        regBean = new RegistrationBean();
        regBean.um = mock(UserManager.class);
        User u = mock(User.class);
        regBean.setUser(u);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of register method, of class RegistrationBean.
     */
    @Test
    public void testRegister() {
        when(regBean.um.notExist(regBean.getUser())).thenReturn(true);
        assertEquals("user/home?faces-redirect=true", regBean.register());
    }
    

    
}
