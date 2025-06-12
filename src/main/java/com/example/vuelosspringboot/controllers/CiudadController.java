package com.example.vuelosspringboot.controllers;

import com.example.vuelosspringboot.entities.Ciudad;
import com.example.vuelosspringboot.services.CiudadServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/ciudades")
public class CiudadController extends BaseControllerImpl<Ciudad, CiudadServiceImpl> {
}