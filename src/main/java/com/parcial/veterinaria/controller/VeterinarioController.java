package com.parcial.veterinaria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.parcial.veterinaria.exception.NotFoundException;
import com.parcial.veterinaria.model.Veterinario;
import com.parcial.veterinaria.repository.VeterinarioRepository;

@Controller
@RequestMapping("/veterinarios")
public class VeterinarioController {

	@Autowired
	VeterinarioRepository repository;
	
	@RequestMapping
	public String veterinariosListTemplate(Model model) {
		model.addAttribute("veterinarios", repository.findAll());
		return "lista-veterinarios";
	}
	
	@GetMapping("/edit/{cedula}")
	public String veterinariosEditTemplate(@PathVariable("cedula") String cedula, Model model) {
		model.addAttribute("veterinario", repository.findById(cedula).orElseThrow(() -> new NotFoundException("Veterinario no encontrado")));
		model.addAttribute("titulo", "Editar veterinario");
		return "veterinarios-form";
	}
	
	@GetMapping("/new")
	public String veterinariosNewTemplate(Model model) {
		model.addAttribute("veterinario", new Veterinario());
		model.addAttribute("titulo", "Agregar veterinario");
		return "veterinarios-form";
	}
	
	@PostMapping("/save")
	public String veterinariosSave(@ModelAttribute("veterinarios") Veterinario veterinario) {
		if(veterinario.getCedula().isEmpty()) {
			veterinario.setCedula(null);
		}
		
		repository.save(veterinario);
		return "redirect:/veterinarios";
	}
	
	@GetMapping("/delete/{cedula}")
	public String deleteVeterinario(@PathVariable("cedula") String cedula) {
		repository.deleteById(cedula);
		return "redirect:/veterinarios";
	}	
}
