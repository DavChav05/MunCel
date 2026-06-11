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

    @GetMapping("/login") 
    public String mostrarLogin() {
        return "login"; 
    }

    @Autowired
    private OrdenServicioRepository ordenServicioRepository;

    // 1. Carga la página por primera vez (Buscador vacío)
    @GetMapping
    public String mostrarPaginaConsulta() {
        return "servicio-tecnico"; // Renderiza tu plantilla servicio-tecnico.html
    }

    // 2. Procesa la búsqueda cuando el cliente o técnico digita el ID de la orden
    @GetMapping("/buscar")
    public String buscarOrden(@RequestParam("numeroOrden") String numeroOrdenStr, Model model) {
        
        // Guardamos el texto enviado para que no se borre de la barra de búsqueda en la interfaz
        model.addAttribute("numeroOrden", numeroOrdenStr);
        
        // Validación de seguridad: Evita que el sistema falle si mandan el buscador vacío o con letras
        if (numeroOrdenStr == null || numeroOrdenStr.trim().isEmpty()) {
            model.addAttribute("error", "Por favor, ingrese un número de orden válido.");
            return "servicio-tecnico";
        }

        try {
            // Convertimos el String de la pantalla a Integer porque 'idOrden' en tu BDD es un INT
            Integer idOrden = Integer.parseInt(numeroOrdenStr.trim());
            
            // Buscamos en la base de datos usando el repositorio corregido (devuelve un Optional)
            Optional<OrdenServicio> ordenOpt = ordenServicioRepository.findById(idOrden);
            
            if (ordenOpt.isPresent()) {
                // Si la orden existe, la mandamos completa a la vista bajo la variable 'servicio'
                model.addAttribute("servicio", ordenOpt.get());
            } else {
                // Si el número no existe en los registros
                model.addAttribute("error", "No se encontró ninguna orden de servicio con el número: " + idOrden);
            }
            
        } catch (NumberFormatException e) {
            // Este bloque atrapa el error si el usuario escribe letras en lugar de números (Ej: "abc")
            model.addAttribute("error", "El formato del número de orden es incorrecto. Ingrese solo números.");
        }
        
        return "servicio-tecnico";
    }
}