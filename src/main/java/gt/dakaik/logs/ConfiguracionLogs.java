/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gt.dakaik.logs;

import org.slf4j.MDC;


public class ConfiguracionLogs{
    
    public static String Llave="aplicacion";
    
    public static String Discriminador(){
        return new StringBuilder().append(InformacionAplicacion.getOrganizacion()).append("/")
                .append(InformacionAplicacion.getAplicacion()).append("/")
                .append(InformacionAplicacion.getVersion()).toString();
    }
    
    public static void agregarLlave(){
        MDC.put(Llave, Discriminador());
    }
    
    public static void removerLlave(){
        MDC.remove(Llave);
    }

   
    
}
