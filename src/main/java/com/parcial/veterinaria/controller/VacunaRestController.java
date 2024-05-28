package com.parcial.veterinaria.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parcial.veterinaria.exception.NotFoundException;
import com.parcial.veterinaria.model.Vacuna;
import com.parcial.veterinaria.repository.VacunaRepository;

@RestController
@RequestMapping(value = "/api/vacunas")
public class VacunaRestController {

	@Autowired
	VacunaRepository repository;
	
	@GetMapping("/")
	public List<Vacuna> getAllVacunas(){
		return repository.findAll();
	}
	
	@GetMapping("/{id}")
	public Vacuna getVacunaById(@PathVariable String id) {
		return repository.findById(id).orElseThrow(() -> new NotFoundException("Vacuna no encontrada"));
	}
	
	@PostMapping("/")
	public Vacuna saveVacuna(@RequestBody Map<String, Object> body) {
	
		ObjectMapper mapper = new ObjectMapper();
		Vacuna vacuna = mapper.convertValue(body, Vacuna.class);
		
		String nombre = (String) body.get("nombre");
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'nombre' es obligatorio y no puede estar vacío");
		}
		
		vacuna.setNombre(nombre);
		
		if(!body.containsKey("fecha")) {
			throw new IllegalArgumentException("El campo 'fecha' es obligatorio y no puede estar vacío");
		}
		
		Object fechaObject = body.get("fecha");
		
		if(fechaObject == null) {
	        throw new IllegalArgumentException("El campo 'fecha' no puede estar vacío.");
		}
		
		if(!(fechaObject instanceof String)) {
	        throw new IllegalArgumentException("El campo 'fecha' debe ser una cadena en formato ISO de fecha (YYYY-MM-DD).");
		}
		
		try {
	        LocalDate fecha = LocalDate.parse((String) fechaObject);
	        vacuna.setFecha(fecha);
	    } catch (DateTimeParseException e) {
	        throw new IllegalArgumentException("El campo 'fecha' debe estar en formato ISO de fecha (YYYY-MM-DD).");
	    }
		
		String tipo = (String) body.get("tipo");
		if(tipo == null || tipo.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'tipo' es obligatorio y no puede estar vacío");
		}
		
		vacuna.setTipo(tipo);
		
		return repository.save(vacuna);
	}
	
	@PutMapping("/{id}")
	public Vacuna updateVacuna(@PathVariable String id, @RequestBody Map<String, Object> body) {
		ObjectMapper mapper = new ObjectMapper();
		Vacuna vacuna = mapper.convertValue(body, Vacuna.class);
		vacuna.setId(id);
		return repository.save(vacuna);
	}
	
	@DeleteMapping("/{id}")
	public Vacuna deleteVacuna(@PathVariable String id) {
		Vacuna vacuna = repository.findById(id).orElseThrow(() -> new NotFoundException("Vacuna no encontrada"));
		repository.deleteById(id);
		return vacuna;
	}
	
}
