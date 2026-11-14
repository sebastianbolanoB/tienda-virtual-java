<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, co.edu.sena.adso.tienda.modelo.*" %>
<%@ include file="fragmentos/utilidades.jspf" %>
<%
    // Datos que dejó AdminProductoServlet en la petición
    Producto f = (Producto) request.getAttribute("formulario");
    @SuppressWarnings("unchecked")
    List<String> errores = (List<String>) request.getAttribute("errores");
    @SuppressWarnings("unchecked")
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    @SuppressWarnings("unchecked")
    List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
    @SuppressWarnings("unchecked")
    List<Marca> marcas = (List<Marca>) request.getAttribute("marcas");

    // Mensaje que dejó el POST anterior en la sesión; se muestra una sola vez
    String mensaje = (String) session.getAttribute("mensaje");
    String tipoMensaje = (String) session.getAttribute("tipoMensaje");
    session.removeAttribute("mensaje");
    session.removeAttribute("tipoMensaje");

    boolean editando = f.getIdProducto() > 0;
    String ruta = request.getContextPath() + "/admin/productos";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Administrar productos | TiendaVirtual</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body>
<jsp:include page="fragmentos/encabezado.jsp">
    <jsp:param name="seccion" value="admin" />
</jsp:include>

<main class="contenido">
    <h1>Administrar productos</h1>

    <% if (mensaje != null) { %>
    <p class="mensaje <%= "error".equals(tipoMensaje) ? "mensaje-error" : "" %>"><%= e(mensaje) %></p>
    <% } %>
    <% if (!errores.isEmpty()) { %>
    <div class="mensaje mensaje-error">
        <strong>No se guardó el producto:</strong>
        <ul><% for (String error : errores) { %><li><%= e(error) %></li><% } %></ul>
    </div>
    <% } %>

    <!-- Formulario con método POST: registra o modifica un producto -->
    <form class="formulario" action="<%= ruta %>" method="post">
        <h2><%= editando ? "Editar producto " + e(f.getSku()) : "Registrar producto" %></h2>
        <input type="hidden" name="accion" value="guardar">
        <input type="hidden" name="idProducto" value="<%= editando ? f.getIdProducto() : "" %>">
        <label>SKU
            <input name="sku" value="<%= e(f.getSku()) %>" required pattern="[A-Za-z]{3}-[0-9]{3}"
                   maxlength="7" placeholder="AUD-003">
        </label>
        <label class="ancho">Nombre
            <input name="nombre" value="<%= e(f.getNombre()) %>" required minlength="3" maxlength="100">
        </label>
        <label>Categoría
            <select name="idCategoria" required>
                <option value="">Seleccione...</option>
                <% for (Categoria c : categorias) { %>
                <option value="<%= c.getIdCategoria() %>" <%= c.getIdCategoria() == f.getIdCategoria() ? "selected" : "" %>>
                    <%= e(c.getNombreCompleto()) %></option>
                <% } %>
            </select>
        </label>
        <label>Marca
            <select name="idMarca">
                <option value="">Sin marca</option>
                <% for (Marca m : marcas) { %>
                <option value="<%= m.getIdMarca() %>"
                        <%= f.getIdMarca() != null && m.getIdMarca() == f.getIdMarca() ? "selected" : "" %>>
                    <%= e(m.getNombre()) %></option>
                <% } %>
            </select>
        </label>
        <label>Precio (COP)
            <input name="precio" type="number" min="1" step="100" required
                   value="<%= f.getPrecio() == null ? "" : f.getPrecio().stripTrailingZeros().toPlainString() %>">
        </label>
        <label>Stock
            <input name="stock" type="number" min="0" required value="<%= editando || !errores.isEmpty() ? f.getStock() : 0 %>">
        </label>
        <label>Stock mínimo
            <input name="stockMinimo" type="number" min="0" required value="<%= f.getStockMinimo() %>">
        </label>
        <label class="ancho">Descripción
            <textarea name="descripcion" rows="2" maxlength="500"><%= e(f.getDescripcion()) %></textarea>
        </label>
        <div class="acciones">
            <button type="submit"><%= editando ? "Guardar cambios" : "Registrar producto" %></button>
            <% if (editando) { %><a class="enlace" href="<%= ruta %>">Cancelar</a><% } %>
        </div>
    </form>

    <h2>Productos registrados (<%= productos.size() %>)</h2>
    <div class="tabla-contenedor">
    <table class="tabla">
        <thead>
        <tr><th>ID</th><th>SKU</th><th>Nombre</th><th>Categoría</th><th>Precio</th><th>Stock</th>
            <th>Estado</th><th>Entrada / salida de stock</th><th>Acciones</th></tr>
        </thead>
        <tbody>
        <% for (Producto p : productos) { %>
        <tr class="<%= p.isActivo() ? "" : "inactivo" %>">
            <td><%= p.getIdProducto() %></td>
            <td><%= e(p.getSku()) %></td>
            <td><%= e(p.getNombre()) %></td>
            <td><%= e(p.getCategoria()) %></td>
            <td class="num"><%= pesos(p.getPrecio()) %></td>
            <td class="num <%= p.isStockBajo() ? "bajo" : "" %>"><%= p.getStock() %><%= p.isStockBajo() ? " (bajo)" : "" %></td>
            <td><%= p.isActivo() ? "Activo" : "Inactivo" %></td>
            <td>
                <form class="en-linea" action="<%= ruta %>" method="post">
                    <input type="hidden" name="accion" value="stock">
                    <input type="hidden" name="idProducto" value="<%= p.getIdProducto() %>">
                    <input name="cantidad" type="number" required placeholder="+10 / -2" aria-label="Cantidad">
                    <button type="submit">Aplicar</button>
                </form>
            </td>
            <td>
                <a class="enlace" href="<%= ruta %>?editar=<%= p.getIdProducto() %>">Editar</a>
                <form class="en-linea" action="<%= ruta %>" method="post">
                    <input type="hidden" name="accion" value="estado">
                    <input type="hidden" name="idProducto" value="<%= p.getIdProducto() %>">
                    <input type="hidden" name="activo" value="<%= p.isActivo() ? "0" : "1" %>">
                    <button type="submit" class="secundario"><%= p.isActivo() ? "Desactivar" : "Activar" %></button>
                </form>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    </div>
</main>

<jsp:include page="fragmentos/pie.jsp" />
</body>
</html>
