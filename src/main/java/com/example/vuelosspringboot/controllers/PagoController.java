package com.example.vuelosspringboot.controllers;

import com.example.vuelosspringboot.entities.Pago;
import com.example.vuelosspringboot.services.PagoServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/pagos")
public class PagoController extends BaseControllerImpl<Pago, PagoServiceImpl> {
}