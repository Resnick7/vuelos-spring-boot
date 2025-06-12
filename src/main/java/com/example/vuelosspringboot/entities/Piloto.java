package com.example.vuelosspringboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@DiscriminatorValue("Piloto")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Piloto extends Persona{
    @Column(name = "numeroPiloto")
    private int numeroPiloto;
}
