/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.schedule;

import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.security.UserManager;
import javax.inject.Named;
import java.io.Serializable;
import java.security.Principal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Filippo
 */
@Named(value = "userProfile")
@ViewScoped
public class UserProfile implements Serializable {

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

    public UserProfile() {

    }

    @PostConstruct
    public void postConstruct() {
        initParam();
        this.user = new User();
        ownprofile = userManager.getLoggedUser().equals(userManager.findUserforId(value));
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
