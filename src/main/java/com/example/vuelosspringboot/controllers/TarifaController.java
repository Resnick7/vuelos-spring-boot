package com.example.vuelosspringboot.controllers;

import com.example.vuelosspringboot.entities.Tarifa;
import com.example.vuelosspringboot.services.TarifaServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/tarifas")
public class TarifaController extends BaseControllerImpl<Tarifa, TarifaServiceImpl> {
}