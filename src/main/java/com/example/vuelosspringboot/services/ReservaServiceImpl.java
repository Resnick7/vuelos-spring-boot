package com.example.vuelosspringboot.services;

import com.example.vuelosspringboot.entities.Reserva;
import com.example.vuelosspringboot.repositories.BaseRepository;
import com.example.vuelosspringboot.repositories.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaServiceImpl extends BaseServiceImpl<Reserva, Long> implements ReservaService {
    @Autowired
    private ReservaRepository reservaRepository;

    public ReservaServiceImpl(BaseRepository<Reserva, Long> baseRepository, ReservaRepository reservaRepository) {
        super(baseRepository);
        this.reservaRepository = reservaRepository;
    }
}
