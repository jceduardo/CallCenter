package com.callcenter.despachador;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.callcenter.empleado.Director;
import com.callcenter.empleado.Empleado;
import com.callcenter.empleado.Operador;
import com.callcenter.empleado.Supervisor;
import com.callcenter.telefonia.ESTADO_LLAMADA;
import com.callcenter.telefonia.Llamada;

/**
 * Clase <code>Dispatcher</code> que actúa como despachador de llamadas telefónicas a los 
 * empleados que están disponibles. Si todos los empleados están atendiendo una llamada, se
 * maneja una cola de llamadas en espera y se van procesando a medida que los empleados van
 * quedando disponibles al terminar una llamada. Si hay menos de 10 empleados se procesan las 
 * llamadas por demanda.
 * 
 * @author Juan Carlos Eduardo
 * @version 1.0
 * @since 1.8
 *
 */
public class Dispatcher {

    /**
     * Capacidad máxima de llamadas concurrentes que se puedan atender
     */
    public static final Integer CAPACIDAD_MAXIMA_CONCURRENTE = 10;
    
    /**
     * Permite contar las llamadas en espera para luego procesarlas en una cola de espera
     */
    private int contarLlamadasEnEspera = 0;
    
    /**
     * Permite contar las llamadas procesadas desde la cola de espera
     */
    private int procesarLlamadasEnEspera = 1;
    
    /**
     * Número de llamadas totales que el despachador va recibiendo
     */
    private int numeroLlamadas = 0;
    
    /**
     * Colección asociada a una lista de empleados
     */
    private Set<Empleado> listaEmpleados;
    
    /**
     * Colección asociada a una lista de de llamadas atendidas
     */
    private Set<Llamada> llamadasAtendidas;
    
    /**
     * Colección asociada a una lista de llamadas en espera o una cola de llamadas
     */
    private Set<Llamada> llamadasEspera;

    /**
     * Constructor por defecto de un despachador
     */
    public Dispatcher() {
        /* Se inicializan las colecciones */
        listaEmpleados = new HashSet<>(0);
        llamadasAtendidas = new HashSet<>(0);
        llamadasEspera = new HashSet<>(0);
    }

    /**
     * Método que asigna un empleado al despachador para luego asignarle llamadas telefónicas 
     * 
     * @param empleado Objeto asociado a un empleado que puede ser un Operador, un Supervisor o un Director
     */
    public void setEmpleado(Empleado empleado) {
        if (empleado == null) {
            System.out.println("Error: Empleado no válido");
            throw new RuntimeException("Error: Empleado no válido");
        }
        listaEmpleados.add(empleado);
    }
    
    /**
     * Método que obtiene un empleado dado una posición dada de la lista de empleados del despachador 
     * 
     * @param indice Posición del empleado en la lista de empleados
     */
    public Set<Empleado> getListaEmpleados() {
        return listaEmpleados;
    }

