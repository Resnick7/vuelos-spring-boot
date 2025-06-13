package com.example.vuelosspringboot.controllers;

import com.example.vuelosspringboot.entities.*;
import com.example.vuelosspringboot.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/vuelos")
public class VueloController extends BaseControllerImpl<Vuelo, VueloServiceImpl> {

    @Autowired
    private CiudadService ciudadService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AerolineaService aerolineaService;

    @Autowired
    private AeropuertoService aeropuertoService;

    @Autowired
    private TarifaService tarifaService;

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarVuelos(
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String fecha) {
        try {
            List<Vuelo> vuelos = servicio.findAll();

            // Filtrar por ciudad si se proporciona
            if (ciudad != null && !ciudad.trim().isEmpty()) {
                vuelos = vuelos.stream()
                        .filter(vuelo -> vuelo.getAeropuertos() != null &&
                                vuelo.getAeropuertos().stream()
                                        .anyMatch(aeropuerto ->
                                                aeropuerto.getCiudad() != null &&
                                                        aeropuerto.getCiudad().getNombreCiudad() != null &&
                                                        aeropuerto.getCiudad().getNombreCiudad().toLowerCase()
                                                                .contains(ciudad.toLowerCase())))
                        .collect(Collectors.toList());
            }

            // Limitar a 10 resultados para mejor rendimiento
            if (vuelos.size() > 10) {
                vuelos = vuelos.subList(0, 10);
            }

            return ResponseEntity.ok(vuelos);

        } catch (Exception e) {
            System.err.println("Error al buscar vuelos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar vuelos: " + e.getMessage());
        }
    }

    @GetMapping("/datos-reserva")
    public ResponseEntity<?> obtenerDatosReserva() {
        try {
            Map<String, Object> datos = new HashMap<>();

            // Obtener ciudades
            List<Ciudad> ciudades = ciudadService.findAll();
            datos.put("ciudades", ciudades);

            // Obtener usuarios
            List<Usuario> usuarios = usuarioService.findAll();
            datos.put("usuarios", usuarios);

            // Obtener aerolíneas
            List<Aerolinea> aerolineas = aerolineaService.findAll();
            datos.put("aerolineas", aerolineas);

            return ResponseEntity.ok(datos);

        } catch (Exception e) {
            System.err.println("Error al obtener datos de reserva: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener datos: " + e.getMessage());
        }
    }

    @GetMapping("/test-datos")
    public ResponseEntity<?> testDatos() {
        try {
            Map<String, Object> datos = new HashMap<>();

            List<Ciudad> ciudades = ciudadService.findAll();
            List<Usuario> usuarios = usuarioService.findAll();
            List<Vuelo> vuelos = servicio.findAll();
            List<Aerolinea> aerolineas = aerolineaService.findAll();

            datos.put("numCiudades", ciudades.size());
            datos.put("numUsuarios", usuarios.size());
            datos.put("numVuelos", vuelos.size());
            datos.put("numAerolineas", aerolineas.size());

            datos.put("mensaje", "Conexión exitosa con la base de datos");

            return ResponseEntity.ok(datos);

        } catch (Exception e) {
            System.err.println("Error en test de datos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en test: " + e.getMessage());
        }
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas() {
        try {
            Map<String, Object> estadisticas = new HashMap<>();

            List<Vuelo> vuelos = servicio.findAll();
            List<Aerolinea> aerolineas = aerolineaService.findAll();
            List<Aeropuerto> aeropuertos = aeropuertoService.findAll();

            estadisticas.put("totalVuelos", vuelos.size());
            estadisticas.put("totalAerolineas", aerolineas.size());
            estadisticas.put("totalAeropuertos", aeropuertos.size());

            // Estadísticas por aerolínea
            Map<String, Long> vuelosPorAerolinea = vuelos.stream()
                    .filter(v -> v.getAerolinea() != null)
                    .collect(Collectors.groupingBy(
                            v -> v.getAerolinea().getNombreAerolinea(),
                            Collectors.counting()
                    ));
            estadisticas.put("vuelosPorAerolinea", vuelosPorAerolinea);

            // Ciudades más populares (como destino)
            Map<String, Long> destinosPopulares = vuelos.stream()
                    .filter(v -> v.getAeropuertos() != null && v.getAeropuertos().size() > 1)
                    .map(v -> v.getAeropuertos().get(v.getAeropuertos().size() - 1)) // Último aeropuerto = destino
                    .filter(a -> a.getCiudad() != null)
                    .collect(Collectors.groupingBy(
                            a -> a.getCiudad().getNombreCiudad(),
                            Collectors.counting()
                    ));
            estadisticas.put("destinosPopulares", destinosPopulares);

            return ResponseEntity.ok(estadisticas);

        } catch (Exception e) {
            System.err.println("Error al obtener estadísticas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener estadísticas: " + e.getMessage());
        }
    }

    @GetMapping("/por-destino/{nombreCiudad}")
    public ResponseEntity<?> buscarVuelosPorDestino(@PathVariable String nombreCiudad) {
        try {
            List<Vuelo> vuelos = servicio.findAll();

            List<Vuelo> vuelosDestino = vuelos.stream()
                    .filter(vuelo -> vuelo.getAeropuertos() != null &&
                            vuelo.getAeropuertos().stream()
                                    .anyMatch(aeropuerto ->
                                            aeropuerto.getCiudad() != null &&
                                                    aeropuerto.getCiudad().getNombreCiudad() != null &&
                                                    aeropuerto.getCiudad().getNombreCiudad().toLowerCase()
                                                            .contains(nombreCiudad.toLowerCase())))
                    .limit(15) // Limitar resultados
                    .collect(Collectors.toList());

            return ResponseEntity.ok(vuelosDestino);

        } catch (Exception e) {
            System.err.println("Error al buscar vuelos por destino: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar vuelos: " + e.getMessage());
        }
    }

    @GetMapping("/con-tarifas")
    public ResponseEntity<?> obtenerVuelosConTarifas() {
        try {
            List<Vuelo> vuelos = servicio.findAll();

            // Enriquecer vuelos con información de tarifas
            List<Map<String, Object>> vuelosConTarifas = vuelos.stream()
                    .limit(20) // Limitar para mejor rendimiento
                    .map(vuelo -> {
                        Map<String, Object> vueloMap = new HashMap<>();
                        vueloMap.put("id", vuelo.getId());
                        vueloMap.put("aerolinea", vuelo.getAerolinea());
                        vueloMap.put("aeropuertos", vuelo.getAeropuertos());
                        vueloMap.put("piloto", vuelo.getPiloto());

                        // Obtener tarifas para este vuelo
                        try {
                            List<Tarifa> tarifas = tarifaService.findAll().stream()
                                    .filter(t -> t.getVuelo() != null && t.getVuelo().getId().equals(vuelo.getId()))
                                    .collect(Collectors.toList());
                            vueloMap.put("tarifas", tarifas);
                        } catch (Exception e) {
                            vueloMap.put("tarifas", new ArrayList<>());
                        }

                        return vueloMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(vuelosConTarifas);

        } catch (Exception e) {
            System.err.println("Error al obtener vuelos con tarifas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener vuelos con tarifas: " + e.getMessage());
        }
    }
}