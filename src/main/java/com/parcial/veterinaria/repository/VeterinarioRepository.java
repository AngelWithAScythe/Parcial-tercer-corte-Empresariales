package com.parcial.veterinaria.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.parcial.veterinaria.model.Veterinario;

@Repository
public interface VeterinarioRepository extends MongoRepository<Veterinario, String>{

	public Veterinario findByUsuario(String usuario);
}
