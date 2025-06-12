package com.example.vuelosspringboot.services;

import com.example.vuelosspringboot.entities.Aeropuerto;
import com.example.vuelosspringboot.repositories.AeropuertoRepository;
import com.example.vuelosspringboot.repositories.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AeropuertoServiceImpl extends BaseServiceImpl<Aeropuerto, Long> implements AeropuertoService {
    @Autowired
    private AeropuertoRepository aeropuertoRepository;

    public AeropuertoServiceImpl(BaseRepository<Aeropuerto, Long> baseRepository, AeropuertoRepository aeropuertoRepository) {
        super(baseRepository);
        this.aeropuertoRepository = aeropuertoRepository;
    }
}
