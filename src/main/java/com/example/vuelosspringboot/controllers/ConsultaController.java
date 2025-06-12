package com.example.vuelosspringboot.controllers;

import com.example.vuelosspringboot.entities.Consulta;
import com.example.vuelosspringboot.services.ConsultaServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/consultas")
public class ConsultaController extends BaseControllerImpl<Consulta, ConsultaServiceImpl> {
}