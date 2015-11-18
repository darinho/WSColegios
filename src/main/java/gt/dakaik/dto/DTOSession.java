/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.dto;

import gt.entities.User;
import gt.entities.Window;
import java.util.List;

/**
 *
 * @author dario.calderon
 */
public class DTOSession {

    private User user;
    private List<Window> windows;
    private String token;

    public DTOSession() {
    }

    public DTOSession(User user, List<Window> windows, String token) {
        this.user = user;
        this.windows = windows;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Window> getWindows() {
        return windows;
    }

    public void setWindows(List<Window> windows) {
        this.windows = windows;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
