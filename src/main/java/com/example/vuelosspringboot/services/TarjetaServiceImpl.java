package com.example.vuelosspringboot.services;

import com.example.vuelosspringboot.entities.Tarjeta;
import com.example.vuelosspringboot.repositories.BaseRepository;
import com.example.vuelosspringboot.repositories.TarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TarjetaServiceImpl extends BaseServiceImpl<Tarjeta, Long> implements TarjetaService {
    @Autowired
    private TarjetaRepository tarjetaRepository;

    public TarjetaServiceImpl(BaseRepository<Tarjeta, Long> baseRepository, TarjetaRepository tarjetaRepository) {
        super(baseRepository);
        this.tarjetaRepository = tarjetaRepository;
    }
}
