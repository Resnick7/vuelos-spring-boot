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
public class VueloController {

    @Autowired
    private VueloService vueloService;

    @Autowired
    private CiudadService ciudadService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AerolineaService aerolineaService;

    @Autowired
    private AeropuertoService aeropuertoService;

    @GetMapping("/listar")
    public List<Vuelo> obtenerVuelos() throws Exception {
        return vueloService.findAll();
    }

    @PostMapping("/agregar")
    public Vuelo agregarVuelo(@RequestBody Vuelo vuelo) throws Exception {
        return vueloService.save(vuelo);
    }

    // ----------------------------------------------------------
    private void inicializarDatosBasicos() throws Exception {
        try {
            // 1. Crear ciudades si no existen
            if (ciudadService.findAll().isEmpty()) {
                crearCiudadesBasicas();
            }

            // 2. Crear usuarios si no existen
            if (usuarioService.findAll().isEmpty()) {
                crearUsuariosBasicos();
            }

            // 3. Crear aerolíneas si no existen
            if (aerolineaService.findAll().isEmpty()) {
                crearAerolineasBasicas();
            }

            // 4. Crear aeropuertos si no existen
            if (aeropuertoService.findAll().isEmpty()) {
                crearAeropuertosBasicos();
            }

        } catch (Exception e) {
            System.err.println("Error en inicialización básica: " + e.getMessage());
            throw e;
        }
    }

    private void crearCiudadesBasicas() {
        String[][] ciudades = {
                {"Madrid", "España"},
                {"Barcelona", "España"},
                {"París", "Francia"},
                {"Londres", "Reino Unido"},
                {"Roma", "Italia"},
                {"Berlín", "Alemania"},
                {"Ámsterdam", "Países Bajos"},
                {"Lisboa", "Portugal"},
                {"Buenos Aires", "Argentina"},
                {"Nueva York", "Estados Unidos"}
        };

        for (String[] datos : ciudades) {
            try {
                Ciudad ciudad = new Ciudad();
                ciudad.setNombreCiudad(datos[0]);
                ciudadService.save(ciudad);
                System.out.println("Ciudad creada: " + datos[0]);
            } catch (Exception e) {
                System.err.println("Error al crear ciudad " + datos[0] + ": " + e.getMessage());
            }
        }
    }

    private void crearUsuariosBasicos() {
        String[][] usuarios = {
                {"Ana", "González", "ana.gonzalez@email.com"},
                {"Juan", "Pérez", "juan.perez@email.com"},
                {"María", "López", "maria.lopez@email.com"},
                {"Carlos", "Rodríguez", "carlos.rodriguez@email.com"},
                {"Laura", "Martínez", "laura.martinez@email.com"}
        };

        for (String[] datos : usuarios) {
            try {
                Usuario usuario = new Usuario();
                usuario.setNombrePersona(datos[0]);
                usuario.setApellidoPersona(datos[1]);
                usuario.setCorreoElectronicoUsuario(datos[2]);
                usuario.setContaseniaUsuario("password123"); // En producción, encriptar
                usuarioService.save(usuario);
                System.out.println("Usuario creado: " + datos[0] + " " + datos[1]);
            } catch (Exception e) {
                System.err.println("Error al crear usuario " + datos[0] + ": " + e.getMessage());
            }
        }
    }

    private void crearAerolineasBasicas() {
        String[] aerolineas = {
                "Iberia", "Lufthansa", "Air France", "British Airways",
                "KLM", "Alitalia", "Turkish Airlines", "Emirates"
        };

        for (String nombre : aerolineas) {
            try {
                Aerolinea aerolinea = new Aerolinea();
                aerolinea.setNombreAerolinea(nombre);
                aerolineaService.save(aerolinea);
                System.out.println("Aerolínea creada: " + nombre);
            } catch (Exception e) {
                System.err.println("Error al crear aerolínea " + nombre + ": " + e.getMessage());
            }
        }
    }

