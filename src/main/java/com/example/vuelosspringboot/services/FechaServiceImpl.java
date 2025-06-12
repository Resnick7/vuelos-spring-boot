package com.example.vuelosspringboot.services;

import com.example.vuelosspringboot.entities.Fecha;
import com.example.vuelosspringboot.repositories.BaseRepository;
import com.example.vuelosspringboot.repositories.FechaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FechaServiceImpl extends BaseServiceImpl<Fecha, Long> implements FechaService {
    @Autowired
    private FechaRepository fechaRepository;

    public FechaServiceImpl(BaseRepository<Fecha, Long> baseRepository, FechaRepository fechaRepository) {
        super(baseRepository);
        this.fechaRepository = fechaRepository;
    }
}
