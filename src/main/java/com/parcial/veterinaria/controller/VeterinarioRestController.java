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
import com.parcial.veterinaria.model.Veterinario;
import com.parcial.veterinaria.repository.VeterinarioRepository;

@RestController
@RequestMapping(value="/api/veterinarios")
public class VeterinarioRestController {

	@Autowired
	VeterinarioRepository repository;
	
	@GetMapping("/")
	public List<Veterinario> getAllVeterinarios(){
		return repository.findAll();
	}
	
	@GetMapping("/{cedula}")
	public Veterinario getVeterinarioById(@PathVariable String cedula) {
		return repository.findById(cedula).orElseThrow(() -> new NotFoundException("Veterinario no encontrado"));
	}
	
	@PostMapping("/")
	public Veterinario saveVeterinario(@RequestBody Map<String, Object> body) {
		ObjectMapper mapper = new ObjectMapper();
		Veterinario veterinario = mapper.convertValue(body, Veterinario.class);	
		
		String cedula = (String) body.get("cedula");
		if (cedula == null || cedula.trim().isEmpty()) {
	        throw new IllegalArgumentException("El campo 'cedula' es obligatorio y no puede estar vacío.");
	    }
		
		veterinario.setCedula(cedula);
		
		String nombre = (String) body.get("nombre");
		if (nombre == null || nombre.trim().isEmpty()) {
	        throw new IllegalArgumentException("El campo 'nombre' es obligatorio y no puede estar vacío.");
	    }
		
		veterinario.setNombre(nombre);
		
		String apellido = (String) body.get("apellido");
		if(apellido == null || apellido.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'apellido' es obligatorio y no puede estar vacío");
		}
		
		veterinario.setApellido(apellido);
		
		String usuario = (String) body.get("usuario");
		
		if(usuario == null || usuario.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'usuario' es obligatorio y no puede estar vacío");
		}
		
		veterinario.setUsuario(usuario);
		
		String contraseña = (String) body.get("contraseña");
		
		if(contraseña == null || contraseña.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'contraseña' es obligatorio y no puede estar vacío");
		}
		
		veterinario.setContraseña(contraseña);
		
		return repository.save(veterinario);
	}
	
	@PutMapping("/{cedula}")
	public Veterinario upadteVeterinario(@PathVariable String cedula, @RequestBody Map<String, Object> body) {
		ObjectMapper mapper = new ObjectMapper();
		Veterinario veterinario = mapper.convertValue(body, Veterinario.class);
		veterinario.setCedula(cedula);
		return repository.save(veterinario);
	}

	@DeleteMapping("/{cedula}")
	public Veterinario deleteVeterinario(@PathVariable String cedula) {
		Veterinario veterinario = repository.findById(cedula).orElseThrow(() -> new NotFoundException("Veterinario no encontrado"));
		repository.deleteById(cedula);
		return veterinario;
	}
}