    private void crearAeropuertosBasicos() throws Exception {
        List<Ciudad> ciudades = ciudadService.findAll();
        if (ciudades.isEmpty()) {
            System.err.println("No hay ciudades disponibles para crear aeropuertos");
            return;
        }

        Object[][] aeropuertos = {
                {123, "Aeropuerto Adolfo Suárez Madrid-Barajas", "Madrid"},
                {234, "Aeropuerto Josep Tarradellas Barcelona-El Prat", "Barcelona"},
                {345, "Aeropuerto Charles de Gaulle", "París"},
                {456, "Aeropuerto de Heathrow", "Londres"},
                {567, "Aeropuerto Leonardo da Vinci", "Roma"}
        };

        for (Object[] datos : aeropuertos) {
            try {
                Long codigo = (Long) datos[0];
                String nombre = (String) datos[1];
                String nombreCiudad = (String) datos[2];

                // Buscar la ciudad
                Ciudad ciudad = ciudades.stream()
                        .filter(c -> c.getNombreCiudad().equals(nombreCiudad))
                        .findFirst()
                        .orElse(null);

                if (ciudad != null) {
                    Aeropuerto aeropuerto = new Aeropuerto();
                    aeropuerto.setId(codigo);
                    aeropuerto.setNombreAeropuerto(nombre);
                    aeropuerto.setCiudad(ciudad);
                    aeropuertoService.save(aeropuerto);
                    System.out.println("Aeropuerto creado: " + nombre);
                }
            } catch (Exception e) {
                System.err.println("Error al crear aeropuerto: " + e.getMessage());
            }
        }
    }

//    @GetMapping("/buscar")
//    public ResponseEntity<?> buscarVuelos(
//            @RequestParam(required = false) String ciudad,
//            @RequestParam(required = false) String fecha) {
//        try {
//            List<Vuelo> vuelos;
//
//            if (ciudad != null && !ciudad.trim().isEmpty()) {
//                // Buscar vuelos por ciudad de destino
//                vuelos = vueloService.findByDestinationCity(ciudad.trim());
//            } else {
//                // Si no se especifica ciudad, devolver todos los vuelos
//                vuelos = vueloService.findAll();
//            }
//
//            // Si no hay vuelos, crear algunos de ejemplo
//            if (vuelos.isEmpty() && ciudad != null) {
//                vuelos = crearVuelosEjemplo(ciudad);
//            }
//
//            return ResponseEntity.ok(vuelos);
//
//        } catch (Exception e) {
//            System.err.println("Error al buscar vuelos: " + e.getMessage());
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error al buscar vuelos: " + e.getMessage());
//        }
//    }

    private List<Vuelo> crearVuelosEjemplo(String ciudadDestino) {
        List<Vuelo> vuelosEjemplo = new ArrayList<>();

        try {
            List<Aerolinea> aerolineas = aerolineaService.findAll();
            List<Aeropuerto> aeropuertos = aeropuertoService.findAll();

            if (aerolineas.isEmpty() || aeropuertos.isEmpty()) {
                System.out.println("No hay aerolíneas o aeropuertos para crear vuelos de ejemplo");
                return vuelosEjemplo;
            }

            // Buscar aeropuerto de destino
            Aeropuerto aeropuertoDestino = aeropuertos.stream()
                    .filter(a -> a.getCiudad().getNombreCiudad().toLowerCase()
                            .contains(ciudadDestino.toLowerCase()))
                    .findFirst()
                    .orElse(aeropuertos.get(0));

            // Crear 3 vuelos de ejemplo
            for (int i = 0; i < 3; i++) {
                Vuelo vuelo = new Vuelo();
                vuelo.setAerolinea(aerolineas.get(i % aerolineas.size()));

                Set<Aeropuerto> aeropuertosVuelo = new HashSet<>();
                aeropuertosVuelo.add(aeropuertos.get(0)); // Origen
                aeropuertosVuelo.add(aeropuertoDestino); // Destino
                vuelo.setAeropuertos((List<Aeropuerto>) aeropuertosVuelo);

                Vuelo vueloGuardado = vueloService.save(vuelo);
                vuelosEjemplo.add(vueloGuardado);
            }

        } catch (Exception e) {
            System.err.println("Error al crear vuelos de ejemplo: " + e.getMessage());
        }

        return vuelosEjemplo;
    }
}