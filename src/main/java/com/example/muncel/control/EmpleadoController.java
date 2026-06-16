package com.example.muncel.control;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.transaction.annotation.Transactional;

import com.example.muncel.model.DetalleVenta;
import com.example.muncel.model.Empleado;
import com.example.muncel.model.NotaVenta;
import com.example.muncel.model.Producto;
import com.example.muncel.repository.ClienteRepository;
import com.example.muncel.repository.DetalleVentaRepository;
import com.example.muncel.repository.NotaVentaRepository;
import com.example.muncel.repository.ProductoRepository;
import com.example.muncel.repository.EmpleadoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class EmpleadoController {

    @Autowired
    private ProductoRepository productoRepositorio;

    @Autowired
    private NotaVentaRepository notaVentaRepositorio;

    @Autowired
    private DetalleVentaRepository detalleVentaRepositorio;

    @Autowired
    private ClienteRepository clienteRepositorio;

    @Autowired
    private EmpleadoRepository empleadoRepositorio;

    private boolean esEmpleado(HttpSession session) {
        return session.getAttribute("empleadoLogueado") != null;
    }

    // --- VISTAS (GET) ---
    @GetMapping("/empleado/ventas")
    public String ventas(@RequestParam(required = false) String buscarCodigo, HttpSession session, Model model) {
        if (!esEmpleado(session))
            return "redirect:/login";

        NotaVenta ultimaNota = notaVentaRepositorio.findTopByOrderByNumeroFacturaDesc();
        int proximoNumero = (ultimaNota != null) ? ultimaNota.getNumeroFactura() + 1 : 4001;
        model.addAttribute("proximoNumero", proximoNumero);

        // 1. Cargamos el carrito actual
        List<DetalleVenta> lista = detalleVentaRepositorio.findByNotaVentaIsNull();
        if (lista == null) {
            lista = new ArrayList<>();
        }
        model.addAttribute("detalles", lista);

        // 2. NUEVA LÓGICA: Si el usuario presionó "Verificar"
        if (buscarCodigo != null && !buscarCodigo.trim().isEmpty()) {
            Producto prod = productoRepositorio.findByCodigoProducto(buscarCodigo).orElse(null);
            if (prod != null) {
                model.addAttribute("productoVerificado", prod);
            } else {
                model.addAttribute("errorBusqueda", "El código '" + buscarCodigo + "' no pertenece a ningún producto.");
            }
        }

        return "empleado/ventas";
    }

    @GetMapping("/empleado/registro-orden")
    public String registroOrden(HttpSession session) {
        if (!esEmpleado(session))
            return "redirect:/login";
        return "empleado/registro-orden";
    }

    // --- ACCIÓN: VACIAR / LIMPIAR CARRITO ---
    @PostMapping("/empleado/ventas/limpiar")
    public String limpiarCarrito(HttpSession session) {
        if (!esEmpleado(session))
            return "redirect:/login";

        List<DetalleVenta> detallesSueltos = detalleVentaRepositorio.findByNotaVentaIsNull();
        detalleVentaRepositorio.deleteAll(detallesSueltos);

        return "redirect:/empleado/ventas?limpiado=true";
    }

    // --- ACCIÓN: AGREGAR AL CARRITO ---
    @PostMapping("/empleado/ventas/agregar")
    public String agregarAlCarrito(@RequestParam String codigoProducto,
            @RequestParam Integer cantidad) {

        // LÍNEA DE DEPURACIÓN PARA VER SI HAY ESPACIOS EN BLANCO
        System.out.println("DEBUG - Código recibido desde el HTML: [" + codigoProducto + "]");

        // 1. Buscar producto
        Producto producto = productoRepositorio.findByCodigoProducto(codigoProducto).orElse(null);

        if (producto == null) {
            System.out.println("DEBUG - El producto resultó NULL. No se encontró en la BDD.");
            return "redirect:/empleado/ventas?error=NoEncontrado";
        }

        System.out.println("DEBUG - ¡Producto encontrado! Nombre: " + producto.getNombreProducto());

        // 2. Crear el detalle directamente
        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);

        // Calculamos valores históricos
        BigDecimal precio = producto.getPrecioVenta();
        detalle.setPrecioUnitarioHistorico(precio);
        detalle.setSubtotalLinea(precio.multiply(new BigDecimal(cantidad)));

        // 3. Guardar el detalle "suelto" en la base de datos
        detalleVentaRepositorio.save(detalle);

        return "redirect:/empleado/ventas";
    }

    // --- ACCIÓN: API PARA VERIFICAR CLIENTE POR CÉDULA ---
    @GetMapping("/api/clientes/buscar")
    @ResponseBody
    public ResponseEntity<?> buscarClientePorCedula(@RequestParam String cedula) {
        Optional<com.example.muncel.model.Cliente> cliente = clienteRepositorio.findByCedulaRuc(cedula);

        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- ACCIÓN: EMITIR NOTA, REGISTRAR CLIENTE Y RESTAR STOCK ---
    @Transactional // ¡No olvides esto para lo del stock!
    @PostMapping("/empleado/ventas/emitir")
    public String emitirNota(@RequestParam String cedula,
            @RequestParam String nombreCliente,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String direccion,
            HttpSession session) {

        // Verificamos si hay un empleado en la sesión
        Empleado empleadoLogueado = (Empleado) session.getAttribute("empleadoLogueado");
        if (empleadoLogueado == null) {
            return "redirect:/login"; // Si no hay, lo mandamos al login
        }

        // 1. Validar que el carrito no esté vacío
        List<DetalleVenta> detallesSueltos = detalleVentaRepositorio.findByNotaVentaIsNull();
        if (detallesSueltos.isEmpty()) {
            return "redirect:/empleado/ventas?error=CarritoVacio";
        }

        // 2. Buscar Cliente o crearlo
        com.example.muncel.model.Cliente cliente = clienteRepositorio.findByCedulaRuc(cedula)
                .orElseGet(() -> {
                    com.example.muncel.model.Cliente nuevoCliente = new com.example.muncel.model.Cliente();
                    nuevoCliente.setCedulaRuc(cedula);
                    nuevoCliente.setNombreCompleto(nombreCliente);
                    nuevoCliente.setTelefono(telefono != null ? telefono : "");
                    nuevoCliente.setDireccion(direccion != null ? direccion : "");
                    return clienteRepositorio.save(nuevoCliente);
                });

        // 3. Crear y configurar la Nota de Venta
        NotaVenta nota = new NotaVenta();
        nota.setCliente(cliente);

        // ¡LA LÍNEA QUE FALTABA! Le asignamos el empleado que está haciendo la venta
        nota.setEmpleado(empleadoLogueado);

        nota.setFechaEmision(LocalDateTime.now());

        NotaVenta ultimaNota = notaVentaRepositorio.findTopByOrderByNumeroFacturaDesc();
        int siguienteNumero = (ultimaNota != null && ultimaNota.getNumeroFactura() != null)
                ? ultimaNota.getNumeroFactura() + 1
                : 4001;
        nota.setNumeroFactura(siguienteNumero);

        BigDecimal totalVenta = detallesSueltos.stream()
                .map(DetalleVenta::getSubtotalLinea)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        nota.setTotalPagar(totalVenta);

        // Ahora sí, se guarda sin problema
        nota = notaVentaRepositorio.save(nota);

        // 4. Vincular detalles y descontar Stock
        for (DetalleVenta d : detallesSueltos) {
            d.setNotaVenta(nota);

            Producto prod = d.getProducto();
            if (prod != null) {
                int stockActual = prod.getStock();
                int nuevoStock = stockActual - d.getCantidad();
                prod.setStock(Math.max(nuevoStock, 0));
                productoRepositorio.save(prod);
            }

            detalleVentaRepositorio.save(d);
        }

        return "redirect:/empleado/ventas?exito=true&idVenta=" + nota.getIdNotaVenta();
    }
}