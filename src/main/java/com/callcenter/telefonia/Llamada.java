package com.callcenter.telefonia;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Clase asociado a una llamada telefónica
 * 
 * @author Juan Carlos Eduardo
 * @version 1.0
 * @since 1.8
 *
 */
public class Llamada {

    /**
     * Permite calcular la duración de una llamada telefónica mediante un número aleatorio entre 5 y 10 segundos 
     */
    private Random generarDuracionAleatoria;

    /**
     * Duración mínima de una llamada telefónica en segundos
     */
    private static final int MIN_DURACION = 5;
    
    /**
     * Duración máxima de una llamada telefónica en segundos
     */
    private static final int MAX_DURACION = 10;
    
    /**
     * Estado de una llamada telefónica que para este caso se maneja dos estados: Iniciada y Terminada
     */
    private ESTADO_LLAMADA estadoLlamada;
    
    /**
     * Nombre asociado al empleado que atendió la llamada
     */
    private String personaAtiendioLlamada;

    /**
     * Constructor por defecto de una llamada telefónica
     */
    public Llamada() {
        generarDuracionAleatoria = new Random();
        generarDuracionAleatoria.setSeed(System.currentTimeMillis());
        personaAtiendioLlamada = "NO_ASIGNADO";
        /* Se indica que el estado de la llamada es Iniciada */
        this.estadoLlamada = ESTADO_LLAMADA.INICIADA;
    }

    /**
     * Método que procesa la atención de una llamada entre 5 y 10 segundos
     * 
     * @param nombre Nombre del empleado que atiende la llamada
     */
    public void atenderLlamada(String nombre) {
        /* Se asigna a la llamada el nombre del empleado */
        this.personaAtiendioLlamada = nombre;
        /* Se ejecuta en un hilo el procesamiento de la llamada */
        Runnable llamada = () -> {
            try {
                int nAleatorio = generarDuracionAleatoria.nextInt(Llamada.MAX_DURACION - Llamada.MIN_DURACION + 1) + Llamada.MIN_DURACION;
                System.out.println("Tiempo de llamada --> " + nAleatorio + " seg(s) ");                
                /* Cálculo de número aleatorio en segundos */
                TimeUnit.SECONDS.sleep(nAleatorio);
                System.out.println(nombre + " - Termino llamada. Empleado disponible. ");
            } catch (InterruptedException e) {
                System.out.println( "\nError de interrupción de una excepción: " + e.getMessage());
            }
        };
        Thread t = new Thread (llamada, nombre);
        t.start();
    }
    
    /**
     * Método que obtiene el estado de una llamada telefónica
     * 
     * @return Un enumerado con el estado de una llamada:
     *         Iniciada - Que indica que la llamada está siendo atendida 
     *         Terminada - Que indica que la llamada terminó 
     */
    public ESTADO_LLAMADA getEstadoLlamada() {
        return estadoLlamada;
    }

    /**
     * Método que asigna el estado a una llamada
     * 
     * @param estadoLlamada Un enumerado con el estado de una llamada:
     *                      Iniciada - Que indica que la llamada está siendo atendida 
     *                      Terminada - Que indica que la llamada terminó 
     */
    public void setEstadoLlamada(ESTADO_LLAMADA estadoLlamada) {
        this.estadoLlamada = estadoLlamada;
    }

    /**
     * Método que obtiene el nombre del empleado que atiende la llamada
     * 
     * @return Nombre del empleado que atendió la llamada
     */
    public String getPersonaAtiendioLlamada() {
        return personaAtiendioLlamada;
    }

    @Override
    public String toString() {
        return "Llamada [generarDuracionAleatoria=" + generarDuracionAleatoria + ", MIN_DURACION=" + MIN_DURACION + ", MAX_DURACION=" + MAX_DURACION
                + ", estadoLlamada=" + estadoLlamada + ", personaAtiendioLlamada=" + personaAtiendioLlamada + "]";
    }

}
