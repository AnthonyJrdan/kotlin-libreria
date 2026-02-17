package com.libreria.apirest.models.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class UserRolId implements Serializable {

    @Column(name = "id_user")
    private Long userId;

    @Column(name = "id_rol")
    private String rolId;

    // Funcion para evitar llaves duplicadas
    @Override
    public boolean equals(Object o) {
        if(this == o) return  true;
        if(!(o instanceof  UserRolId)) return  false;
        UserRolId userRolId = (UserRolId) o;
        return Objects.equals(userId, userRolId.userId) && Objects.equals(rolId, userRolId.rolId);
    }


    // Definir la llave primaria
    @Override
    public int hashCode()
    {
        return  Objects.hash(userId, rolId);
    }
}