    /**
     * Método para asignar una lista de empleados
     * 
     * @param listaEmpleados Lista de empleados
     */
    public void setListaEmpleados(Set<Empleado> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    /**
     * Método que obtiene una lista de llamadas atendidas por los empleados
     * 
     * @return Devuelve una colección de llamadas atendidas
     */
    public Set<Llamada> getLlamadasAtendidas() {
        return llamadasAtendidas;
    }

    /**
     * Método que asigna una llamada a una cola de espera en caso que no haya disponibilidad de atender la llamada
     * 
     * @param llamada
     */
    public void setLLamadaEspera(Llamada llamada) {   
        llamadasEspera.add(llamada);
    }
    
    public void dispatchCall(Llamada llamada) { 
        if (llamada == null) {
            System.out.println("\nError: Llamada no válida y no procesada por el despachador.");
            throw new RuntimeException("Error: Llamada no válida y no procesada por el despachador.");
        }
        if (listaEmpleados.isEmpty()) {
            System.out.println("Error: Lista de empleados vacía");
            throw new RuntimeException("Error: Lista de empleados vacía");
            
        }        
        numeroLlamadas++;
        Optional<Empleado> empleado = asignarLlamadaEmpleadoDisponible();
        if (!empleado.isPresent()) {
            if (contarLlamadasEnEspera < numeroLlamadas - Dispatcher.CAPACIDAD_MAXIMA_CONCURRENTE) {
                System.out.println("--> CallCenter full...");
                setLLamadaEspera(llamada);
                System.out.println("--> Llamada " + contarLlamadasEnEspera + " en Espera");
                /* Determina que hay un procesamiento de llamadas en espera */
                if (contarLlamadasEnEspera == 0) {
                    procesarColaLlamadas();
                }
                contarLlamadasEnEspera++;
            }
        } else {
            Empleado empleadoDisponible =  empleado.get();
            System.out.println("\n" + empleadoDisponible.getNombre() + " - Atendiendo llamada...");
            llamada.atenderLlamada(empleadoDisponible.getNombre());           
            llamada.setEstadoLlamada(ESTADO_LLAMADA.TERMINADA);
            llamadasAtendidas.add(llamada);
        }        
    }

    /**
     * Procesar colas de llamadas por demanda
     */
    public void procesarColaLlamadas() {
        /* Crear un hilo para que procese la cola de llamadas por demanda (a penas esté alguno de los empleados disponibles) */
        Runnable revisarLlamadasEnEspera = () -> {
            while (Boolean.TRUE) {
                Optional<Llamada> terminoAlgunaLlamada = llamadasAtendidas.stream().filter(f -> f.getEstadoLlamada() == ESTADO_LLAMADA.TERMINADA).findFirst();
                if ((contarLlamadasEnEspera > 0) && terminoAlgunaLlamada.isPresent()) {
                    System.out.println("Atendiendo " + procesarLlamadasEnEspera + " Llamada en Espera...");
                    Llamada llamadaTerminada = terminoAlgunaLlamada.get();
                    System.out.println("Llamada Terminada -> " + llamadaTerminada);
                    listaEmpleados = listaEmpleados.stream()
                            .map(e -> (e.getNombre().compareTo(llamadaTerminada.getPersonaAtiendioLlamada()) == 0)
                                    ? obtenerEmpleado(e, Boolean.TRUE).get()
                                            : e)
                            .collect(Collectors.toSet()); 
                    llamadaTerminada.atenderLlamada(llamadaTerminada.getPersonaAtiendioLlamada());         
                    if (procesarLlamadasEnEspera < contarLlamadasEnEspera) {
                        procesarLlamadasEnEspera++;
                    } else {
                        break;
                    }
                }
            }
        };
        Thread t = new Thread(revisarLlamadasEnEspera, "ColaLlamadas");
        t.start();
    }

    /**
     * Método sincronizado que permite al despachador asignar una llamada a un empleado disponible
     * 
     * @return Devuelve el empleado disponible para atender la llamada telefónica
     */
    public synchronized Optional<Empleado> asignarLlamadaEmpleadoDisponible() {        
        Optional<Empleado> empleadoDisponible;
        empleadoDisponible = listaEmpleados.stream()
                .filter(e -> e.getEstaLibre())                                      /* Verifica los empleados que estén libres */
                .sorted((e1, e2) -> e1.getPrioridad().compareTo(e2.getPrioridad())) /* Ordena por orden de prioridad */
                .findFirst();                                                       /* Devuelve el primero disponible */
        if (empleadoDisponible.isPresent()) {
            Empleado empleado = empleadoDisponible.get();
            /* El empleado que atiende la llamada hay que cambiarle el estado a ocupado */
            asignarEmpleadoOcupadoEnLlamada(empleado.getNombre());
        }        
        return empleadoDisponible;
    }

    /**
     * Método que permite que el despachador asigne una llamada telefónica a un empleado disponible 
     * 
     * @param nombre Nombre del empleado disponible
     */
    private void asignarEmpleadoOcupadoEnLlamada(String nombre) {
        /* Recorre la lista de empleados, verifica que si el nombre coincide y esta disponible entonces
         * realiza la asignación a estado no disponible */
        listaEmpleados = listaEmpleados.stream()
                .map(e -> (e.getNombre().compareTo(nombre) == 0) && (e.getEstaLibre())  /* Busca el nombre y verifica su disponibilidad  */
                        ? obtenerEmpleado(e, Boolean.FALSE).get()                       /* Asigna al empleado que está ocupado */
                                : e)                                                    /* Devuelve la colección actualizada */
                .collect(Collectors.toSet()); 
    }

    /**
     * Método que apartir de la clase padre <code>Empleado</code> permite conocer si es de tipo Operador,
     * Supervisor o Director
     * 
     * @param empleado Objeto asociada a un empleado en general (operador, supervisor o director)  
     * @param estaLibre Indica si el empleado está disponible  o no
     * @return Un objeto empleado con la subclase asociada
     */
    public Optional<Empleado> obtenerEmpleado(Empleado empleado, Boolean estaLibre) {
        Optional<Empleado> empleadoSubClase = Optional.empty();
        if (empleado instanceof Operador) {
            /* Crea un empleado tipo Operador */
            empleadoSubClase = Optional.of(new Operador(empleado.getNombre(), estaLibre)); 
        } else if (empleado instanceof Supervisor) {
            /* Crea un empleado tipo Supervisor */
            empleadoSubClase = Optional.of(new Supervisor(empleado.getNombre(), estaLibre)); 
        } else if (empleado instanceof Director) {
            /* Crea un empleado tipo Director */
            empleadoSubClase = Optional.of(new Director(empleado.getNombre(), estaLibre)); 
        }
        return empleadoSubClase;
    }

}
