package com.example.vuelosspringboot.controllers;

import com.example.vuelosspringboot.entities.Piloto;
import com.example.vuelosspringboot.services.PilotoServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/pilotos")
public class PilotoController extends BaseControllerImpl<Piloto, PilotoServiceImpl> {
}