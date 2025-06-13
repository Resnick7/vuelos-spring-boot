package com.example.vuelosspringboot.config;

import com.example.vuelosspringboot.entities.*;
import com.example.vuelosspringboot.services.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CiudadService ciudadService;

    @Autowired
    private AeropuertoService aeropuertoService;

    @Autowired
    private AerolineaService aerolineaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private VueloService vueloService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("🚀 Iniciando carga de datos iniciales...");

        crearDatosBasicos();
        System.out.println("🎉 ¡Datos básicos cargados exitosamente!");
    }

    private void crearDatosBasicos() throws Exception {
        // 1. Crear ciudades
        List<Ciudad> ciudades = new ArrayList<>();
        String[] nombresCiudades = {
                "Madrid", "Barcelona", "París", "Londres", "Roma",
                "Berlín", "Ámsterdam", "Lisboa", "Nueva York", "Miami"
        };

        for (String nombre : nombresCiudades) {
            Ciudad ciudad = new Ciudad();
            ciudad.setNombreCiudad(nombre);
            ciudades.add(ciudadService.save(ciudad));
        }
        System.out.println("✅ Ciudades: " + ciudades.size());

        // 2. Crear aerolíneas
        List<Aerolinea> aerolineas = new ArrayList<>();
        String[] nombresAerolineas = {
                "Iberia", "Vueling", "Lufthansa", "Air France", "British Airways"
        };

        for (String nombre : nombresAerolineas) {
            Aerolinea aerolinea = new Aerolinea();
            aerolinea.setNombreAerolinea(nombre);
            aerolineas.add(aerolineaService.save(aerolinea));
        }
        System.out.println("✅ Aerolíneas: " + aerolineas.size());

        // 3. Crear aeropuertos
        List<Aeropuerto> aeropuertos = new ArrayList<>();
        String[] nombresAeropuertos = {
                "Madrid-Barajas", "Barcelona-El Prat", "Charles de Gaulle",
                "Heathrow", "Leonardo da Vinci", "Berlín-Brandenburg",
                "Schiphol", "Lisboa", "JFK", "Miami International"
        };

        for (int i = 0; i < nombresAeropuertos.length && i < ciudades.size(); i++) {
            Aeropuerto aeropuerto = new Aeropuerto();
            aeropuerto.setNombreAeropuerto(nombresAeropuertos[i]);
            aeropuerto.setCiudad(ciudades.get(i));
            aeropuertos.add(aeropuertoService.save(aeropuerto));
        }
        System.out.println("✅ Aeropuertos: " + aeropuertos.size());

        // 4. Crear usuarios
        List<Usuario> usuarios = new ArrayList<>();
        String[][] datosUsuarios = {
                {"Ana", "González", "ana@email.com"},
                {"Juan", "Pérez", "juan@email.com"},
                {"María", "López", "maria@email.com"},
                {"Carlos", "Rodríguez", "carlos@email.com"},
                {"Laura", "Martínez", "laura@email.com"}
        };

        for (int i = 0; i < datosUsuarios.length; i++) {
            String[] datos = datosUsuarios[i];
            Usuario usuario = new Usuario();
            usuario.setDniPersona(12345678 + i);
            usuario.setNombrePersona(datos[0]);
            usuario.setApellidoPersona(datos[1]);
            usuario.setCorreoElectronicoUsuario(datos[2]);
            usuario.setContaseniaUsuario("password123");
            usuario.setNumeroUsuario(1000 + i);
            usuarios.add(usuarioService.save(usuario));
        }
        System.out.println("✅ Usuarios: " + usuarios.size());

        // 5. Crear vuelos
        crearVuelosSimples(aeropuertos, aerolineas);
    }

    private void crearVuelosSimples(List<Aeropuerto> aeropuertos, List<Aerolinea> aerolineas) throws Exception {
        List<Vuelo> vuelos = new ArrayList<>();

        Object[][] vuelosDefinidos = {
                {0, 0, 1}, // Iberia: Madrid -> Barcelona
                {0, 2, 3}, // Iberia: París -> Londres
                {1, 1, 4}, // Vueling: Barcelona -> Roma
                {1, 0, 2}, // Vueling: Madrid -> París
                {2, 3, 5}, // Lufthansa: Londres -> Berlín
                {2, 4, 6}, // Lufthansa: Roma -> Ámsterdam
                {3, 2, 0}, // Air France: París -> Madrid
                {3, 6, 7}, // Air France: Ámsterdam -> Lisboa
                {4, 3, 8}, // British Airways: Londres -> Nueva York
                {4, 8, 9}, // British Airways: Nueva York -> Miami
                {0, 5, 0}, // Iberia: Berlín -> Madrid
                {1, 7, 1}, // Vueling: Lisboa -> Barcelona
                {2, 9, 8}, // Lufthansa: Miami -> Nueva York
                {3, 1, 5}, // Air France: Barcelona -> Berlín
                {4, 4, 2}  // British Airways: Roma -> París
        };

        for (int i = 0; i < vuelosDefinidos.length; i++) {
            try {
                Object[] datos = vuelosDefinidos[i];
                int aerolineaIndex = (int) datos[0];
                int origenIndex = (int) datos[1];
                int destinoIndex = (int) datos[2];

                Vuelo vuelo = new Vuelo();
                vuelo.setAerolinea(aerolineas.get(aerolineaIndex));

                List<Aeropuerto> aeropuertosVuelo = new ArrayList<>();
                aeropuertosVuelo.add(aeropuertos.get(origenIndex));
                aeropuertosVuelo.add(aeropuertos.get(destinoIndex));
                vuelo.setAeropuertos(aeropuertosVuelo);

                Vuelo vueloGuardado = vueloService.save(vuelo);
                vuelos.add(vueloGuardado);

                System.out.println("✅ Vuelo " + (i + 1) + ": " +
                        aerolineas.get(aerolineaIndex).getNombreAerolinea() + " - " +
                        aeropuertos.get(origenIndex).getCiudad().getNombreCiudad() + " → " +
                        aeropuertos.get(destinoIndex).getCiudad().getNombreCiudad());

            } catch (Exception e) {
                System.err.println("❌ Error en vuelo " + (i + 1) + ": " + e.getMessage());
            }
        }
        System.out.println("✅ Total vuelos: " + vuelos.size());
    }
}