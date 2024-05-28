package com.parcial.veterinaria.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.parcial.veterinaria.model.Vacuna;

@Repository
public interface VacunaRepository extends MongoRepository<Vacuna, String>{

}
