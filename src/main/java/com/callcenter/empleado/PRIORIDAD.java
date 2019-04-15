package com.callcenter.empleado;

/**
 * Enumerado para categorizar la prioridad en la atención de una llamada telefónica
 * 
 * @author Juan Carlos Eduardo
 * @version 1.0
 * @since 1.8
 */
public enum PRIORIDAD {
    UNO, /* Asociada al OPERADOR y debe atender en primera instancia una llamada telefónica */ 
    DOS, /* Asociada al SUPERVISOR y debe atender una llamada telefónica si no hay ningún operador libre */
    TRES /* Asociada al DIRECTOR y debe atender una llamada telefónica si no hay ningún operadores libres ni supervisores */
}
