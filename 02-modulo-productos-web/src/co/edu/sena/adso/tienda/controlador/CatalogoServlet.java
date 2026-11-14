package co.edu.sena.adso.tienda.controlador;

import co.edu.sena.adso.tienda.dao.CategoriaDAO;
import co.edu.sena.adso.tienda.dao.ProductoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controlador del caso de uso "Explorar catálogo" con su extensión "Filtrar y ordenar".
 * Usa el método GET: los filtros viajan en la URL, así la búsqueda se puede guardar o compartir
 * y no cambia datos en el servidor.
 */
@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final transient ProductoDAO productoDAO = new ProductoDAO();
    private final transient CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String texto = request.getParameter("q");
        Integer idCategoria = Parametros.entero(request.getParameter("categoria"));
        String orden = request.getParameter("orden");
        try {
            request.setAttribute("categorias", categoriaDAO.listarCategorias());
            request.setAttribute("productos", productoDAO.buscar(texto, idCategoria, orden, true));
        } catch (SQLException e) {
            throw new ServletException("Error al consultar el catálogo", e);
        }
        request.getRequestDispatcher("/WEB-INF/vistas/catalogo.jsp").forward(request, response);
    }
}
