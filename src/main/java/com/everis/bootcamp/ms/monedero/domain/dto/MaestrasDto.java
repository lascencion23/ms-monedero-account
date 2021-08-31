package com.everis.bootcamp.ms.monedero.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaestrasDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private TypeMaestro type;

    private String valor;

    public enum TypeMaestro {
        TYPEDOCUMENT,
        OPERADORA
    }

}
