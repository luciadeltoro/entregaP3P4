package org.example.entregap3p4;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/** DTO de entrada del formulario (no es una entidad de base de datos). */
public class SolicitudPrestamoForm {

    // Propiedades de instancia del DTO, Spring MVC las rellena con el binding

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 60, message = "El nombre no puede superar los 60 caracteres.")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "El formato del correo no es válido.")
    @Size(max = 120, message = "El correo no puede superar los 120 caracteres.")
    private String email;

    // Código del material, que exista en el catálogo se comprueba en el controlador
    @NotBlank(message = "Debes seleccionar un material.")
    private String material;

    // Integer para que vacío llegue como null y lo detecte @NotNull
    @NotNull(message = "Las unidades son obligatorias.")
    @Min(value = 1, message = "Las unidades deben ser como mínimo 1.")
    @Max(value = 3, message = "Las unidades deben ser como máximo 3.")
    private Integer unidades;

    // Formato ISO , el mismo que envían los controles HTML de fecha
    @NotNull(message = "La fecha de recogida es obligatoria.")
    @FutureOrPresent(message = "La fecha de recogida debe ser hoy o futura.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaRecogida;

    @NotNull(message = "La fecha de devolución es obligatoria.")
    @FutureOrPresent(message = "La fecha de devolución debe ser hoy o futura.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaDevolucion;

    @NotBlank(message = "El motivo es obligatorio.")
    @Size(min = 30, max = 400, message = "El motivo debe tener entre 30 y 400 caracteres.")
    private String motivo;

    @AssertTrue(message = "Debes aceptar las normas para enviar la solicitud.")
    private boolean normas;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    public Integer getUnidades() { return unidades; }
    public void setUnidades(Integer unidades) { this.unidades = unidades; }
    public LocalDate getFechaRecogida() { return fechaRecogida; }
    public void setFechaRecogida(LocalDate fechaRecogida) { this.fechaRecogida = fechaRecogida; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public boolean isNormas() { return normas; }
    public void setNormas(boolean normas) { this.normas = normas; }
}