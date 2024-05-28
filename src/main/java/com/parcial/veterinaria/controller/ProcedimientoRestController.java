package com.parcial.veterinaria.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

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
import com.parcial.veterinaria.model.Procedimiento;
import com.parcial.veterinaria.repository.ProcedimientoRepository;

@RestController
@RequestMapping(value = "/api/procedimientos")
public class ProcedimientoRestController {

	@Autowired
	ProcedimientoRepository repository;
	
	@GetMapping("/")
	public List<Procedimiento> getAllVacunas(){
		return repository.findAll();
	}
	
	@GetMapping("/{id}")
	public Procedimiento getProcedimientoById(@PathVariable String id) {
		return repository.findById(id).orElseThrow(() -> new NotFoundException("Procedimiento no encontrada"));
	}
	
	@PostMapping("/")
	public Procedimiento saveProcedimiento(@RequestBody Map<String, Object> body) {
	
		ObjectMapper mapper = new ObjectMapper();
		Procedimiento procedimiento = mapper.convertValue(body, Procedimiento.class);
		
		String nombre = (String) body.get("nombre");
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'nombre' es obligatorio y no puede estar vacío");
		}
		
		procedimiento.setNombre(nombre);
		
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
	        procedimiento.setFecha(fecha);
	    } catch (DateTimeParseException e) {
	        throw new IllegalArgumentException("El campo 'fecha' debe estar en formato ISO de fecha (YYYY-MM-DD).");
	    }
		
		String tipo = (String) body.get("tipo");
		if(tipo == null || tipo.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'tipo' es obligatorio y no puede estar vacío");
		}
		
		procedimiento.setTipo(tipo);
		
		String descripcion = (String) body.get("descripcion");
		if(descripcion == null || tipo.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'procedimiento' es obligatorio y no puede estar vacío");
		}
		
		procedimiento.setDescripcion(descripcion);
		
		return repository.save(procedimiento);
	}
	
	@PutMapping("/{id}")
	public Procedimiento updateProcedimiento(@PathVariable String id, @RequestBody Map<String, Object> body) {
		ObjectMapper mapper = new ObjectMapper();
		Procedimiento procedimiento = mapper.convertValue(body, Procedimiento.class);
		procedimiento.setId(id);
		return repository.save(procedimiento);
	}
	
	@DeleteMapping("/{id}")
	public Procedimiento deleteProcedimiento(@PathVariable String id) {
		Procedimiento procedimiento = repository.findById(id).orElseThrow(() -> new NotFoundException("Procedimiento no encontrada"));
		repository.deleteById(id);
		return procedimiento;
	}
	
}
