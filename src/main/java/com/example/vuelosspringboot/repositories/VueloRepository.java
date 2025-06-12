package com.example.vuelosspringboot.repositories;

import com.example.vuelosspringboot.entities.Vuelo;
import org.springframework.stereotype.Repository;

@Repository
public interface VueloRepository extends BaseRepository<Vuelo, Long> {
}
