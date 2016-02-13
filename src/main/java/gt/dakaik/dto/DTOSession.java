/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.dto;

import gt.entities.User;
import gt.entities.UserProfile;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dario.calderon
 */
public class DTOSession {

    private Long idUserSession;
    private User user;
    private Long idUser;
    private String token;
    private Date startDate;
    private Date endDate;
    private List<UserProfile> userProfiles;
    private UserProfile userProfile;

    public DTOSession() {
    }

    public DTOSession(Long idUserSession, User user, Long idUser, String token, Date startDate, Date endDate) {
        this.idUserSession = idUserSession;
        this.user = user;
        this.idUser = idUser;
        this.token = token;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public DTOSession(Long idUserSession, User user, Long idUser, String token, Date startDate, Date endDate, List<UserProfile> userProfiles, UserProfile userProfile) {
        this.idUserSession = idUserSession;
        this.user = user;
        this.idUser = idUser;
        this.token = token;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userProfiles = userProfiles;
        this.userProfile = userProfile;
    }

    public Long getIdUserSession() {
        return idUserSession;
    }

    public void setIdUserSession(Long idUserSession) {
        this.idUserSession = idUserSession;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public void setUserProfiles(List<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

}
