package com.parcial.veterinaria.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.parcial.veterinaria.exception.NotFoundException;
import com.parcial.veterinaria.model.Dueño;
import com.parcial.veterinaria.model.Mascota;
import com.parcial.veterinaria.model.Procedimiento;
import com.parcial.veterinaria.model.Vacuna;
import com.parcial.veterinaria.model.Veterinario;
import com.parcial.veterinaria.repository.DueñoRepository;
import com.parcial.veterinaria.repository.MascotaRepository;
import com.parcial.veterinaria.repository.ProcedimientoRepository;
import com.parcial.veterinaria.repository.VacunaRepository;
import com.parcial.veterinaria.repository.VeterinarioRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mascotas")
public class MascotaController {

	@Autowired
	MascotaRepository repository;
	@Autowired
	DueñoRepository dRepository;
	@Autowired
	VacunaRepository vaRepository;
	@Autowired
	VeterinarioRepository vRepository;
	@Autowired
	ProcedimientoRepository pRepository;
	
	@GetMapping("/{cedula}")
	public String mascotasListTemplate(@PathVariable("cedula") String cedula, Model model) {
		model.addAttribute("mascotas", repository.findByVeterinarioCedula(cedula));
		return "lista-mascotas";
	}
	
	@GetMapping("/lista/{cedula}")
	public String mascotasForDueño(@PathVariable("cedula") String cedula, Model model) {
		model.addAttribute("mascotas", repository.findByDueñoCedula(cedula));
		return "mis-mascotas";
	}
	
	@GetMapping("/vacunas/{id}")
	@ResponseBody
	public List<Vacuna> getVacunas(@PathVariable("id") String id){
		Mascota mascota = repository.findById(id).orElseThrow(() -> new NotFoundException("Mascota no encontrada"));
		return mascota.getVacunas();
	}
	
	@GetMapping("/procedimientos/{id}")
	@ResponseBody
	public List<Procedimiento> getProcedimientos(@PathVariable("id") String id){
		Mascota mascota = repository.findById(id).orElseThrow(() -> new NotFoundException("Mascota no encontrada"));
		return mascota.getProcedimientos();
	}
	
	@GetMapping("/edit/{id}")
	public String mascotasEditTemplate(@PathVariable("id") String id, Model model) {
		model.addAttribute("mascota", repository.findById(id).orElseThrow(() -> new NotFoundException("Mascota no encontrada")));
		model.addAttribute("titulo", "Editar mascota");
		model.addAttribute("sexos", List.of("MACHO", "HEMBRA"));
		return "mascotas-form";
	}
	
	@GetMapping("/new/{dueñoCedula}")
	public String mascotasNewTemplate(@PathVariable("dueñoCedula") String dueñoCedula, Model model) {
		
		Dueño dueño = dRepository.findById(dueñoCedula).orElseThrow(() -> new NotFoundException("Dueño no encontrado"));
		model.addAttribute("mascota", new Mascota());
		model.addAttribute("titulo", "Agregar mascota");
		model.addAttribute("sexos", List.of("MACHO", "HEMBRA"));
		model.addAttribute("dueño", dueño);
		return "mascotas-form";
	}
	
	@PostMapping("/save/{dueñoCedula}/{veterinarioCedula}")
	public String mascotasSave(@ModelAttribute("mascotas") Mascota mascota, @PathVariable("dueñoCedula") String dueñoCedula, @PathVariable("veterinarioCedula") String veterinarioCedula) {
		if(mascota.getId().isEmpty()) {
			mascota.setId(null);
		}
		Dueño dueño = dRepository.findById(dueñoCedula).orElseThrow(() -> new IllegalArgumentException("Dueño no encontrado"+dueñoCedula));
		Veterinario veterinario = vRepository.findById(veterinarioCedula).orElseThrow(() -> new IllegalArgumentException("Veterinario no encontrado"));
	    mascota.setDueño(dueño);
	    mascota.setVeterinario(veterinario);
		repository.save(mascota);
		return "redirect:/mascotas/"+veterinario.getCedula();
	}
	
	@GetMapping("/delete/{id}")
	public String deleteMascota(@PathVariable("id") String id, HttpSession session) {
		repository.deleteById(id);
		Veterinario veterinario = (Veterinario) session.getAttribute("usuario");
		return "redirect:/mascotas/"+veterinario.getCedula();
	}	
	
	@GetMapping("/vacuna/{id}")
	public String registrarVacuna(@PathVariable("id") String id) {
		Mascota mascota = repository.findById(id).orElseThrow(() -> new NotFoundException("Mascota no encontrada"));
		return "redirect:/vacunas/new/"+mascota.getId();
	}
	
	@GetMapping("/procedimiento/{id}")
	public String registrarProcedimiento(@PathVariable("id") String id) {
		Mascota mascota = repository.findById(id).orElseThrow(() -> new NotFoundException("Mascota no encontrada"));
		return "redirect:/procedimientos/new/"+mascota.getId();
	}
	
	@GetMapping("/anadirVacuna/{id}/{idVacuna}")
	public String añadirVacuna(@PathVariable("id") String id, @PathVariable("idVacuna") String idVacuna, HttpSession session) {
		Mascota mascota = repository.findById(id).orElseThrow(() -> new NotFoundException("Mascota no encontrada"));
		Vacuna vacuna = vaRepository.findById(idVacuna).orElseThrow(() -> new NotFoundException("Vacuna no encontrada"));
		Veterinario veterinario = (Veterinario) session.getAttribute("usuario");
		if(mascota.getVacunas() == null) {
			mascota.setVacunas(new ArrayList<>());
		}
		
		mascota.getVacunas().add(vacuna);
		repository.save(mascota);
		
		return "redirect:/mascotas/"+veterinario.getCedula();
	}
	
	@GetMapping("/anadirProcedimiento/{id}/{idProcedimiento}")
	public String añadirProcedimiento(@PathVariable("id") String id, @PathVariable("idProcedimiento") String idProcedimiento, HttpSession session) {
		Mascota mascota = repository.findById(id).orElseThrow(() -> new NotFoundException("Mascota no encontrada"));
		Procedimiento procedimiento = pRepository.findById(idProcedimiento).orElseThrow(() -> new NotFoundException("Procedimiento no encontrado"));
		Veterinario veterinario = (Veterinario) session.getAttribute("usuario");
		if(mascota.getProcedimientos() == null) {
			mascota.setProcedimientos(new ArrayList<>());
		}
		
		mascota.getProcedimientos().add(procedimiento);
		repository.save(mascota);
		
		return "redirect:/mascotas/"+veterinario.getCedula();
	}	
	
	@PostMapping("/foto/{id}")
	public String subirFotoMascota(@RequestParam("foto") MultipartFile foto, @PathVariable("id") String id, HttpSession session) {
		Mascota mascota = repository.findById(id).orElseThrow(() -> new NotFoundException("Mascota no encontrada"));
		try {
            if (!foto.isEmpty()) {
                byte[] fotoBytes = foto.getBytes();
                mascota.setFoto(fotoBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		Dueño dueño = (Dueño) session.getAttribute("usuario");
		
		repository.save(mascota);
		
		return "redirect:/mascotas/lista/" + dueño.getCedula();
	}
}
