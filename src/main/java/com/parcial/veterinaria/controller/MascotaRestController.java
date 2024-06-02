package com.parcial.veterinaria.controller;

import java.util.ArrayList;
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
import com.parcial.veterinaria.model.Mascota;
import com.parcial.veterinaria.model.Vacuna;
import com.parcial.veterinaria.model.Veterinario;
import com.parcial.veterinaria.repository.DueñoRepository;
import com.parcial.veterinaria.repository.MascotaRepository;
import com.parcial.veterinaria.repository.VacunaRepository;
import com.parcial.veterinaria.repository.VeterinarioRepository;

@RestController
@RequestMapping(value = "/api/mascotas")
public class MascotaRestController {

	@Autowired
	MascotaRepository repository;
	@Autowired
	DueñoRepository dRepository;
	@Autowired
	VeterinarioRepository vRepository;
	@Autowired
	VacunaRepository vaRepository;

	@GetMapping("/")
	public List<Mascota> getAllMascotas() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Mascota getMascotaById(@PathVariable String id) {
		return repository.findById(id).orElseThrow(() -> new NotFoundException("Mascota no encontrada"));
	}

	@PostMapping("/")
	public Mascota saveMascota(@RequestBody Map<String, Object> body) {
		ObjectMapper mapper = new ObjectMapper();
		Mascota mascota = mapper.convertValue(body, Mascota.class);

		String nombre = (String) body.get("nombre");
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'nombre' es obligatorio y no puede estar vacío.");
		}

		mascota.setNombre(nombre);

		String especie = (String) body.get("especie");
		if (especie == null || especie.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'especie' es obligatorio y no puede estar vacío");
		}

		mascota.setEspecie(especie);

		String raza = (String) body.get("raza");

		if (raza == null || raza.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'raza' es obligatorio y no puede estar vacío");
		}

		mascota.setRaza(raza);

		int edad = (Integer) body.get("edad");

		if (edad < 0) {
			throw new IllegalArgumentException("El campo 'edad' debe tener un valor valido");
		}

		mascota.setEdad(edad);

		String sexo = (String) body.get("sexo");

		if (sexo == null || sexo.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'sexo' es obligatorio y no puede estar vacío");
		}

		mascota.setSexo(sexo);

		Object dueñoObject = body.get("dueño");

		if (dueñoObject instanceof String) {
			String dueñoCedula = (String) dueñoObject;
			Dueño dueño = dRepository.findById(dueñoCedula)
					.orElseThrow(() -> new NotFoundException("Dueño no encontrado"));
			mascota.setDueño(dueño);
		} else {
			throw new IllegalArgumentException("La propiedad 'dueño' debe ser una cadena");
		}
		
		Object veterinarioObject = body.get("veterinario");

		if (veterinarioObject instanceof String) {
			String veterinarioCedula = (String) veterinarioObject;
			Veterinario veterinario = vRepository.findById(veterinarioCedula)
					.orElseThrow(() -> new NotFoundException("Veterinario no encontrado"));
			mascota.setVeterinario(veterinario);
		} else {
			throw new IllegalArgumentException("La propiedad 'veterinario' debe ser una cadena");
		}
		
		mascota.setVacunas(null);
		mascota.setProcedimientos(null);
		mascota.setFoto(null);
		
		return repository.save(mascota);
	}

	@PutMapping("/{id}")
	public Mascota upadteMascota(@PathVariable String id, @RequestBody Map<String, Object> body) {
		ObjectMapper mapper = new ObjectMapper();
		Mascota mascota = mapper.convertValue(body, Mascota.class);
		mascota.setId(id);
		return repository.save(mascota);
	}

	@DeleteMapping("/{id}")
	public Mascota deleteMascota(@PathVariable String id) {
		Mascota mascota = repository.findById(id).orElseThrow(() -> new NotFoundException("Mascota no encontrada"));
		repository.deleteById(id);
		return mascota;
	}
	
	@PutMapping("/añadirVacuna/{id}/{idVacuna}")
	public Mascota añadirVacuna(@PathVariable String id, @PathVariable String idVacuna) {
		Mascota mascota = repository.findById(id).orElseThrow(() -> new NotFoundException("Mascota no encontrada"));
		Vacuna vacuna = vaRepository.findById(idVacuna).orElseThrow(() -> new NotFoundException("Vacuna no encontrada"));
		
		if(mascota.getVacunas() == null) {
			mascota.setVacunas(new ArrayList<>());
		}
		
		mascota.getVacunas().add(vacuna);
		return repository.save(mascota);
	}
}
