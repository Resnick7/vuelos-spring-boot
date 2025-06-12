package com.example.vuelosspringboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Ciudad")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Ciudad extends Base{
    @Column(name = "nombreCiudad")
    private String nombreCiudad;
}
