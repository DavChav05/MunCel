package com.example.muncel.control;

import com.example.muncel.model.CategoriaProducto;
import com.example.muncel.model.Cliente;
import com.example.muncel.model.ClienteUsuario;
import com.example.muncel.model.Empleado;
import com.example.muncel.model.NotaVenta;
import com.example.muncel.model.OrdenServicio;
import com.example.muncel.model.SubcategoriaProducto;
import com.example.muncel.repository.ProductoRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.example.muncel.repository.ClienteRepository;
import com.example.muncel.repository.ClienteUsuarioRepository;
import com.example.muncel.repository.EmpleadoRepository;
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

    @Autowired
    private EmpleadoRepository empleadoRepository; // Asegúrate de tener esto arriba

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
            if (clienteUsuarioRepository.existsByUsername(username)) {
                model.addAttribute("error", "El nombre de usuario ya está en uso.");
                return "registro";
            }

            // BUSCAR CLIENTE O CREAR UNO NUEVO
            Cliente cliente = clienteRepositorio.findByCedulaRuc(cedulaRuc)
                    .orElse(new Cliente());

            // Actualizamos los datos personales siempre
            cliente.setCedulaRuc(cedulaRuc);
            cliente.setNombreCompleto(nombreCompleto);
            cliente.setTelefono(telefono);
            Cliente clienteGuardado = clienteRepositorio.save(cliente);

            // Verificar si este cliente físico ya tiene un usuario web
            // (Opcional: podrías agregar un método existsByCliente en tu repositorio)

            // CREAR CREDENCIALES DE ACCESO
            ClienteUsuario nuevoUsuario = new ClienteUsuario();
            nuevoUsuario.setUsername(username);
            nuevoUsuario.setCorreo(correo); // Correo va en el usuario
            nuevoUsuario.setPassword(password); // ¡Se encripta automáticamente por tu setter!
            nuevoUsuario.setCliente(clienteGuardado);

            clienteUsuarioRepository.save(nuevoUsuario);

            return "redirect:/login?registrado=true";

        } catch (Exception e) {
            model.addAttribute("error", "Error del sistema al registrar. Verifique que el correo no esté en uso.");
            return "registro";
        }
    }

    @PostMapping("/ingresar")
    public String ingresarManualmente(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            HttpSession session, // Mantén esta
            HttpServletRequest request) {

        Optional<Empleado> empleadoOpt = empleadoRepository.findByUsername(username);

        if (empleadoOpt.isPresent()) {
            Empleado empleado = empleadoOpt.get();
            // Nota: Asegúrate de que este .equals coincida con cómo guardas la clave
            if (empleado.getPassword().equals(password)) {
                if (empleado.isEstadoActivo()) {
                    // Creamos una sesión nueva limpia
                    session.invalidate(); // Matas la anterior
                    HttpSession nuevaSesion = request.getSession(true); // Creas una nueva
                    nuevaSesion.setAttribute("empleadoLogueado", empleado); // Guardas en la nueva

                    return "redirect:/empleado/ventas";
                } else {
                    model.addAttribute("errorMan", "Cuenta de empleado desactivada.");
                    return "login";
                }
            }
        }

        // 1. EMPLEADOS
        System.out.println("DEBUG: Buscando usuario: " + username);
        Optional<ClienteUsuario> usuarioOpt = clienteUsuarioRepository.findOptionalByUsername(username);

        if (usuarioOpt.isPresent()) {
            System.out.println("DEBUG: Usuario encontrado. Verificando password...");
            boolean esValida = usuarioOpt.get().verificarPasswordIngresada(password);
            System.out.println("DEBUG: ¿Password válida? " + esValida);

            if (esValida) {
                Cliente cliente = usuarioOpt.get().getCliente();
                session.setAttribute("usuarioLogueado", cliente);
                return "redirect:/cliente/dashboard";
            }
        } else {
            System.out.println("DEBUG: Usuario NO encontrado en la base de datos.");
        }

        model.addAttribute("errorMan", "Usuario o contraseña incorrectos.");
        return "login";
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
            // Cambiamos el nombre del parámetro a 'subcategoria' para que coincida con tu
            // HTML
            @RequestParam(name = "subcategoria", required = false) String subcategoriaStr,
            jakarta.servlet.http.HttpSession session,
            Model model) {

        // 1. Recuperar cliente para el navbar (lo que ya tenías funcionando)
        model.addAttribute("cliente", session.getAttribute("usuarioLogueado"));

        // 2. Lógica de filtro por SUBCATEGORÍA
        if (subcategoriaStr != null && !subcategoriaStr.isEmpty()) {
            try {
                // Asegúrate de usar el Enum correcto (SubcategoriaProducto)
                SubcategoriaProducto subEnum = SubcategoriaProducto.valueOf(subcategoriaStr.toUpperCase());
                // AQUÍ LLAMAS AL MÉTODO DE TU REPOSITORIO
                model.addAttribute("productos", productoRepositorio.findBySubcategoria(subEnum));
            } catch (IllegalArgumentException e) {
                // Si el nombre no coincide, mostramos todos
                model.addAttribute("productos", productoRepositorio.findByVisibleEnCatalogoTrue());
            }
        } else {
            // Filtro por defecto
            model.addAttribute("productos", productoRepositorio.findByVisibleEnCatalogoTrue());
        }

        return "productos";
    }
}