package com.example.vuelosspringboot.controllers;

import com.example.vuelosspringboot.entities.Aeropuerto;
import com.example.vuelosspringboot.services.AeropuertoServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/aeropuertos")
public class AeropuertoController extends BaseControllerImpl<Aeropuerto, AeropuertoServiceImpl> {
}