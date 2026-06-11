package com.example.muncel.control;

import com.example.muncel.model.CategoriaProducto;
import com.example.muncel.model.Cliente;
import com.example.muncel.model.ClienteUsuario;
import com.example.muncel.model.NotaVenta;
import com.example.muncel.model.OrdenServicio;
import com.example.muncel.repository.ProductoRepository;
import com.example.muncel.repository.ClienteRepository;
import com.example.muncel.repository.ClienteUsuarioRepository;
import com.example.muncel.repository.NotaVentaRepository;
import com.example.muncel.repository.OrdenServicioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.Optional;
import java.util.List;

@Controller
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepositorio;

    // Inyectamos los repositorios necesarios para el registro de clientes
    @Autowired
    private ClienteRepository clienteRepositorio;

    @Autowired
    private ClienteUsuarioRepository clienteUsuarioRepository;

    // Repositorios adicionales para el panel del cliente
    @Autowired
    private OrdenServicioRepository ordenServicioRepositorio; // Para traer sus reparaciones

    @Autowired
    private NotaVentaRepository notaVentaRepositorio; // Para traer su historial de compras

    @GetMapping("/login") 
    public String mostrarLogin() {
        return "login"; 
    }

    // ==========================================
    // NUEVOS MÉTODOS PARA EL REGISTRO DE CLIENTES
    // ==========================================

    // 1. Muestra la pantalla de registro
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    // 2. Procesa los datos del formulario de registro
    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam("cedulaRuc") String cedulaRuc,
            @RequestParam("nombreCompleto") String nombreCompleto,
            @RequestParam("telefono") String telefono,
            @RequestParam("correo") String correo,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model) {
        
        try {
            // Validación 1: Verificar si la cédula ya existe en la tabla 'clientes'
            // (Usando el método corregido findByCedulaRuc de tu repositorio)
            Optional<Cliente> clienteExistente = clienteRepositorio.findByCedulaRuc(cedulaRuc);
            if (clienteExistente.isPresent()) {
                model.addAttribute("error", "La cédula o RUC ya se encuentra registrada.");
                return "registro";
            }

            // Validación 2: Verificar si el nombre de usuario ya existe en 'usuario_cliente'
            // (Para evitar que dos clientes elijan el mismo username)
            if (clienteUsuarioRepository.existsByUsername(username)) {
                model.addAttribute("error", "El nombre de usuario ya está en uso.");
                return "registro";
            }

            // PASO A: Crear y guardar el nuevo Cliente
            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setCedulaRuc(cedulaRuc);
            nuevoCliente.setNombreCompleto(nombreCompleto);
            nuevoCliente.setTelefono(telefono);
            nuevoCliente.setCorreo(correo);
            
            // Guardamos en la tabla 'clientes' y JPA nos devuelve el cliente con su 'id_cliente' generado
            Cliente clienteGuardado = clienteRepositorio.save(nuevoCliente);

            // PASO B: Crear y guardar las credenciales en 'usuario_cliente'
            ClienteUsuario nuevoUsuario = new ClienteUsuario();
            nuevoUsuario.setUsername(username);
            
            // ¡IMPORTANTE! Aquí debes encriptar la contraseña antes de guardarla.
            // Si usas BCrypt de Spring Security, sería: passwordEncoder.encode(password)
            // Por ahora la guardamos directo para probar el flujo:
            nuevoUsuario.setPassword(password);
            
            // Enlazamos la relación de la clave foránea id_cliente_fk
            nuevoUsuario.setCliente(clienteGuardado);

            clienteUsuarioRepository.save(nuevoUsuario);

            // Si todo sale bien, redirige al login con un parámetro de éxito
            return "redirect:/login?registrado=true";

        } catch (Exception e) {
            model.addAttribute("error", "Error del sistema: " + e.getMessage());
            return "registro";
        }
    }

    // PROCESAR EL LOGIN DE FORMA MANUAL
    @PostMapping("/ingresar")
    public String ingresarManualmente(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model, 
            jakarta.servlet.http.HttpSession session) { // Usamos la sesión del navegador
        
        // 1. Buscamos al usuario por su username
        Optional<ClienteUsuario> usuarioOpt = clienteUsuarioRepository.findOptionalByUsername(username);

        // 2. Validamos si existe y si la contraseña coincide en texto plano
        if (usuarioOpt.isPresent() && usuarioOpt.get().getPassword().equals(password)) {
            
            // ¡ÉXITO! Guardamos al cliente en la sesión del navegador para saber quién está conectado
            Cliente cliente = usuarioOpt.get().getCliente();
            session.setAttribute("usuarioLogueado", cliente);
            
            // Lo mandamos directo a su panel
            return "redirect:/cliente/dashboard";
        } else {
            // Si falla, recargamos el login con el mensaje de error
            model.addAttribute("errorMan", "Usuario o contraseña incorrectos.");
            return "login";
        }
    }

    // ==========================================
    // 3. PANEL PRINCIPAL DEL CLIENTE (DASHBOARD)
    // ==========================================
    @GetMapping("/cliente/dashboard")
    public String mostrarDashboardCliente(jakarta.servlet.http.HttpSession session, Model model) {
        
        // A. Revisamos si hay un cliente guardado en la sesión del navegador
        Cliente cliente = (Cliente) session.getAttribute("usuarioLogueado");
        
        // B. Si nadie ha iniciado sesión, lo redirigimos al login por seguridad
        if (cliente == null) {
            return "redirect:/login";
        }
        
        // C. Al usar la sesión manual, ya tenemos directamente al objeto Cliente. 
        // Buscamos sus datos específicos usando tus repositorios
        List<OrdenServicio> misOrdenes = ordenServicioRepositorio.findByDispositivoCliente(cliente);
        List<NotaVenta> misCompras = notaVentaRepositorio.findByCliente(cliente);

        // D. Pasamos los datos recolectados a la plantilla HTML mediante el Model
        model.addAttribute("cliente", cliente);
        model.addAttribute("ordenes", misOrdenes);
        model.addAttribute("compras", misCompras);
        
        return "dashboard-cliente"; // Retorna el archivo dashboard-cliente.html
    }

    // ==========================================
    // 4. CERRAR SESIÓN DE FORMA MANUAL
    // ==========================================
    @GetMapping("/logout")
    public String cerrarSesionManual(jakarta.servlet.http.HttpSession session) {
        session.invalidate(); // Borra por completo los datos del cliente logueado en el navegador
        return "redirect:/login?logout=true"; // Redirige al login mostrando un mensaje de salida
    }

    // ==========================================
    // MÉTODOS EXISTENTES DE TU TIENDA (ADAPTADOS CON SESIÓN)
    // ==========================================

    @GetMapping("/")
    public String index(jakarta.servlet.http.HttpSession session, Model model) {
        // Pasamos los productos normales
        model.addAttribute("productos", productoRepositorio.findByVisibleEnCatalogoTrue());
        
        // Enviamos al cliente logueado (si es que existe) para no romper el diseño
        Cliente cliente = (Cliente) session.getAttribute("usuarioLogueado");
        model.addAttribute("cliente", cliente); 
        
        return "index";
    }

    @GetMapping("/productos")
    public String listaProductos(
            @RequestParam(name = "categoria", required = false) String categoriaStr, 
            jakarta.servlet.http.HttpSession session, 
            Model model) {
        
        if (categoriaStr != null && !categoriaStr.isEmpty()) {
            try {
                CategoriaProducto categoriaEnum = CategoriaProducto.valueOf(categoriaStr.toUpperCase());
                model.addAttribute("productos", productoRepositorio.findByCategoria(categoriaEnum));
            } catch (IllegalArgumentException e) {
                model.addAttribute("productos", productoRepositorio.findAll());
            }
        } else {
            model.addAttribute("productos", productoRepositorio.findByVisibleEnCatalogoTrue());
        }
        
        // Enviamos al cliente logueado a la vista del catálogo de productos
        Cliente cliente = (Cliente) session.getAttribute("usuarioLogueado");
        model.addAttribute("cliente", cliente);

        return "productos";
    }
}