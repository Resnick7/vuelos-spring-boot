package com.example.vuelosspringboot.controllers;

import com.example.vuelosspringboot.entities.Fecha;
import com.example.vuelosspringboot.services.FechaServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/fechas")
public class FechaController extends BaseControllerImpl<Fecha, FechaServiceImpl> {
}