/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.boundary;

import it.polimi.meteocal.entity.User;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author miglie
 */
@RunWith(Arquillian.class)
public class UserManagerIT {
    

    @EJB
    UserManager cut;
    
    @PersistenceContext
    EntityManager em;
    
//    @PersistenceContext
//    EntityManager em;

    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(UserManager.class)
                .addPackage(User.class.getPackage())
                .addAsResource("test-persistence.xml", "/META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Test
    public void UserManagerShouldBeInjected() {
        assertNotNull(cut);
    }
}