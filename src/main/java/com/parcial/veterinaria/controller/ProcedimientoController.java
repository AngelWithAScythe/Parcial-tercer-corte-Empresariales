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
import com.parcial.veterinaria.model.Procedimiento;
import com.parcial.veterinaria.model.Veterinario;
import com.parcial.veterinaria.repository.MascotaRepository;
import com.parcial.veterinaria.repository.ProcedimientoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/procedimientos")
public class ProcedimientoController {

	@Autowired
	ProcedimientoRepository repository;
	@Autowired
	MascotaRepository mRepository;
	
	@GetMapping("/edit/{id}")
	public String procedimientosEditTemplate(@PathVariable("id") String id, Model model) {
		model.addAttribute("procedimiento",
				repository.findById(id).orElseThrow(() -> new NotFoundException("Procedimiento no encontrada")));
		model.addAttribute("titulo", "Editar Procedimiento");
		return "procedimientos-form";
	}

	@GetMapping("/new/{id}")
	public String procedimientosNewTemplate(Model model, @PathVariable("id") String id) {
		model.addAttribute("procedimiento", new Procedimiento());
		model.addAttribute("titulo", "Agregar procedimiento");
		model.addAttribute("mascota",
				mRepository.findById(id).orElseThrow(() -> new NotFoundException("Mascota no encontrada")));
		return "procedimientos-form";
	}

	@PostMapping("/save/{id}")
	public String procedimientosSave(@ModelAttribute("procedimientos") Procedimiento procedimiento, @PathVariable("id") String id) {
		if (procedimiento.getId().isEmpty()) {
			procedimiento.setId(null);
		}

		Mascota mascota = mRepository.findById(id).orElseThrow(() -> new NotFoundException("Mascota no encontrada"));

		repository.save(procedimiento);
		return "redirect:/mascotas/anadirProcedimiento/" + mascota.getId() + "/" + procedimiento.getId();
	}

	@GetMapping("/delete/{id}")
	@Transactional
	public String deleteProcedimiento(@PathVariable("id") String id, HttpSession session) {
		Procedimiento procedimiento = repository.findById(id).orElseThrow(() -> new NotFoundException("Procedimiento no encontrado"));
		Mascota mascota = mRepository.findByProcedimientosContains(procedimiento);
		Veterinario veterinario = (Veterinario) session.getAttribute("usuario");
		if(mascota.getProcedimientos().contains(procedimiento)) {
			mascota.getProcedimientos().removeIf(p -> p.getId().equals(procedimiento.getId()));
			mRepository.save(mascota);
		}
		repository.deleteById(procedimiento.getId());
		return "redirect:/mascotas/"+veterinario.getCedula();
	}
}
