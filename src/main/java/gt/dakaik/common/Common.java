/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.common;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author dario.calderon
 */
public class Common {

    static final String TOKEN = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final Map<String, Integer> activeSession = new HashMap<>();
    static final Map<String, Integer> activeTimeSession = new HashMap<>();
    static final Map<Integer, String> userSession = new HashMap<>();
    static final Timer validSession = new Timer();
    static final Timer discountTime = new Timer();
    static final int secVerified = 60;
    static final int minsSession = 10;

    public static void verifiedSessions() {
        validSession.schedule(new TimerTask() {

            @Override
            public void run() {
                activeTimeSession.entrySet().stream().forEach((e) -> {
                    int mins = e.getValue() - 1;
                    if (mins == 0) {
                        String token = e.getKey();
                        int idUser = activeSession.get(token);
                        activeSession.remove(token);
                        activeTimeSession.remove(token);
                        userSession.remove(idUser);
                    }
                    e.setValue(mins);
                });
            }
        }, secVerified * 1000);
    }

    public static String getToken() {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            sb.append(TOKEN.charAt(rnd.nextInt(TOKEN.length())));
        }

        return sb.toString();
    }

    public static long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public static String setTokenLocal(String token, Integer idUser) {
        activeSession.put(token, idUser);
        activeTimeSession.put(token, minsSession);
        userSession.put(idUser, token);
        return token;
    }

    public static void cleanSessionPrev(Integer idUser) {
        String token = userSession.get(idUser);
        activeSession.remove(token);
        activeTimeSession.remove(token);
    }

    public static boolean validSession(String token, Integer idUser) {
        Integer i = activeSession.getOrDefault(token, -1);

        if (Objects.equals(i, idUser)) {
            activeTimeSession.replace(token, minsSession);
            return true;
        } else {
            return false;
        }
    }
}
