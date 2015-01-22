/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.boundary;

import it.polimi.meteocal.entity.AdminNotification;
import it.polimi.meteocal.entity.ChangedEventNotification;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.EventType;
import it.polimi.meteocal.entity.InviteNotification;
import it.polimi.meteocal.entity.ResponseNotification;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.entity.WeatherNotification;
import it.polimi.meteocal.gui.EmailSessionBean;
import it.polimi.meteocal.interfaces.Notification;
import it.polimi.meteocal.notification.NotificationStatus;
import it.polimi.meteocal.notification.NotificationType;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Filippo
 */
@Stateless
public class NotificationManager {

    @PersistenceContext
    private EntityManager em;

        
    @EJB
    private EmailSessionBean emailBean;
    
    public List<Notification> getNotificationForUser(User u){
         List<Notification> res= new LinkedList<>();
         res.addAll(em.createNamedQuery(WeatherNotification.findByReceiver, WeatherNotification.class).setParameter("user", u.getEmail()).getResultList());
         res.addAll(em.createNamedQuery(ResponseNotification.findByReceiver, ResponseNotification.class).setParameter("user", u.getEmail()).getResultList());
         res.addAll(em.createNamedQuery(InviteNotification.findByReceiver, ResponseNotification.class).setParameter("user", u.getEmail()).getResultList());
         res.addAll(em.createNamedQuery(AdminNotification.findByReceiver, ResponseNotification.class).setParameter("user", u.getEmail()).getResultList());
         res.addAll(em.createNamedQuery(ChangedEventNotification.findByReceiver, ResponseNotification.class).setParameter("user", u.getEmail()).getResultList());
         return res;
    }

    public void createWeatherNotification(WeatherNotification notification) {
        em.persist(notification);
        emailBean.sendWeatherEmail(notification.getReceiver().getEmail(), notification.getAbout().getTitle(), notification.getState());
    }

