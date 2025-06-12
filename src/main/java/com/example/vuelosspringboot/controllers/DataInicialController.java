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
@RequestMapping("/api/init")
public class DataInicialController {

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

    @PostMapping("/datos-prueba")
    public ResponseEntity<?> cargarDatosPrueba() {
        try {
            Map<String, Object> resultado = new HashMap<>();

            // 1. Crear ciudades
            List<Ciudad> ciudades = crearCiudades();
            resultado.put("ciudadesCreadas", ciudades.size());

            // 2. Crear usuarios
            List<Usuario> usuarios = crearUsuarios();
            resultado.put("usuariosCreados", usuarios.size());

            // 3. Crear aerolíneas
            List<Aerolinea> aerolineas = crearAerolineas();
            resultado.put("aerolineasCreadas", aerolineas.size());

            // 4. Crear aeropuertos
            List<Aeropuerto> aeropuertos = crearAeropuertos(ciudades);
            resultado.put("aeropuertosCreados", aeropuertos.size());

            // 5. Crear vuelos (opcional, para tener datos completos)
            List<Vuelo> vuelos = crearVuelos(aerolineas, aeropuertos);
            resultado.put("vuelosCreados", vuelos.size());

            resultado.put("mensaje", "Datos de prueba cargados exitosamente");

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cargar datos de prueba: " + e.getMessage());
        }
    }

    private List<Ciudad> crearCiudades() throws Exception {
        List<Ciudad> ciudades = new ArrayList<>();

        // Verificar si ya existen ciudades
        if (ciudadService.findAll().size() > 0) {
            System.out.println("Ya existen ciudades en la base de datos");
            return ciudadService.findAll();
        }

        String[][] datosCiudades = {
                {"Madrid"},
                {"Barcelona"},
                {"París"},
                {"Londres"},
                {"Roma"},
                {"Berlín"},
                {"Ámsterdam"},
                {"Lisboa"},
                {"Viena"},
                {"Praga"},
                {"Buenos Aires"},
                {"São Paulo"},
                {"Nueva York"},
                {"Los Ángeles"},
                {"Tokio"},
                {"Seúl"},
                {"Bangkok"},
                {"Singapur"},
                {"Dubái"},
                {"Estambul"}
        };

        for (String[] datos : datosCiudades) {
            Ciudad ciudad = new Ciudad();
            ciudad.setNombreCiudad(datos[0]);

            try {
                Ciudad ciudadGuardada = ciudadService.save(ciudad);
                ciudades.add(ciudadGuardada);
                System.out.println("Ciudad creada: " + ciudadGuardada.getNombreCiudad());
            } catch (Exception e) {
                System.err.println("Error al crear ciudad " + datos[0] + ": " + e.getMessage());
            }
        }

        return ciudades;
    }

    private List<Usuario> crearUsuarios() throws Exception {
        List<Usuario> usuarios = new ArrayList<>();

        // Verificar si ya existen usuarios
        if (usuarioService.findAll().size() > 0) {
            System.out.println("Ya existen usuarios en la base de datos");
            return usuarioService.findAll();
        }

        String[][] datosUsuarios = {
                {"Ana", "González", "ana.gonzalez@email.com", "password123"},
                {"Juan", "Pérez", "juan.perez@email.com", "password123"},
                {"María", "López", "maria.lopez@email.com", "password123"},
                {"Carlos", "Rodríguez", "carlos.rodriguez@email.com", "password123"},
                {"Laura", "Martínez", "laura.martinez@email.com", "password123"},
                {"Diego", "Fernández", "diego.fernandez@email.com", "password123"},
                {"Carmen", "García", "carmen.garcia@email.com", "password123"},
                {"Miguel", "Sánchez", "miguel.sanchez@email.com", "password123"},
                {"Isabel", "Ruiz", "isabel.ruiz@email.com", "password123"},
                {"Alejandro", "Torres", "alejandro.torres@email.com", "password123"}
        };

        for (String[] datos : datosUsuarios) {
            Usuario usuario = new Usuario();
            usuario.setNombrePersona(datos[0]);
            usuario.setApellidoPersona(datos[1]);
            usuario.setCorreoElectronicoUsuario(datos[2]);
            usuario.setContaseniaUsuario(datos[3]);

            try {
                Usuario usuarioGuardado = usuarioService.save(usuario);
                usuarios.add(usuarioGuardado);
                System.out.println("Usuario creado: " + usuarioGuardado.getNombrePersona() + " " + usuarioGuardado.getApellidoPersona());
            } catch (Exception e) {
                System.err.println("Error al crear usuario " + datos[0] + ": " + e.getMessage());
            }
        }

        return usuarios;
    }

    private List<Aerolinea> crearAerolineas() {
        List<Aerolinea> aerolineas = new ArrayList<>();

        String[] nombresAerolineas = {
                "Iberia",
                "Lufthansa",
                "Air France",
                "British Airways",
                "KLM",
                "Alitalia",
                "Turkish Airlines",
                "Emirates",
                "Qatar Airways",
                "American Airlines"
        };

        for (String nombre : nombresAerolineas) {
            try {
                // Verificar si ya existe
                List<Aerolinea> existentes = aerolineaService.findAll();
                boolean existe = existentes.stream()
                        .anyMatch(a -> a.getNombreAerolinea().equals(nombre));

                if (!existe) {
                    Aerolinea aerolinea = new Aerolinea();
                    aerolinea.setNombreAerolinea(nombre);

                    Aerolinea aerolineaGuardada = aerolineaService.save(aerolinea);
                    aerolineas.add(aerolineaGuardada);
                    System.out.println("Aerolínea creada: " + aerolineaGuardada.getNombreAerolinea());
                }
            } catch (Exception e) {
                System.err.println("Error al crear aerolínea " + nombre + ": " + e.getMessage());
            }
        }

        return aerolineas;
    }

