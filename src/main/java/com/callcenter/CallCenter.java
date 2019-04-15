package com.callcenter;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.callcenter.despachador.Dispatcher;
import com.callcenter.empleado.Director;
import com.callcenter.empleado.Operador;
import com.callcenter.empleado.Supervisor;
import com.callcenter.telefonia.Llamada;

/**
 * Clase principal del CallCenter que ejecuta el procesamiento de llamadas telefónicas
 * 
 * @author Juan Carlos Eduardo
 * @version 1.0
 * @since 1.8
 *
 */
public class CallCenter {
    
    /* Despachador con los nombres de operadores, el supervidor y el director */
    private static Dispatcher dispatcher;
    
    /* Lista de empleados  */
    private static String[] listaEmpleados = new String[] { "Tomas", "Jose", "Pedro", "Marta", "Maria", "Esteban", "Luis", "Andres", "Lorena", "Juan" };

    /* Programa principal que ejecuta el CallCenter con capacidad máxima de 10 empleados. */
    public static void main(String[] args) {
        /* Ingresar al CallCenter los Operadores, Supervisor y Director */
        dispatcher = new Dispatcher();
        for(int i = 0; i < listaEmpleados.length; i++) {
            if ((i >= 0) && (i <= 7)) {
                System.out.printf("\nIngresando operador --> %s",listaEmpleados[i]);
                dispatcher.setEmpleado(new Operador(listaEmpleados[i]));
            } else if (i == 8) {
                System.out.printf("\nIngresando supervisor --> %s",listaEmpleados[i]);
                dispatcher.setEmpleado(new Supervisor(listaEmpleados[i]));
            } else {
                System.out.printf("\nIngresando director --> %s",listaEmpleados[i]);
                dispatcher.setEmpleado(new Director(listaEmpleados[i]));
            }             
        } 
    
        for(int i = 1; i <= 10; i++) {
            System.out.printf("\nEntrando Llamada (%d)...",i);
            dispatcher.dispatchCall(new Llamada());
        }   
        
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\nLLamadas Atendidas:");
        Set<Llamada> llamadasAtendidas = dispatcher.getLlamadasAtendidas();
        llamadasAtendidas.stream().forEach(System.out::println);

    }

}
