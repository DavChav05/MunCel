package com.example.muncel.control;

import com.example.muncel.model.OrdenServicio;
import com.example.muncel.repository.OrdenServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@Controller
@RequestMapping("/servicio-tecnico")
public class OrdenServicioController {

    @Autowired
    private OrdenServicioRepository ordenServicioRepository;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // Carga la página pública por primera vez (buscador vacío)
    @GetMapping
    public String mostrarPaginaConsulta() {
        return "servicio-tecnico";
    }

    // Procesa la búsqueda pública por el número de orden de 4 dígitos
    @GetMapping("/buscar")
    public String buscarOrden(@RequestParam("numeroOrden") String numeroOrdenStr, Model model) {

        model.addAttribute("numeroOrden", numeroOrdenStr);

        if (numeroOrdenStr == null || numeroOrdenStr.trim().isEmpty()) {
            model.addAttribute("error", "Por favor, ingrese un número de orden.");
            return "servicio-tecnico";
        }

        String numeroLimpio = numeroOrdenStr.trim();

        try {
            // 1. Aquí transformamos el texto a número entero
            Integer numeroEntero = Integer.parseInt(numeroLimpio);

            if (numeroLimpio.length() != 4) {
                model.addAttribute("error", "El número de orden debe tener exactamente 4 dígitos (Ej: 1000).");
                return "servicio-tecnico";
            }

            // 2. CORREGIDO: Pasamos 'numeroEntero' (el Integer) en lugar de 'numeroLimpio'
            // (el String)
            Optional<OrdenServicio> ordenOpt = ordenServicioRepository.findByNumeroOrden(numeroEntero);

            if (ordenOpt.isPresent()) {
                OrdenServicio orden = ordenOpt.get();
                model.addAttribute("servicio", orden);

                if (orden.getDispositivo() != null) {
                    model.addAttribute("dispositivoNombre",
                            orden.getDispositivo().getMarca() + " " + orden.getDispositivo().getModelo());
                } else {
                    model.addAttribute("dispositivoNombre", "Dispositivo en Taller");
                }

            } else {
                model.addAttribute("error", "No se encontró ninguna orden de servicio con el número: " + numeroLimpio);
            }

        } catch (NumberFormatException e) {
            model.addAttribute("error", "El formato es incorrecto. Ingrese solo números.");
        }

        return "servicio-tecnico";
    }
}