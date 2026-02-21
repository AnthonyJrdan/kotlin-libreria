package com.libreria.apirest.repositories;

import com.libreria.apirest.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsByNombre(String nombre);

    // Consulta para definir rol por usuario
    // lista de roles
    // Userroles viene de la variable de la entidad rol

    //1ra forma
    //@Query("SELECT r from role r JOIN r.userRoles ur WHERE ur.user.id = :userId")
    //List<Role> findRolesByUserId(@Param("userId") Long userId);

    List<Role> findAllByUserRoles_User_Id(Long idUser);
}
