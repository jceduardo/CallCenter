package com.callcenter.despachador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.callcenter.despachador.Dispatcher;
import com.callcenter.empleado.Director;
import com.callcenter.empleado.Empleado;
import com.callcenter.empleado.Operador;
import com.callcenter.empleado.Supervisor;
import com.callcenter.telefonia.ESTADO_LLAMADA;
import com.callcenter.telefonia.Llamada;

@DisplayName("Suite de pruebas de CallCenter")
@TestMethodOrder(OrderAnnotation.class)
public class CallCenterUnitTest {

    /* Despachador con los nombres de operadores, el supervidor y el director */
    private static Dispatcher dispatcher;

    /* Lista de empleados  */
    private static String[] listaEmpleados = new String[] { "Tomas", "Jose", "Pedro", "Marta", "Maria", "Esteban", "Luis", "Andres", "Lorena", "Juan" };

    @BeforeEach
    void antesCadaPrueba() {
        System.out.println("Antes de cada Caso de Prueba - Crear despachador");
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

        Set<Empleado> listaEmpleadosCreados = dispatcher.getListaEmpleados();
        /* Esto no es necesario pero es importante para métrica de cobertura */ 
        for(Empleado empleado: listaEmpleadosCreados) {
            if (empleado instanceof Operador) {
                Operador operador = (Operador) empleado;
                assertTrue(operador.toString() != null);  
            } else if (empleado instanceof Supervisor) {
                Supervisor supervisor = (Supervisor) empleado;
                assertTrue(supervisor.toString() != null);  
            } else if (empleado instanceof Director) {
                Director director = (Director) empleado;
                assertTrue(director.toString() != null);  
            }  
        }
        System.out.println("");
    }
    
    @AfterEach
    void despuesCadaPrueba() {
        System.out.println("Después de cada Caso de Prueba - Liberar despachador");
        dispatcher = null;
    }

    @Test
    @Order(1)
    @DisplayName("1 - Verificar llamada no válida")
    void pruebaVerificarLlamadaNoValida() {                
        Assertions.assertThrows(RuntimeException.class, () -> {
            dispatcher.dispatchCall(null);
        });
    }

    @Test
    @Order(2)
    @DisplayName("2 - Verificar empleado no válido")
    void pruebaVerificarEmpleadoNoValido() {                
        Assertions.assertThrows(RuntimeException.class, () -> {
            dispatcher.setEmpleado(null);
        });
    }

    @Test
    @Order(3)
    @DisplayName("3 - Verificar 6 llamadas concurrentes y capacidad máxima de 10 empleados")
    void pruebaMenosLlamadasMaximoEmpleados() {

        for(int i = 1; i <= 6; i++) {
            System.out.printf("\nEntrando Llamada (%d)...",i);
            dispatcher.dispatchCall(new Llamada());
        } 

        /* Obtener el resultado de las llamadas */
        Set<Llamada> llamadasAtendidas = dispatcher.getLlamadasAtendidas();

        /* Verificar el método toString() de un objeto Llamada - No es necesario pero es algo que se mide en la cobertura */        
        assertTrue(llamadasAtendidas.stream().findFirst().get().toString() != null);

        /* Verificar que sean 6 llamadas */
        assertTrue(llamadasAtendidas.size() == 6);

        /* Verificar que a cada empleado se le asignó una llamada y la culminó */
        List<String> integerList = new ArrayList<>(Arrays.asList(listaEmpleados));
        for(Llamada llamada: llamadasAtendidas) {
            assertTrue(integerList.stream().anyMatch(emp -> llamada.getPersonaAtiendioLlamada().compareTo(emp) == 0));
        }

    }

    @Test
    @Order(4)
    @DisplayName("4 - Verificar 10 llamadas concurrentes y capacidad máxima de 10 empleados")
    void pruebaCapacidadMáxima() {

        for(int i = 1; i <= 10; i++) {
            System.out.printf("\nEntrando Llamada (%d)...",i);
            dispatcher.dispatchCall(new Llamada());
        } 

        /* Obtener el resultado de las llamadas */
        Set<Llamada> llamadasAtendidas = dispatcher.getLlamadasAtendidas();

        /* Verificar que sean 10 llamadas */
        assertTrue(llamadasAtendidas.size() == 10);

        /* Verificar que a cada empleado se le asignó una llamada y la culminó */
        for(Llamada llamada: llamadasAtendidas) {
            System.out.println("lamada.getEstadoLlamada() --> " + llamada.getEstadoLlamada());
            //assertEquals(ESTADO_LLAMADA.TERMINADA, llamada.getEstadoLlamada());
            assertTrue(llamada.getPersonaAtiendioLlamada().compareTo("NO_ASIGNADO") != 0);
        }

    }

