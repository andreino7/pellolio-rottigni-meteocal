/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.boundary;

import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.gui.LoginBean;
import it.polimi.meteocal.gui.RegistrationBean;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.when;

/**
 *
 * @author miglie
 */
@RunWith(Arquillian.class)
public class UserManagerIT {
    

    @EJB
    UserManager cut;
    @Inject
    LoginBean lb;
    @Inject
    RegistrationBean rb;
    
    @PersistenceContext
    EntityManager em;
    
//    @PersistenceContext
//    EntityManager em;

    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(UserManager.class)
                .addClass(LoginBean.class)
                .addClass(RegistrationBean.class)
                .addPackage(User.class.getPackage())
                .addAsResource("test-persistence.xml", "/META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Test
    public void UserManagerandLoginBeanShouldBeInjected() {
        assertNotNull(cut);
        assertNotNull(lb);
        assertNotNull(rb);
    }
    
    @Test
    public void EntityManagerShouldBeInjected(){
        assertNotNull(em);
    }
    
    @Test
    public void NewUser(){
        User u= new User("invalidMail", "s", null, 99, null, null);
        rb.setUser(u);
        try {
            rb.register();
            fail("User Should not be registered");
        }catch (Exception e){
            assertTrue(true);
        }
        String mail="zz@x.y";
        String password="password";
        u=new User(mail, "aa", "bb", 346, "USER", password);
        
        rb.setUser(u);
        try {
            rb.register();
            assertTrue(true);
        }catch (Exception e){
            fail("User Should be registered");
        }
        
        lb.setEmail(mail);
        lb.setPassword(password);
        
        lb.login();
        
        assertTrue(cut.isLoggedIn());
    }
    
    
    
    
}