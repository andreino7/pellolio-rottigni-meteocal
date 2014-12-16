/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Filippo
 */
@Named(value = "userBean")
@RequestScoped
public class UserBean implements Serializable {

    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
    }

    @EJB
    private UserManager userManager;

    private String name;
    private String surname;
    private String email;
    private String profilePhoto;

    public String getProfilePhoto() {
        if (userManager.isLoggedIn()) {
            return userManager.getLoggedUser().getProfilePhoto();
        }
        return "avatars/default.jpg";
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getEmail() {
        if (userManager.isLoggedIn()) {
            return userManager.getLoggedUser().getEmail();
        }
        return null;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        if (userManager.isLoggedIn()) {
            return userManager.getLoggedUser().getName();
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        if (userManager.isLoggedIn()) {
            return userManager.getLoggedUser().getSurname();
        }
        return null;
    }

    public void setSurname(String surname) {

        this.surname = surname;
    }

}
