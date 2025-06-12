package com.example.vuelosspringboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;

@Entity
@Table(name = "Fecha")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Fecha extends Base{
    @Temporal(TemporalType.DATE)
    private Date fecha;
}
