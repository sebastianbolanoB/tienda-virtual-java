package co.edu.sena.adso.tienda.controlador;

import co.edu.sena.adso.tienda.dao.ProductoDAO;
import co.edu.sena.adso.tienda.modelo.Producto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/** Controlador del caso de uso "Ver detalle de producto" (GET /producto?id=N). */
@WebServlet("/producto")
public class DetalleProductoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final transient ProductoDAO productoDAO = new ProductoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = Parametros.entero(request.getParameter("id"));
        Producto producto;
        try {
            producto = id == null ? null : productoDAO.consultar(id);
        } catch (SQLException e) {
            throw new ServletException("Error al consultar el producto", e);
        }
        if (producto == null || !producto.isActivo()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "El producto no existe o no está disponible");
            return;
        }
        request.setAttribute("producto", producto);
        request.getRequestDispatcher("/WEB-INF/vistas/detalle.jsp").forward(request, response);
    }
}
