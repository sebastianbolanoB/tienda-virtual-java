package co.edu.sena.adso.tienda.controlador;

import co.edu.sena.adso.tienda.dao.CategoriaDAO;
import co.edu.sena.adso.tienda.dao.ProductoDAO;
import co.edu.sena.adso.tienda.modelo.Producto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de la gestión de productos del rol Administrador.
 * GET  /admin/productos            muestra la tabla y el formulario vacío.
 * GET  /admin/productos?editar=N   carga el producto N en el formulario.
 * POST /admin/productos            accion = guardar | stock | estado (cambian datos).
 * Después de un POST correcto se redirige con GET (patrón Post/Redirect/Get) y el mensaje
 * se guarda en la sesión, para que al recargar la página no se repita el registro.
 */
@WebServlet("/admin/productos")
public class AdminProductoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String VISTA = "/WEB-INF/vistas/admin_productos.jsp";
    private static final int ERROR_DUPLICADO = 1062;

    private final transient ProductoDAO productoDAO = new ProductoDAO();
    private final transient CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer editar = Parametros.entero(request.getParameter("editar"));
        try {
            Producto formulario = editar == null ? null : productoDAO.consultar(editar);
            mostrar(request, response, formulario == null ? new Producto() : formulario, List.of());
        } catch (SQLException e) {
            throw new ServletException("Error al consultar los productos", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = Parametros.texto(request.getParameter("accion"));
        try {
            switch (accion) {
                case "guardar" -> guardar(request, response);
                case "stock" -> actualizarStock(request, response);
                case "estado" -> cambiarEstado(request, response);
                default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
        } catch (SQLException e) {
            throw new ServletException("Error al guardar en la base de datos", e);
        }
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        Producto p = new Producto();
        Integer id = Parametros.entero(request.getParameter("idProducto"));
        p.setIdProducto(id == null ? 0 : id);
        p.setSku(Parametros.texto(request.getParameter("sku")).toUpperCase());
        p.setNombre(Parametros.texto(request.getParameter("nombre")));
        p.setDescripcion(Parametros.texto(request.getParameter("descripcion")));
        p.setIdMarca(Parametros.entero(request.getParameter("idMarca")));
        Integer idCategoria = Parametros.entero(request.getParameter("idCategoria"));
        BigDecimal precio = Parametros.decimal(request.getParameter("precio"));
        Integer stock = Parametros.entero(request.getParameter("stock"));
        Integer stockMinimo = Parametros.entero(request.getParameter("stockMinimo"));

        List<String> errores = new ArrayList<>();
        if (!p.getSku().matches("[A-Z]{3}-\\d{3}")) {
            errores.add("El SKU debe tener el formato AAA-000, por ejemplo AUD-003.");
        }
        if (p.getNombre().length() < 3 || p.getNombre().length() > 100) {
            errores.add("El nombre debe tener entre 3 y 100 caracteres.");
        }
        if (idCategoria == null) {
            errores.add("Seleccione una categoría.");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            errores.add("El precio debe ser un número mayor que cero.");
        }
        if (stock == null || stock < 0 || stockMinimo == null || stockMinimo < 0) {
            errores.add("El stock y el stock mínimo deben ser enteros de 0 en adelante.");
        }
        p.setIdCategoria(idCategoria == null ? 0 : idCategoria);
        p.setPrecio(precio);
        p.setStock(stock == null ? 0 : stock);
        p.setStockMinimo(stockMinimo == null ? 0 : stockMinimo);

        if (errores.isEmpty()) {
            try {
                if (p.getIdProducto() == 0) {
                    int nuevo = productoDAO.insertar(p);
                    exito(request, response, "Producto " + p.getSku() + " registrado con el id " + nuevo + ".");
                } else {
                    productoDAO.actualizar(p);
                    exito(request, response, "Producto " + p.getSku() + " actualizado.");
                }
                return;
            } catch (SQLException e) {
                if (e.getErrorCode() != ERROR_DUPLICADO) {
                    throw e;
                }
                errores.add("Ya existe un producto con el SKU " + p.getSku() + ".");
            }
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        mostrar(request, response, p, errores);
    }

    private void actualizarStock(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {
        Integer id = Parametros.entero(request.getParameter("idProducto"));
        Integer cantidad = Parametros.entero(request.getParameter("cantidad"));
        if (id == null || cantidad == null || cantidad == 0) {
            redirigir(request, response, "error",
                    "Escriba una cantidad distinta de cero (positiva = entrada, negativa = salida).");
        } else if (productoDAO.actualizarStock(id, cantidad) == 0) {
            redirigir(request, response, "error",
                    "No se actualizó el stock: la salida es mayor que las unidades disponibles.");
        } else {
            Producto p = productoDAO.consultar(id);
            exito(request, response, "Stock de " + p.getSku() + " actualizado a " + p.getStock() + " unidades.");
        }
    }

    private void cambiarEstado(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {
        Integer id = Parametros.entero(request.getParameter("idProducto"));
        boolean activar = "1".equals(request.getParameter("activo"));
        if (id != null && productoDAO.cambiarEstado(id, activar) == 1) {
            Producto p = productoDAO.consultar(id);
            exito(request, response, "Producto " + p.getSku()
                    + (activar ? " activado." : " desactivado: ya no se muestra en el catálogo."));
        } else {
            redirigir(request, response, "error", "No se encontró el producto.");
        }
    }

    private void mostrar(HttpServletRequest request, HttpServletResponse response, Producto formulario,
            List<String> errores) throws ServletException, IOException, SQLException {
        request.setAttribute("formulario", formulario);
        request.setAttribute("errores", errores);
        request.setAttribute("productos", productoDAO.buscar(null, null, "nombre", false));
        request.setAttribute("categorias", categoriaDAO.listarCategorias());
        request.setAttribute("marcas", categoriaDAO.listarMarcas());
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    private void exito(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws IOException {
        redirigir(request, response, "ok", mensaje);
    }

    /** Guarda el mensaje en la sesión y redirige con GET (Post/Redirect/Get). */
    private void redirigir(HttpServletRequest request, HttpServletResponse response, String tipo,
            String mensaje) throws IOException {
        request.getSession().setAttribute("mensaje", mensaje);
        request.getSession().setAttribute("tipoMensaje", tipo);
        response.sendRedirect(request.getContextPath() + "/admin/productos");
    }
}
