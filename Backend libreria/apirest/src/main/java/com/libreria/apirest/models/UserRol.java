package com.libreria.apirest.models;

import com.libreria.apirest.models.id.UserRolId;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "UserRol")
public class UserRol {

    @EmbeddedId
    private UserRolId id = new UserRolId();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @MapsId("rolId")
    @JoinColumn(name = "id_rol")
    private Role role;
}
