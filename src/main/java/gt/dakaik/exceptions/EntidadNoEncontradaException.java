/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.exceptions;

/**
 *
 * @author Steve Ortiz <stvortiz@gmail.com>
 */
public class EntidadNoEncontradaException extends Exception {

    public EntidadNoEncontradaException() {
        super();
    }

    public EntidadNoEncontradaException(final String Mensaje) {
        super(Mensaje);
    }
}
