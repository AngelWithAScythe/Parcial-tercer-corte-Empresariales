package com.parcial.veterinaria.model;

import java.util.Base64;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "mascotas")
public class Mascota {

	@Id
	private String id;

	private String nombre;

	private String especie;

	private String raza;

	private int edad;

	private String sexo;
	
	private byte[] foto;

	@DocumentReference
	private Veterinario veterinario;

	@DocumentReference
	private Dueño dueño;
	
	@DocumentReference
	private List<Vacuna> vacunas;
	
	@DocumentReference
	private List<Procedimiento> procedimientos;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public String getRaza() {
		return raza;
	}

	public void setRaza(String raza) {
		this.raza = raza;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
	
	public String getFotoBase64() {
        return foto != null ? Base64.getEncoder().encodeToString(foto) : null;
    }

	public Veterinario getVeterinario() {
		return veterinario;
	}

	public void setVeterinario(Veterinario veterinario) {
		this.veterinario = veterinario;
	}

	public Dueño getDueño() {
		return dueño;
	}

	public void setDueño(Dueño dueño) {
		this.dueño = dueño;
	}

	public List<Vacuna> getVacunas() {
		return vacunas;
	}

	public void setVacunas(List<Vacuna> vacunas) {
		this.vacunas = vacunas;
	}

	public List<Procedimiento> getProcedimientos() {
		return procedimientos;
	}

	public void setProcedimientos(List<Procedimiento> procedimientos) {
		this.procedimientos = procedimientos;
	}

}