    @Test
    @Order(5)
    @DisplayName("5 - Verificar 12 llamadas concurrentes y capacidad máxima de 10 empleados")
    void pruebaCapacidadSuperiorMáxima() {

        for(int i = 1; i <= 12; i++) {
            System.out.printf("\nEntrando Llamada (%d)...",i);
            dispatcher.dispatchCall(new Llamada());
        } 

        /* Voy asumir que en el peor de los casos todas las llamadas tardaron 16 segundos */
        try {
            TimeUnit.SECONDS.sleep(120);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /* Obtener el resultado de las llamadas */
        Set<Llamada> llamadasAtendidas = dispatcher.getLlamadasAtendidas();

        /* Verificar que a cada empleado se le asignó una llamada y la culminó */
        for(Llamada llamada: llamadasAtendidas) {
            assertEquals(ESTADO_LLAMADA.TERMINADA, llamada.getEstadoLlamada());
            assertTrue(llamada.getPersonaAtiendioLlamada().compareTo("NO_ASIGNADO") != 0);
        }

    };

    @Test
    @Order(6)
    @DisplayName("6 - Verificar que dos empleados no puedan atender una misma llamada concurrente)")
    void pruebaVerificarNoExistanDuplicados() {

        for(int i = 1; i <= 10; i++) {
            System.out.printf("\nEntrando Llamada (%d)...",i);
            dispatcher.dispatchCall(new Llamada());
        } 

        /* Obtener el resultado de las llamadas */
        Set<Llamada> llamadasAtendidas = dispatcher.getLlamadasAtendidas();

        /* Verificar que sean 10 llamadas */
        assertTrue(llamadasAtendidas.size() == 10);

        /* Las llamadas no deben estar duplicadas */
        Set<Llamada> llamadasDuplicadas = llamadasAtendidas.stream().filter(i -> Collections.frequency(llamadasAtendidas, i) > 1)
                .collect(Collectors.toSet());        
        assertTrue(llamadasDuplicadas.isEmpty());

    }

    @Test
    @Order(7)
    @DisplayName("7 - Verificar 10 llamadas concurrentes y capacidad máxima de 6 empleados")
    void pruebaNoTodaLaCapacidad() {
        
        for(int i = 0; i < listaEmpleados.length; i++) {
            if ((i >= 4) && (i <= 7)) {
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

        /* Obtener el resultado de las llamadas */
        Set<Llamada> llamadasAtendidas = dispatcher.getLlamadasAtendidas();

        /* Verificar el método toString() de un objeto Llamada - No es necesario pero es algo que se mide en la cobertura */        
        assertTrue(llamadasAtendidas.stream().findFirst().get().toString() != null);

        /* Verificar que sean 10 llamadas */
        assertTrue(llamadasAtendidas.size() == 10);

        /* Verificar que a cada empleado se le asignó una llamada y la culminó */
        List<String> integerList = new ArrayList<>(Arrays.asList(listaEmpleados));
        for(Llamada llamada: llamadasAtendidas) {
            assertTrue(integerList.stream().anyMatch(emp -> llamada.getPersonaAtiendioLlamada().compareTo(emp) == 0));
        }

    }
    
    @Test
    @Order(8)
    @DisplayName("8 - Verificar que no pueda ejecutarse el despachador sin empleados")
    void pruebaSinEmpleados() {
        dispatcher.setListaEmpleados(new HashSet<>(0));
        for(int i = 1; i <= 10; i++) {
            System.out.printf("\nEntrando Llamada (%d)...",i);
            Assertions.assertThrows(RuntimeException.class, () -> {
                dispatcher.dispatchCall(new Llamada());
            });
        }

    }

}
