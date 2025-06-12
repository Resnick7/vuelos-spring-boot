package com.example.vuelosspringboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@MappedSuperclass
@DiscriminatorColumn(name = "tipo_persona", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Persona extends Base{
    @Column(name = "dniPersona")
    private int dniPersona;

    @Column(name = "nombrePersona")
    private String nombrePersona;

    @Column(name = "apellidoPersona")
    private String apellidoPersona;
}