    private List<Aeropuerto> crearAeropuertos(List<Ciudad> ciudades) {
        List<Aeropuerto> aeropuertos = new ArrayList<>();

        // Datos de aeropuertos [código, nombre, ciudadIndex]
        Object[][] datosAeropuertos = {
                {123, "Aeropuerto Adolfo Suárez Madrid-Barajas", 0}, // Madrid
                {234, "Aeropuerto Josep Tarradellas Barcelona-El Prat", 1}, // Barcelona
                {345, "Aeropuerto Charles de Gaulle", 2}, // París
                {456, "Aeropuerto de Heathrow", 3}, // Londres
                {567, "Aeropuerto Leonardo da Vinci", 4}, // Roma
                {678, "Aeropuerto de Berlín-Tegel", 5}, // Berlín
                {789, "Aeropuerto de Ámsterdam-Schiphol", 6}, // Ámsterdam
                {890, "Aeropuerto Humberto Delgado", 7}, // Lisboa
                {134, "Aeropuerto de Viena", 8}, // Viena
                {245, "Aeropuerto Václav Havel de Praga", 9}, // Praga
                {356, "Aeropuerto Internacional Ezeiza", 10}, // Buenos Aires
                {467, "Aeropuerto Internacional de São Paulo", 11}, // São Paulo
                {578, "Aeropuerto Internacional John F. Kennedy", 12}, // Nueva York
                {689, "Aeropuerto Internacional de Los Ángeles", 13}, // Los Ángeles
                {790, "Aeropuerto Internacional de Narita", 14}, // Tokio
                {124, "Aeropuerto Internacional de Incheon", 15}, // Seúl
                {235, "Aeropuerto Internacional Suvarnabhumi", 16}, // Bangkok
                {346, "Aeropuerto de Singapur Changi", 17}, // Singapur
                {457, "Aeropuerto Internacional de Dubái", 18}, // Dubái
                {568, "Aeropuerto de Estambul", 19} // Estambul
        };

        for (Object[] datos : datosAeropuertos) {
            try {
                Long codigo = (Long) datos[0];
                String nombre = (String) datos[1];
                int ciudadIndex = (int) datos[2];

                if (ciudadIndex < ciudades.size()) {
                    Aeropuerto aeropuerto = new Aeropuerto();
                    aeropuerto.setId(codigo);
                    aeropuerto.setNombreAeropuerto(nombre);
                    aeropuerto.setCiudad(ciudades.get(ciudadIndex));

                    Aeropuerto aeropuertoGuardado = aeropuertoService.save(aeropuerto);
                    aeropuertos.add(aeropuertoGuardado);
                    System.out.println("Aeropuerto creado: " + aeropuertoGuardado.getNombreAeropuerto());
                }
            } catch (Exception e) {
                System.err.println("Error al crear aeropuerto: " + e.getMessage());
            }
        }

        return aeropuertos;
    }

    private List<Vuelo> crearVuelos(List<Aerolinea> aerolineas, List<Aeropuerto> aeropuertos) {
        List<Vuelo> vuelos = new ArrayList<>();

        if (aerolineas.isEmpty() || aeropuertos.size() < 2) {
            System.out.println("No hay suficientes aerolíneas o aeropuertos para crear vuelos");
            return vuelos;
        }

        try {
            // Crear algunos vuelos de ejemplo
            for (int i = 0; i < Math.min(10, aerolineas.size()); i++) {
                Vuelo vuelo = new Vuelo();
                vuelo.setAerolinea(aerolineas.get(i % aerolineas.size()));

                // Agregar aeropuertos (origen y destino)
                Set<Aeropuerto> aeropuertosVuelo = new HashSet<>();
                aeropuertosVuelo.add(aeropuertos.get(0)); // Origen (Madrid)
                aeropuertosVuelo.add(aeropuertos.get((i + 1) % Math.min(aeropuertos.size(), 10))); // Destino rotativo
                vuelo.setAeropuertos((List<Aeropuerto>) aeropuertosVuelo);

                Vuelo vueloGuardado = vueloService.save(vuelo);
                vuelos.add(vueloGuardado);
                System.out.println("Vuelo creado: " + vueloGuardado.getId());
            }
        } catch (Exception e) {
            System.err.println("Error al crear vuelos: " + e.getMessage());
        }

        return vuelos;
    }

    @GetMapping("/verificar-datos")
    public ResponseEntity<?> verificarDatos() {
        try {
            Map<String, Object> datos = new HashMap<>();

            List<Ciudad> ciudades = ciudadService.findAll();
            List<Usuario> usuarios = usuarioService.findAll();

            datos.put("ciudades", ciudades.size());
            datos.put("usuarios", usuarios.size());

            // Mostrar algunas ciudades como ejemplo
            if (!ciudades.isEmpty()) {
                List<String> nombresCiudades = ciudades.stream()
                        .limit(5)
                        .map(c -> c.getNombreCiudad())
                        .collect(Collectors.toList());
                datos.put("ejemploCiudades", nombresCiudades);
            }

            // Mostrar algunos usuarios como ejemplo
            if (!usuarios.isEmpty()) {
                List<String> nombresUsuarios = usuarios.stream()
                        .limit(5)
                        .map(u -> u.getNombrePersona() + " " + u.getApellidoPersona())
                        .collect(Collectors.toList());
                datos.put("ejemploUsuarios", nombresUsuarios);
            }

            return ResponseEntity.ok(datos);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
