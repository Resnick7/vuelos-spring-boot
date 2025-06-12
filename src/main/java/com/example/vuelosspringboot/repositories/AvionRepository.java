package com.example.vuelosspringboot.repositories;

import com.example.vuelosspringboot.entities.Avion;
import org.springframework.stereotype.Repository;

@Repository
public interface AvionRepository extends BaseRepository<Avion, Long> {
}
