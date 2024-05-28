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
import com.parcial.veterinaria.model.Dueño;
import com.parcial.veterinaria.repository.DueñoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/duenos")
public class DueñoController {

	@Autowired
	DueñoRepository repository;
	
	@RequestMapping
	public String dueñossListTemplate(Model model) {
		model.addAttribute("dueños", repository.findAll());
		return "lista-dueños";
	}
	
	@GetMapping("/edit/{cedula}")
	public String dueñosEditTemplate(@PathVariable("cedula") String cedula, Model model) {
		model.addAttribute("dueño", repository.findById(cedula).orElseThrow(() -> new NotFoundException("Veterinario no encontrado")));
		model.addAttribute("titulo", "Editar dueño");
		model.addAttribute("tipo", "antiguo");
		return "dueños-form";
	}
	
	@GetMapping("/new")
	public String dueñosNewTemplate(Model model) {
		model.addAttribute("dueño", new Dueño());
		model.addAttribute("titulo", "Agregar dueño");
		model.addAttribute("tipo", "nuevo");
		return "dueños-form";
	}
	
	@PostMapping("/save")
	public String dueñosSave(@ModelAttribute("dueños") Dueño dueño, HttpSession session) {
		if(dueño.getCedula().isEmpty()) {
			dueño.setCedula(null);
		}
		
		repository.save(dueño);
		session.setAttribute("usuario", dueño);
		return "redirect:/dueno";
	}
	
	@PostMapping("/register")
	public String dueñosRegister(@ModelAttribute("dueños") Dueño dueño) {
		if(dueño.getCedula().isEmpty()) {
			dueño.setCedula(null);
		}
		
		repository.save(dueño);
		return "redirect:/mascotas/new/"+dueño.getCedula();
	}
	
	@GetMapping("/delete/{cedula}")
	public String deleteDueño(@PathVariable("cedula") String cedula) {
		repository.deleteById(cedula);
		return "redirect:/duenos";
	}
	
}
