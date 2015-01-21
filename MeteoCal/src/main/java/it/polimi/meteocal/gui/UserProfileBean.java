/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.gui;

import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.boundary.UserManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import java.io.Serializable;
import java.nio.file.Files;
import java.rmi.server.UID;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Filippo
 */
@Named(value = "userProfile")
@ViewScoped
public class UserProfileBean implements Serializable {

    /**
     * Creates a new instance of userProfile
     */
    @PersistenceContext
    private EntityManager em;

    @EJB
    private UserManager userManager;

    @Inject
    Principal principal;
    
    private String photoUrl;

    private User user;
    private boolean ownprofile;
    private String value;
    private String name;
    private String surname;
    private Object FilenameUtils;
    private User userProfile;

    public String getPhotoUrl() {
        return photoUrl;
    }

    
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
        user.setSurname(surname);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        user.setName(name);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        init();

    }

    public boolean isOwnprofile() {
        return ownprofile;
    }

    public void setOwnprofile(boolean ownprofile) {
        this.ownprofile = ownprofile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserProfileBean() {

    }

    @PostConstruct
    public void postConstruct() {
        initParam();
        userProfile = userManager.findUserforId(value);
        if (userProfile!=null) {
            ownprofile = userManager.getLoggedUser().equals(userProfile);
        } else {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(EventPageBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void init() {
        this.user = userManager.findUserforId(value);
        ownprofile = em.find(User.class, principal.getName()).equals(this.user);
    }

    public void initParam() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        value = request.getParameter("id");
        System.out.println("id: " + value);

    }

    public void reset() {

        this.user = userManager.findUserforId(user.getEmail());

    }
 
    public void save() {
    
        userManager.update(user);
    }

}
