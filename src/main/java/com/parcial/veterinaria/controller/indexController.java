package com.parcial.veterinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({ "/", "/index" })
public class indexController {

	@RequestMapping
	public String inicio() {
		return "home";
	}

	@GetMapping("/nosotros")
	public String nosotros() {
		return "nosotros";
	}

	@GetMapping("/veterinario")
	public String vistaVeterinario() {
		return "view-veterinario";
	}

	@GetMapping("/admin")
	public String vistaAdministrador() {
		return "view-admin";
	}

	@GetMapping("/dueno")
	public String vistaDueño() {
		return "view-dueño";
	}
}
