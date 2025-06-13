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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CiudadService ciudadService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AerolineaService aerolineaService;

    @Autowired
    private AeropuertoService aeropuertoService;

    @Autowired
    private VueloService vueloService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private TarifaService tarifaService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> obtenerDashboard() {
        try {
            Map<String, Object> dashboard = new HashMap<>();

            // Estadísticas generales
            dashboard.put("totalCiudades", ciudadService.findAll().size());
            dashboard.put("totalUsuarios", usuarioService.findAll().size());
            dashboard.put("totalAerolineas", aerolineaService.findAll().size());
            dashboard.put("totalAeropuertos", aeropuertoService.findAll().size());
            dashboard.put("totalVuelos", vueloService.findAll().size());
            dashboard.put("totalReservas", reservaService.findAll().size());

            return ResponseEntity.ok(dashboard);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener dashboard: " + e.getMessage());
        }
    }

    @GetMapping("/ciudades-disponibles")
    public ResponseEntity<?> obtenerCiudadesDisponibles() {
        try {
            List<Ciudad> ciudades = ciudadService.findAll();

            List<Map<String, Object>> ciudadesConVuelos = ciudades.stream()
                    .map(ciudad -> {
                        Map<String, Object> ciudadMap = new HashMap<>();
                        ciudadMap.put("id", ciudad.getId());
                        ciudadMap.put("nombre", ciudad.getNombreCiudad());

                        try {
                            List<Vuelo> vuelos = vueloService.findAll();
                            long vuelosDisponibles = vuelos.stream()
                                    .filter(v -> v.getAeropuertos() != null &&
                                            v.getAeropuertos().stream()
                                                    .anyMatch(a -> a.getCiudad() != null &&
                                                            a.getCiudad().getId().equals(ciudad.getId())))
                                    .count();
                            ciudadMap.put("vuelosDisponibles", vuelosDisponibles);
                        } catch (Exception e) {
                            ciudadMap.put("vuelosDisponibles", 0);
                        }

                        return ciudadMap;
                    })
                    .filter(c -> (Long) c.get("vuelosDisponibles") > 0)
                    .sorted((c1, c2) -> Long.compare((Long) c2.get("vuelosDisponibles"), (Long) c1.get("vuelosDisponibles")))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ciudadesConVuelos);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener ciudades: " + e.getMessage());
        }
    }

    @PostMapping("/reinicializar-datos")
    public ResponseEntity<?> reinicializarDatos(@RequestParam(defaultValue = "false") boolean forzar) {
        try {
            Map<String, Object> resultado = new HashMap<>();

            int ciudadesActuales = ciudadService.findAll().size();
            int usuariosActuales = usuarioService.findAll().size();
            int vuelosActuales = vueloService.findAll().size();
            int aerolineasActuales = aerolineaService.findAll().size();

            resultado.put("datosAnteriores", Map.of(
                    "ciudades", ciudadesActuales,
                    "usuarios", usuariosActuales,
                    "vuelos", vuelosActuales,
                    "aerolineas", aerolineasActuales
            ));

            if (!forzar && ciudadesActuales > 0 && vuelosActuales > 0) {
                resultado.put("mensaje", "Los datos ya están inicializados. Usa ?forzar=true para reinicializar.");
                return ResponseEntity.ok(resultado);
            }

            resultado.put("mensaje", "Para reinicializar completamente, reinicia la aplicación o usa la consola H2.");
            resultado.put("recomendacion", "Usa /h2-console para gestionar la base de datos manualmente.");
            resultado.put("urlH2", "/h2-console");

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al reinicializar datos: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> verificarSalud() {
        try {
            Map<String, Object> salud = new HashMap<>();

            boolean ciudadesOk = ciudadService.findAll() != null;
            boolean usuariosOk = usuarioService.findAll() != null;
            boolean vuelosOk = vueloService.findAll() != null;

            salud.put("serviciosCiudades", ciudadesOk ? "OK" : "ERROR");
            salud.put("serviciosUsuarios", usuariosOk ? "OK" : "ERROR");
            salud.put("serviciosVuelos", vuelosOk ? "OK" : "ERROR");
            salud.put("estadoGeneral", (ciudadesOk && usuariosOk && vuelosOk) ? "SALUDABLE" : "CON_PROBLEMAS");
            salud.put("timestamp", new Date());

            return ResponseEntity.ok(salud);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "estadoGeneral", "ERROR",
                            "error", e.getMessage(),
                            "timestamp", new Date()
                    ));
        }
    }

    @GetMapping("/debug-datos")
    public ResponseEntity<?> debugDatos() {
        try {
            Map<String, Object> debug = new HashMap<>();

            List<Ciudad> ciudades = ciudadService.findAll();
            List<Usuario> usuarios = usuarioService.findAll();
            List<Aerolinea> aerolineas = aerolineaService.findAll();
            List<Aeropuerto> aeropuertos = aeropuertoService.findAll();
            List<Vuelo> vuelos = vueloService.findAll();

            debug.put("conteos", Map.of(
                    "ciudades", ciudades.size(),
                    "usuarios", usuarios.size(),
                    "aerolineas", aerolineas.size(),
                    "aeropuertos", aeropuertos.size(),
                    "vuelos", vuelos.size()
            ));

            debug.put("ejemploCiudades", ciudades.stream()
                    .limit(5)
                    .map(c -> Map.of("id", c.getId(), "nombre", c.getNombreCiudad()))
                    .collect(Collectors.toList()));

            debug.put("ejemploAerolineas", aerolineas.stream()
                    .limit(5)
                    .map(a -> Map.of("id", a.getId(), "nombre", a.getNombreAerolinea()))
                    .collect(Collectors.toList()));

            List<Map<String, Object>> vuelosDetalle = vuelos.stream()
                    .limit(5)
                    .map(v -> {
                        Map<String, Object> vueloInfo = new HashMap<>();
                        vueloInfo.put("id", v.getId());
                        vueloInfo.put("aerolinea", v.getAerolinea() != null ? v.getAerolinea().getNombreAerolinea() : null);
                        vueloInfo.put("numAeropuertos", v.getAeropuertos() != null ? v.getAeropuertos().size() : 0);

                        if (v.getAeropuertos() != null && !v.getAeropuertos().isEmpty()) {
                            vueloInfo.put("origen", v.getAeropuertos().get(0).getCiudad() != null ?
                                    v.getAeropuertos().get(0).getCiudad().getNombreCiudad() : "Sin ciudad");
                            if (v.getAeropuertos().size() > 1) {
                                vueloInfo.put("destino", v.getAeropuertos().get(v.getAeropuertos().size() - 1).getCiudad() != null ?
                                        v.getAeropuertos().get(v.getAeropuertos().size() - 1).getCiudad().getNombreCiudad() : "Sin ciudad");
                            }
                        }

                        return vueloInfo;
                    })
                    .collect(Collectors.toList());

            debug.put("ejemploVuelos", vuelosDetalle);

            debug.put("estadoGeneral", vuelos.size() > 0 ? "DATOS_DISPONIBLES" : "SIN_VUELOS");
            debug.put("timestamp", new Date());

            return ResponseEntity.ok(debug);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", e.getMessage(),
                            "stackTrace", e.getStackTrace(),
                            "timestamp", new Date()
                    ));
        }
    }
}