    public Notification findNotificationById(String id) {
        if (id != null) {
            WeatherNotification wn = em.find(WeatherNotification.class, Integer.parseInt(id));
            if (wn != null) {
                return wn;
            }
            InviteNotification in = em.find(InviteNotification.class, Integer.parseInt(id));
            if (in != null) {
                return in;
            }
            ResponseNotification rn = em.find(ResponseNotification.class, Integer.parseInt(id));
            if (rn != null) {
                return rn;
            }
            ChangedEventNotification cn = em.find(ChangedEventNotification.class, Integer.parseInt(id));
            if (cn != null) {
                return cn;
            }
            AdminNotification an = em.find(AdminNotification.class, Integer.parseInt(id));
            if (an != null) {
                return an;
            }
        }
        return null;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public void updateNotification(Notification notification, NotificationType notificationType) {
        switch (notificationType) {
            case WEATHER:
                em.merge((WeatherNotification) notification);
                break;
            case INVITE:
                em.merge((InviteNotification) notification);
                break;
            case RESPONSE:
                em.merge((ResponseNotification) notification);
                break;
            case CHANGED:
                em.merge((ChangedEventNotification) notification);
                break;
            case ADMIN:
                em.merge((AdminNotification) notification);
                break;
        }
    }

    public void createResponseNotification(ResponseNotification notification) {
        em.persist(notification);
        emailBean.sendResponseEmail(notification.getReceiver().getEmail(), notification.getAbout().getTitle(), notification.getSender().getTitle(),notification.getAnswer());
    }

    public Notification findNotificationByIdAndType(String id, NotificationType notificationType) {
        if (id != null) {
            switch (notificationType) {
                case WEATHER:
                    return em.find(WeatherNotification.class, Integer.parseInt(id));
                case INVITE:
                    return em.find(InviteNotification.class, Integer.parseInt(id));
                case RESPONSE:
                    return em.find(ResponseNotification.class, Integer.parseInt(id));
                case CHANGED:
                    return em.find(ChangedEventNotification.class, Integer.parseInt(id));
                case ADMIN:
                    return em.find(AdminNotification.class, Integer.parseInt(id));
            }
        }
        return null;
    }

    public void deleteInvite(InviteNotification inviteNotification) {
        InviteNotification toBeDelete = em.merge(inviteNotification);
        em.remove(toBeDelete);
    }

    public void createChangedEventNotification(Event e, List<User> partecipant) {
        User owner = e.getEventOwner();
        for (User u : partecipant) {
            if (!u.equals(owner)) {
                ChangedEventNotification change = new ChangedEventNotification();
                change.setAbout(e);
                change.setState(NotificationStatus.UNREADSEEN.toString());
                change.setReceiver(u);
                em.persist(change);
                emailBean.sendChangedEvent(u.getEmail(), e.getTitle());
            }
        }
    }

    public void removeWeatherNotification(WeatherNotification weatherNotification) {
        WeatherNotification toBeDelete = em.merge(weatherNotification);
        em.remove(toBeDelete);
    }

    public void removeAllForEvent(Event e) {
        List<WeatherNotification> weathers = getWeatherNotificationForEvent(e);
        for (WeatherNotification w: weathers) {
            removeWeatherNotification(w);
        }
        List<InviteNotification> invites = getInviteNotificationForEvent(e);
        for (InviteNotification i: invites) {
            removeInviteNotification(i);
        }
        List<ResponseNotification> responses = getResponseNotificationForEvent(e);
        for (ResponseNotification r : responses) {
            removeResponseNotification(r);
        }
        List<ChangedEventNotification> changes = getChangedEventNotificationForEvent(e);
        for (ChangedEventNotification c : changes) {
            removeChangeEventNotification(c);
        }
        List<AdminNotification> admins = getAdminNotificationForEvent(e);
        for (AdminNotification a : admins) {
            removeAdminNotification(a);
        }
    }

    private List<WeatherNotification> getWeatherNotificationForEvent(Event e) {
        return em.createNamedQuery(WeatherNotification.findByAbout, WeatherNotification.class).setParameter("event", e.getId()).getResultList();
    }

    private List<InviteNotification> getInviteNotificationForEvent(Event e) {
        return em.createNamedQuery(InviteNotification.findByAbout, InviteNotification.class).setParameter("event", e.getId()).getResultList();
    }

    private List<ResponseNotification> getResponseNotificationForEvent(Event e) {
        return em.createNamedQuery(ResponseNotification.findByAbout, ResponseNotification.class).setParameter("event", e.getId()).getResultList();
    }

    private List<ChangedEventNotification> getChangedEventNotificationForEvent(Event e) {
        return em.createNamedQuery(ChangedEventNotification.findByAbout, ChangedEventNotification.class).setParameter("event", e.getId()).getResultList();
    }

    private List<AdminNotification> getAdminNotificationForEvent(Event e) {
        return em.createNamedQuery(AdminNotification.findByAbout, AdminNotification.class).setParameter("event", e.getId()).getResultList();
    }

    public void removeInviteNotification(InviteNotification i) {
        InviteNotification toBeDelete = em.merge(i);
        em.remove(toBeDelete);
    }

    public void removeResponseNotification(ResponseNotification r) {
        ResponseNotification toBeDelete = em.merge(r);
        em.remove(toBeDelete);    }

    public void removeChangeEventNotification(ChangedEventNotification c) {
        ChangedEventNotification toBeDelete = em.merge(c);
        em.remove(toBeDelete);   
    }

    public void removeAdminNotification(AdminNotification a) {
        AdminNotification toBeDelete = em.merge(a);
        em.remove(toBeDelete);
    }
    
    public void createAdminNotification(EventType et){
        List<Event> tonotif=em.createNamedQuery(Event.findByType, Event.class).setParameter("typeid", et.getId()).getResultList();
        for (Event e:tonotif){
            AdminNotification notif=new AdminNotification();
            notif.setAbout(e);
            notif.setReceiver(e.getEventOwner());
            notif.setState(NotificationStatus.UNREADSEEN.toString());
            notif.setId(-1);
            em.persist(notif);
            emailBean.sendAdminEmail(e.getEventOwner().getEmail(), e.getTitle());
        }
    }

    public boolean notAnswered(Event event, User user) {
       Query q = em.createNamedQuery(ResponseNotification.findByAboutAndSender, ResponseNotification.class);
       q.setParameter("event", event.getId());
       q.setParameter("user", user.getEmail());
       return q.getResultList().isEmpty();
    }

    public List<WeatherNotification> findWeatherNotificationByReceiverAndEvent(Event event, User user) {
        Query q = em.createNamedQuery(WeatherNotification.findByReceiverAndEvent, WeatherNotification.class);
        q.setParameter("event", event.getId());
        q.setParameter("user", user.getEmail());
        return q.getResultList();
    }

    public boolean existInvite(User visitor, Event event) {
        Query q = em.createNamedQuery(InviteNotification.findByReceiverAndEvent, InviteNotification.class);
        q.setParameter("event", event.getId());
        q.setParameter("user", visitor.getEmail());
        return !q.getResultList().isEmpty();
    }

}
