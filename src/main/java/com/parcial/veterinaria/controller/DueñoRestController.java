package com.parcial.veterinaria.controller;

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
import com.parcial.veterinaria.model.Dueño;
import com.parcial.veterinaria.repository.DueñoRepository;

@RestController
@RequestMapping(value="/api/dueños")
public class DueñoRestController {

	@Autowired
	DueñoRepository repository;
	
	@GetMapping("/")
	public List<Dueño> getAllDueños(){
		return repository.findAll();
	}
	
	@GetMapping("/{cedula}")
	public Dueño getDueñoById(@PathVariable String cedula) {
		return repository.findById(cedula).orElseThrow(() -> new NotFoundException("Dueño no encontrado"));
	}
	
	@PostMapping("/")
	public Dueño saveDueño(@RequestBody Map<String, Object> body) {
		ObjectMapper mapper = new ObjectMapper();
		Dueño dueño = mapper.convertValue(body, Dueño.class);	
		
		String cedula = (String) body.get("cedula");
		if (cedula == null || cedula.trim().isEmpty()) {
	        throw new IllegalArgumentException("El campo 'cedula' es obligatorio y no puede estar vacío.");
	    }
		
		dueño.setCedula(cedula);
		
		String nombre = (String) body.get("nombre");
		if (nombre == null || nombre.trim().isEmpty()) {
	        throw new IllegalArgumentException("El campo 'nombre' es obligatorio y no puede estar vacío.");
	    }
		
		dueño.setNombre(nombre);
		
		String apellido = (String) body.get("apellido");
		if(apellido == null || apellido.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'apellido' es obligatorio y no puede estar vacío");
		}
		
		dueño.setApellido(apellido);
		
		String usuario = (String) body.get("usuario");
		
		if(usuario == null || usuario.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'usuario' es obligatorio y no puede estar vacío");
		}

		dueño.setUsuario(usuario);
		
		Long telefono = (Long) body.get("telefono");
		
		if (telefono < 0) {
			throw new IllegalArgumentException("El campo 'telefono' debe tener un valor valido");
		}
		
		dueño.setTelefono(telefono);
		
		String direccion = (String) body.get("direccion");
		
		if(direccion == null || direccion.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'direccion' es obligatorio y no puede estar vacío");
		}
		
		dueño.setDireccion(direccion);
		
		String contraseña = (String) body.get("contraseña");
		
		if(contraseña == null || contraseña.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'contraseña' es obligatorio y no puede estar vacío");
		}
		
		dueño.setContraseña(contraseña);
		
		return repository.save(dueño);
	}
	
	@PutMapping("/{cedula}")
	public Dueño upadteDueño(@PathVariable String cedula, @RequestBody Map<String, Object> body) {
		ObjectMapper mapper = new ObjectMapper();
		Dueño dueño = mapper.convertValue(body, Dueño.class);
		dueño.setCedula(cedula);
		return repository.save(dueño);
	}

	@DeleteMapping("/{cedula}")
	public Dueño deleteDueño(@PathVariable String cedula) {
		Dueño dueño = repository.findById(cedula).orElseThrow(() -> new NotFoundException("Dueño no encontrado"));
		repository.deleteById(cedula);
		return dueño;
	}
}
