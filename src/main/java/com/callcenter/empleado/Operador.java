package com.callcenter.empleado;

/**
 * La clase <code>Operador</code> representa el cargo en jerarquia más baja del CallCenter con las siguientes caracteristicas:
 * <ul>
 * <li>Esta clase extiende de la clase general Empleado, heredando todos sus atributos y métodos
 * <li>Tiene asociado un nombre
 * <li>Tiene prioridad 1 de atención de una llamada telefónica
 * <li>Posee una marcación que indica si está disponible al momento de recibir una llamada 
 * <li>
 * </ul> 
 * 
 * @author Juan Carlos Eduardo
 * @version 1.0
 * @since 1.8
 */
public class Operador extends Empleado {

    /**
     * Constructor que instancia un operador
     * 
     * @param nombre Nombre asociado al operador
     */
    public Operador(String nombre) {
        super(nombre);
        setPrioridad(PRIORIDAD.UNO);
    }

    /**
     * Constructor que instancia un operador 
     * 
     * @param nombre Nombre asociado al operador
     * @param estaLibre Marcación que indica la disponibilidad para atender una llamada telefónica
     */
    public Operador(String nombre, Boolean estaLibre) {
        super(nombre, estaLibre);
        setPrioridad(PRIORIDAD.UNO);
    }

    @Override
    public String toString() {
        return "Operador [getNombre()=" + getNombre() + ", toString()=" + super.toString() + ", getClass()="
                + getClass() + ", hashCode()=" + hashCode() + "]";
    }

}
