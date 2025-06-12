package com.example.vuelosspringboot.repositories;

import com.example.vuelosspringboot.entities.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends BaseRepository<Usuario, Long> {
}
