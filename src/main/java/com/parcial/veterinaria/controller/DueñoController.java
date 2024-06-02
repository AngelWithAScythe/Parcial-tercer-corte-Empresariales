package com.parcial.veterinaria.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
	public String dueñosListTemplate(Model model) {
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
	
	@Transactional
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
	
	@PostMapping("/foto/{id}")
	public String subirFotoDueño(@RequestParam("foto") MultipartFile foto, @PathVariable("id") String id) {
		Dueño dueño = repository.findById(id).orElseThrow(() -> new NotFoundException("Dueño no encontrado"));
		try {
			if(!foto.isEmpty()) {
				byte[] fotoBytes = foto.getBytes();
				dueño.setFoto(fotoBytes);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		repository.save(dueño);
		
		return "redirect:/dueno";
	}
	
}
