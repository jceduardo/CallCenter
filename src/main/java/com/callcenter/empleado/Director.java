package com.callcenter.empleado;

/**
 * La clase <code>Director</code> representa el cargo en jerarquia más alta del CallCenter con las siguientes caracteristicas:
 * <ul>
 * <li>Esta clase extiende de la clase general Empleado, heredando todos sus atributos y métodos
 * <li>Tiene asociado un nombre
 * <li>Tiene prioridad 3 de atención de una llamada telefónica
 * <li>Posee una marcación que indica si está disponible al momento de recibir una llamada 
 * <li>
 * </ul> 
 * 
 * @author Juan Carlos Eduardo
 * @version 1.0
 * @since 1.8
 */
public class Director extends Empleado {

    /**
     * Constructor que instancia un director
     * 
     * @param nombre Nombre asociado al director
     */
    public Director(String nombre) {
        super(nombre);
        setPrioridad(PRIORIDAD.TRES);
    }

    /**
     * Constructor que instancia un director 
     * 
     * @param nombre Nombre asociado al director
     * @param estaLibre Marcación que indica la disponibilidad para atender una llamada telefónica
     */
    public Director(String nombre, Boolean estaLibre) {
        super(nombre, estaLibre);
        setPrioridad(PRIORIDAD.TRES);
    }

    @Override
    public String toString() {
        return "Operador [getNombre()=" + getNombre() + ", toString()=" + super.toString() + ", getClass()="
                + getClass() + ", hashCode()=" + hashCode() + "]";
    }

}
