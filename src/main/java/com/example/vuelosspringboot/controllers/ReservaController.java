package com.example.vuelosspringboot.controllers;

import com.example.vuelosspringboot.entities.Reserva;
import com.example.vuelosspringboot.entities.Usuario;
import com.example.vuelosspringboot.entities.Vuelo;
import com.example.vuelosspringboot.services.ReservaServiceImpl;
import com.example.vuelosspringboot.services.UsuarioServiceImpl;
import com.example.vuelosspringboot.services.VueloServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/reservas")
public class ReservaController extends BaseControllerImpl<Reserva, ReservaServiceImpl> {

    @Autowired
    private VueloServiceImpl vueloService;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearReserva(@RequestBody Map<String, Long> request) {
        try {
            Long vueloId = request.get("vueloId");
            Long usuarioId = request.get("usuarioId");

            if (vueloId == null || usuarioId == null) {
                return ResponseEntity.badRequest().body("Se requieren vueloId y usuarioId");
            }

            Vuelo vuelo = vueloService.findById(vueloId);
            Usuario usuario = usuarioService.findById(usuarioId);

            if (vuelo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vuelo no encontrado");
            }

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            Reserva reserva = new Reserva();
            reserva.setVueloReservado(vuelo);
            reserva.setUsuario(usuario);

            Reserva reservaGuardada = servicio.save(reserva);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Reserva creada exitosamente");
            response.put("reserva", reservaGuardada);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la reserva: " + e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> getReservasByUsuario(@PathVariable Long usuarioId) {
        try {
            Usuario usuario = usuarioService.findById(usuarioId);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            return ResponseEntity.status(HttpStatus.OK).body(usuario.getReservas());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener las reservas: " + e.getMessage());
        }
    }
}
