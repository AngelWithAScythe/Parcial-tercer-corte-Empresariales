package com.parcial.veterinaria.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.parcial.veterinaria.model.Administrador;

@Repository
public interface AdministradorRepository extends MongoRepository<Administrador, String> {

	public Administrador findByUsuario(String usuario);
}
