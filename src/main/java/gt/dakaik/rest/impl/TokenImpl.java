/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.impl;

import gt.dakaik.rest.interfaces.WSToken;
import gt.dakaik.rest.repository.UserRepository;
import gt.dakaik.rest.repository.UserSessionRepository;
import gt.entities.UserSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Dario Calderon
 */
@Component
public class TokenImpl implements WSToken {

    Logger eLog = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserRepository ru;
    @Autowired
    UserSessionRepository rs;

    public void setRepo(UserRepository ru, UserSessionRepository rs) {
        this.rs = rs;
        this.ru = ru;
    }

    public boolean validaTokenUsuarioOperaciones(String sToken, int idUsuario) {
        boolean bResult = false;
        try {
            List<UserSession> list2 = rs.findByToken(sToken);
            if (list2.size() == 1) {
                bResult = list2.get(0).getIdUser() == idUsuario;
            }
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            String err = errors.toString();
            eLog.error(err);
        }
        return bResult;
    }
}
