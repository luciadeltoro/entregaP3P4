package org.example.entregap3p4;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
public class PrestamoController {

    // Catálogo en memoria, cada fila muestra código, nombre, descripción
    private static final String[][] MATERIALES = {
            {"PORTATIL", "Ordenador portátil", "Incluye un cargador y una funda."},
            {"MICRO", "Kit de microcontrolador", "Incluye una placa y un cable USB."},
            {"CAMARA", "Cámara de documentación", "Incluye un soporte de mesa."}
    };

    @GetMapping("/")
    public String catalogo(Model model) {
        model.addAttribute("materiales", MATERIALES);
        return "catalogo";
    }

    @GetMapping("/materiales/{codigo}")
    public String ficha(@PathVariable String codigo,
                        @RequestParam(defaultValue = "general") String turno,
                        Model model) {
        String[] material = buscarMaterial(codigo);
        if (material == null) {
            // 404 Spring Boot elegirá templates/error/404.html
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("material", material);
        model.addAttribute("turno", turno);
        return "ficha";
    }

    @GetMapping("/solicitud/nueva")
    public String nuevaSolicitud(Model model) {
        model.addAttribute("solicitud", new SolicitudPrestamoForm());
        model.addAttribute("materiales", MATERIALES);
        return "formulario";
    }

    // BindingResult (errores) debe ir justo después del dto validado
    @PostMapping("/solicitudes")
    public String enviarSolicitud(@Valid @ModelAttribute("solicitud") SolicitudPrestamoForm solicitud,
                                  BindingResult errores,
                                  Model model) {

        // Validación que depende del catálogo (solo si el campo no tiene ya otro error)
        if (!errores.hasFieldErrors("material") && buscarMaterial(solicitud.getMaterial()) == null) {
            errores.rejectValue("material", "material.invalido",
                    "El material seleccionado no existe en el catálogo.");
        }

        // Validación entre dos campos (solo si ambas fechas se enlazaron bien)
        LocalDate recogida = solicitud.getFechaRecogida();
        LocalDate devolucion = solicitud.getFechaDevolucion();
        if (recogida != null && devolucion != null && devolucion.isBefore(recogida)) {
            errores.rejectValue("fechaDevolucion", "fechas.orden",
                    "La fecha de devolución no puede ser anterior a la de recogida.");
        }

        if (errores.hasErrors()) {
            // Hay que volver a poner las opciones del selector
            model.addAttribute("materiales", MATERIALES);
            return "formulario";
        }

        // Variables locales, solo existen durante esta petición
        long dias = calcularDiasPrestamo(recogida, devolucion);
        long diasUnidad = dias * solicitud.getUnidades();

        model.addAttribute("materialSolicitado", buscarMaterial(solicitud.getMaterial()));
        model.addAttribute("dias", dias);
        model.addAttribute("diasUnidad", diasUnidad);
        // solicitud ya está en el modelo gracias a @ModelAttribute
        return "resultado";
    }

    // Devuelve la fila del material o null si el código no existe
    private String[] buscarMaterial(String codigo) {
        for (String[] fila : MATERIALES) {
            if (fila[0].equals(codigo)) {
                return fila;
            }
        }
        return null;
    }

    // Cuenta recogida y devolución (ambos días incluidos)
    private long calcularDiasPrestamo(LocalDate recogida, LocalDate devolucion) {
        return ChronoUnit.DAYS.between(recogida, devolucion) + 1;
    }
}