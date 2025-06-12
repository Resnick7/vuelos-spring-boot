package com.example.vuelosspringboot.controllers;

import com.example.vuelosspringboot.entities.Aerolinea;
import com.example.vuelosspringboot.services.AerolineaServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/aerolineas")
public class AerolineaController extends BaseControllerImpl<Aerolinea, AerolineaServiceImpl> {
}
