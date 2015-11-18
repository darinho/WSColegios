/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Steve Ortiz <stvortiz@gmail.com>
 */
@ControllerAdvice
public class ManejadorExcepciones {

    Logger eLog = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @ExceptionHandler(EntidadNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, String> entidadNoEncontradaExceptionHandler(EntidadNoEncontradaException ex) {
        Map<String, String> res = new HashMap<>();
        res.put("error", "Entidad no Encontrada");
        res.put("entidad", ex.getMessage());
        return res;
    }

    @ResponseBody
    @ExceptionHandler(EntidadDuplicadaException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    Map<String, String> entidadDuplicadaExceptionHandler(EntidadDuplicadaException ex) {
        Map<String, String> res = new HashMap<>();
        res.put("error", "Entidad duplicada");
        res.put("entidad", ex.getMessage());
        return res;
    }

    @ResponseBody
    @ExceptionHandler(GeneralException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    Map<String, String> generalExceptionHandler(GeneralException ex) {
        Map<String, String> res = new HashMap<>();
        res.put("error", "Error de Servicio");
        res.put("entidad", ex.getMessage());
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        String err = errors.toString();
        eLog.error(err);
        return res;
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ResponseEntity<String> exceptionHandler(Exception ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        String err = errors.toString();
        eLog.error(err);
        String resp = "<div><h1>" + ex.getMessage() + "</h1><br/><p>" + err + "</p></div>";
        return new ResponseEntity(resp, HttpStatus.CONFLICT);
    }

}
