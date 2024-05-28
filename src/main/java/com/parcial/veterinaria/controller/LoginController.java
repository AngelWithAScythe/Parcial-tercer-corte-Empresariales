package com.parcial.veterinaria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.parcial.veterinaria.model.Administrador;
import com.parcial.veterinaria.model.Dueño;
import com.parcial.veterinaria.model.Veterinario;
import com.parcial.veterinaria.repository.AdministradorRepository;
import com.parcial.veterinaria.repository.DueñoRepository;
import com.parcial.veterinaria.repository.VeterinarioRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired
	VeterinarioRepository vRepository;
	@Autowired
	AdministradorRepository aRepository;
	@Autowired
	DueñoRepository dRepository;

	@GetMapping("/login/{tipo}")
	public String login(@PathVariable("tipo") String tipo, Model model) {
		model.addAttribute("tipo", tipo);
		return "login";
	}

	@PostMapping("/loginVeterinario")
	public String loginVeterinario(@RequestParam("usuario") String usuario,
			@RequestParam("contraseña") String contraseña, HttpSession session, Model model) {
		Veterinario veterinario = vRepository.findByUsuario(usuario);
		if (veterinario != null && veterinario.getContraseña().equals(contraseña)) {
			session.setAttribute("usuario", veterinario);
			return "redirect:/veterinario";
		} else {
			model.addAttribute("error", "Usuario o contraseña invalidos");
			return "login";
		}
	}

	@PostMapping("/loginAdministrador")
	public String loginAdministrador(@RequestParam("usuario") String usuario,
			@RequestParam("contraseña") String contraseña, HttpSession session, Model model) {
		Administrador administrador = aRepository.findByUsuario(usuario);
		if (administrador != null && administrador.getContraseña().equals(contraseña)) {
			session.setAttribute("usuario", administrador);
			return "redirect:/admin";
		} else {
			model.addAttribute("error", "Usuario o contraseña invalidos");
			return "login";
		}
	}

	@PostMapping("/loginDueno")
	public String loginDueño(@RequestParam("usuario") String usuario, @RequestParam("contraseña") String contraseña,
			HttpSession session, Model model) {
		Dueño dueño = dRepository.findByUsuario(usuario);
		if (dueño != null && dueño.getContraseña().equals(contraseña)) {
			session.setAttribute("usuario", dueño);
			return "redirect:/dueno";
		} else {
			model.addAttribute("error", "Usuario o contraseña invalidos");
			return "login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
