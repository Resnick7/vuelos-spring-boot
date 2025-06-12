package com.example.vuelosspringboot.repositories;

import com.example.vuelosspringboot.entities.Ciudad;
import org.springframework.stereotype.Repository;

@Repository
public interface CiudadRepository extends BaseRepository<Ciudad, Long> {
}
