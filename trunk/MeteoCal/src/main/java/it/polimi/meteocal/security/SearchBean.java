/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.User;
import java.util.List;
import java.util.Vector;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Filippo
 */
@Named(value = "searchBean")
@Dependent
public class SearchBean {

    /**
     * Creates a new instance of SearchBean
     */
    public SearchBean() {
    }
    @PersistenceContext
    EntityManager em;

    private String input;
    private User user;
    private SearchResult res;

    public SearchResult getRes() {
        return res;
    }

    public void setRes(SearchResult res) {
        this.res = res;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public List<SearchResult> complete(String query) {
        if (query != null & !query.isEmpty()) {
            while (!query.isEmpty() & query.charAt(0) == ' ') {
                query = query.substring(1);
            }
            List<SearchResult> res= new Vector<>();
            res.addAll((List<User>) em.createNamedQuery(User.findByPartOfEmail, User.class).setParameter("part", "%" + query + "%").getResultList());
            System.out.println("%" + query + "%");
            res.addAll((List<Event>) em.createNamedQuery(Event.findByPartOfTitle, Event.class).setParameter("part", "%" + query + "%").getResultList() );

            return res;
        }
        return null;

    }

    public Character groupOfResult(SearchResult res){
        return res.getGroup().charAt(0);
    }
}
