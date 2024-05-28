package com.parcial.veterinaria.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.parcial.veterinaria.model.Dueño;

@Repository
public interface DueñoRepository extends MongoRepository<Dueño, String>{

	public Dueño findByUsuario(String usuario);
}
