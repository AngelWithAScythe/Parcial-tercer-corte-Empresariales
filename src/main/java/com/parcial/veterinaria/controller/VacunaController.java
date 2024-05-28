package com.parcial.veterinaria.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.parcial.veterinaria.exception.NotFoundException;
import com.parcial.veterinaria.model.Mascota;
import com.parcial.veterinaria.model.Vacuna;
import com.parcial.veterinaria.model.Veterinario;
import com.parcial.veterinaria.repository.VacunaRepository;

import jakarta.servlet.http.HttpSession;

import com.parcial.veterinaria.repository.MascotaRepository;

@Controller
@RequestMapping("/vacunas")
public class VacunaController {

	@Autowired
	VacunaRepository repository;
	@Autowired
	MascotaRepository mRepository;

	@GetMapping("/edit/{id}")
	public String vacunasEditTemplate(@PathVariable("id") String id, Model model) {
		model.addAttribute("vacuna",
				repository.findById(id).orElseThrow(() -> new NotFoundException("Vacuna no encontrada")));
		model.addAttribute("titulo", "Editar vacuna");
		return "vacunas-form";
	}

	@GetMapping("/new/{id}")
	public String vacunasNewTemplate(Model model, @PathVariable("id") String id) {
		model.addAttribute("vacuna", new Vacuna());
		model.addAttribute("titulo", "Agregar vacuna");
		model.addAttribute("mascota",
				mRepository.findById(id).orElseThrow(() -> new NotFoundException("Mascota no encontrada")));
		return "vacunas-form";
	}

	@PostMapping("/save/{id}")
	public String vacunasSave(@ModelAttribute("vacunas") Vacuna vacuna, @PathVariable("id") String id) {
		if (vacuna.getId().isEmpty()) {
			vacuna.setId(null);
		}

		Mascota mascota = mRepository.findById(id).orElseThrow(() -> new NotFoundException("Mascota no encontrada"));

		repository.save(vacuna);
		return "redirect:/mascotas/anadirVacuna/" + mascota.getId() + "/" + vacuna.getId();
	}

	@GetMapping("/delete/{id}")
	@Transactional
	public String deleteVacuna(@PathVariable("id") String id, HttpSession session) {
		Vacuna vacuna = repository.findById(id).orElseThrow(() -> new NotFoundException("Vacuna no encontrada"));
		Veterinario veterinario = (Veterinario) session.getAttribute("usuario");
		Mascota mascota = mRepository.findByVacunasContains(vacuna);
		
			if(mascota.getVacunas().contains(vacuna)) {
				mascota.getVacunas().removeIf(v -> v.getId().equals(vacuna.getId()));
				mRepository.save(mascota);
		}
		repository.deleteById(vacuna.getId());
		return "redirect:/mascotas/"+veterinario.getCedula();
	}

}
