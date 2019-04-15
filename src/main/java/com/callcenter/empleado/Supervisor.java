package com.callcenter.empleado;

/**
 * La clase <code>Supervisor</code> representa el cargo en jerarquia media del CallCenter con las siguientes caracteristicas:
 * <ul>
 * <li>Esta clase extiende de la clase general Empleado, heredando todos sus atributos y métodos
 * <li>Tiene asociado un nombre
 * <li>Tiene prioridad 2 de atención de una llamada telefónica
 * <li>Posee una marcación que indica si está disponible al momento de recibir una llamada 
 * <li>
 * </ul> 
 * 
 * @author Juan Carlos Eduardo
 * @version 1.0
 * @since 1.8
 */
public class Supervisor extends Empleado {

    /**
     * Constructor que instancia un supervisor
     * 
     * @param nombre Nombre asociado al supervisor
     */
    public Supervisor(String nombre) {
        super(nombre);
        setPrioridad(PRIORIDAD.DOS);
    }

    /**
     * Constructor que instancia un supervisor 
     * 
     * @param nombre Nombre asociado al supervisor
     * @param estaLibre Marcación que indica la disponibilidad para atender una llamada telefónica
     */
    public Supervisor(String nombre, Boolean estaLibre) {
        super(nombre, estaLibre);
        setPrioridad(PRIORIDAD.DOS);
    }

    @Override
    public String toString() {
        return "Operador [getNombre()=" + getNombre() + ", toString()=" + super.toString() + ", getClass()="
                + getClass() + ", hashCode()=" + hashCode() + "]";
    }

}
