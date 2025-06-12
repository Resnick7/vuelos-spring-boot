package com.example.vuelosspringboot.services;

import com.example.vuelosspringboot.entities.Avion;
import com.example.vuelosspringboot.repositories.AvionRepository;
import com.example.vuelosspringboot.repositories.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvionServiceImpl extends BaseServiceImpl<Avion, Long> implements AvionService {
    @Autowired
    private AvionRepository avionRepository;

    public AvionServiceImpl(BaseRepository<Avion, Long> baseRepository, AvionRepository avionRepository) {
        super(baseRepository);
        this.avionRepository = avionRepository;
    }
}
