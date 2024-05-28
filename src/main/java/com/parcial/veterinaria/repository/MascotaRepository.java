package com.parcial.veterinaria.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.parcial.veterinaria.model.Mascota;
import com.parcial.veterinaria.model.Procedimiento;
import com.parcial.veterinaria.model.Vacuna;

@Repository
public interface MascotaRepository extends MongoRepository<Mascota, String> {

	public List<Mascota> findByDueñoCedula(String cedula);
	
	public List<Mascota> findByVeterinarioCedula(String cedula);
	
	public Mascota findByVacunasContains(Vacuna vacuna);
	
	public Mascota findByProcedimientosContains(Procedimiento procedimiento);
}
