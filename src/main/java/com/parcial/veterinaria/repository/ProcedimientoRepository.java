package com.parcial.veterinaria.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.parcial.veterinaria.model.Procedimiento;

@Repository
public interface ProcedimientoRepository extends MongoRepository<Procedimiento, String>{

}
