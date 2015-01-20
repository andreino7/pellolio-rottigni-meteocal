/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui;

import it.polimi.meteocal.boundary.UserManager;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.schedule.Visibility;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
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
public class LoginBeanTest {
    
    private LoginBean loginBean;
    private User user;
    
    public LoginBeanTest() {
    }
    
    
    @Before
    public void setUp() {
        loginBean = new LoginBean();
        loginBean.userManager = mock(UserManager.class);
        user = mock(User.class);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getEmail method, of class LoginBean.
     */






    /**
     * Test of login method, of class LoginBean.
     */
    @Test
    public void testAdminLogin() {
        loginBean.setEmail("aa");
        loginBean.setPassword("aa");
        HttpServletRequest request = mock(HttpServletRequest.class);
        FacesContext context = ContextMocker.mockFacesContext();
        ExternalContext extContext = mock(ExternalContext.class);
        when(loginBean.userManager.getLoggedUser()).thenReturn(user);
        when(context.getExternalContext()).thenReturn(extContext);
        when(extContext.getRequest()).thenReturn(request);
        when(user.getClearance()).thenReturn("ADMIN");
        assertEquals("/admin/adminHome.xhtml?faces-redirect=true", loginBean.login());
        
    }
    
    
    @Test
    public void testUserLogin() {
        loginBean.setEmail("aa");
        loginBean.setPassword("aa");
        HttpServletRequest request = mock(HttpServletRequest.class);
        FacesContext context = ContextMocker.mockFacesContext();
        ExternalContext extContext = mock(ExternalContext.class);
        when(loginBean.userManager.getLoggedUser()).thenReturn(user);
        when(context.getExternalContext()).thenReturn(extContext);
        when(extContext.getRequest()).thenReturn(request);
        when(user.getClearance()).thenReturn("USER");
        assertEquals("/user/home.xhtml?faces-redirect=true", loginBean.login());
        
    }

    /**
     * Test of logout method, of class LoginBean.
     */
 /*   @Test
    public void testLogout() {
        System.out.println("logout");
        LoginBean instance = new LoginBean();
        instance.logout();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
