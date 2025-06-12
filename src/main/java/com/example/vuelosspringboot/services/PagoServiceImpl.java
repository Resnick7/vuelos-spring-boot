package com.example.vuelosspringboot.services;

import com.example.vuelosspringboot.entities.Pago;
import com.example.vuelosspringboot.repositories.BaseRepository;
import com.example.vuelosspringboot.repositories.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagoServiceImpl extends BaseServiceImpl<Pago, Long> implements PagoService {
    @Autowired
    private PagoRepository pagoRepository;

    public PagoServiceImpl(BaseRepository<Pago, Long> baseRepository, PagoRepository pagoRepository) {
        super(baseRepository);
        this.pagoRepository = pagoRepository;
    }
}
