package com.example.vuelosspringboot.services;

import com.example.vuelosspringboot.entities.Piloto;
import com.example.vuelosspringboot.repositories.BaseRepository;
import com.example.vuelosspringboot.repositories.PilotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PilotoServiceImpl extends BaseServiceImpl<Piloto, Long> implements PilotoService {
    @Autowired
    private PilotoRepository pilotoRepository;

    public PilotoServiceImpl(BaseRepository<Piloto, Long> baseRepository, PilotoRepository pilotoRepository) {
        super(baseRepository);
        this.pilotoRepository = pilotoRepository;
    }
}
