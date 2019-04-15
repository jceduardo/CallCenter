package com.callcenter.empleado;

/**
 * Clase abstracta para persistir la información de un empleado del CallCenter
 * 
 * @author Juan Carlos Eduardo
 * @version 1.0
 * @since 1.8
 *
 */
public abstract class Empleado {

    /**
     * Nombre asociado al empleado
     */
    protected String nombre;
    
    /**
     * Prioridad de atención de una llamada telefónica
     */
    protected PRIORIDAD prioridad;
    
    /**
     * Marcación que indica se está disponible para atender una llamada telefónica
     */
    protected Boolean estaLibre;

    /**
     * Constructor que instancia un empleado
     * 
     * @param nombre Nombre asociado al empleado
     */
    public Empleado(String nombre) {
        this.nombre = nombre;
        this.estaLibre = Boolean.TRUE;
    }

    /**
     * Constructor que instancia un empleado 
     * 
     * @param nombre Nombre asociado al empleado
     * @param estaLibre Marcación que indica la disponibilidad para atender una llamada telefónica
     */
    public Empleado(String nombre, Boolean estaLibre) {
        this.nombre = nombre;
        this.estaLibre = estaLibre;
    }

    /**
     * Método que obtiene el nombre asociado a un empleado
     * 
     * @return Nombre del empeado
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Método que obtiene la prioridad asignada al empleado
     * 
     * @return Prioridad del empleado
     */
    public PRIORIDAD getPrioridad() {
        return prioridad;
    }

    /**
     * Método que asigna la prioridad al empleado 
     * 
     * @param prioridad Enumerado que indican la prioridad {@link #ESTADO_LLAMADA} 
     */
    public void setPrioridad(PRIORIDAD prioridad) {
        this.prioridad = prioridad;
    }

    /**
     * Método que obtiene la disponibilidad del empleado
     * 
     * @return <code>true</code> si está libre al momento de atender una llamada telefónica o <code>false</code> en caso contrario
     */
    public Boolean getEstaLibre() {
        return estaLibre;
    }

    @Override
    public String toString() {
        return "Empleado [nombre=" + nombre + ", prioridad=" + prioridad + ", estaLibre=" + estaLibre + "]";
    }

}
