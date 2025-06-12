package com.example.vuelosspringboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "Asiento")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Asiento extends Base{
    @Column(name = "filaAsiento")
    private int filaAsiento;

    @Column(name = "letraAsiento")
    private char letraAsiento;

    @Enumerated(EnumType.STRING)
    private Clase claseAsiento;
}
