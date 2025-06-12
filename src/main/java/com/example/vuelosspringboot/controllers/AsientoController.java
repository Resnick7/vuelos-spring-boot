package com.example.vuelosspringboot.controllers;

import com.example.vuelosspringboot.entities.Asiento;
import com.example.vuelosspringboot.services.AsientoServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/asientos")
public class AsientoController extends BaseControllerImpl<Asiento, AsientoServiceImpl> {
}