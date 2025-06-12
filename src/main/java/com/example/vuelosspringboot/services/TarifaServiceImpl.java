package com.example.vuelosspringboot.services;

import com.example.vuelosspringboot.entities.Tarifa;
import com.example.vuelosspringboot.repositories.BaseRepository;
import com.example.vuelosspringboot.repositories.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TarifaServiceImpl extends BaseServiceImpl<Tarifa, Long> implements TarifaService {
    @Autowired
    private TarifaRepository tarifaRepository;

    public TarifaServiceImpl(BaseRepository<Tarifa, Long> baseRepository, TarifaRepository tarifaRepository) {
        super(baseRepository);
        this.tarifaRepository = tarifaRepository;
    }
}
