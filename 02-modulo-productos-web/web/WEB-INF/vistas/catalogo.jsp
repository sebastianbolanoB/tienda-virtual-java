<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, co.edu.sena.adso.tienda.modelo.Producto, co.edu.sena.adso.tienda.modelo.Categoria" %>
<%@ include file="fragmentos/utilidades.jspf" %>
<%
    // Datos que dejó CatalogoServlet en la petición
    @SuppressWarnings("unchecked")
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    @SuppressWarnings("unchecked")
    List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
    String q = request.getParameter("q") == null ? "" : request.getParameter("q");
    String categoria = request.getParameter("categoria") == null ? "" : request.getParameter("categoria");
    String orden = request.getParameter("orden") == null ? "nombre" : request.getParameter("orden");
    String[][] ordenes = {{"nombre", "Nombre (A-Z)"}, {"precio_asc", "Precio: menor a mayor"},
                          {"precio_desc", "Precio: mayor a menor"}};
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Catálogo | TiendaVirtual</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body>
<jsp:include page="fragmentos/encabezado.jsp">
    <jsp:param name="seccion" value="catalogo" />
</jsp:include>

<main class="contenido">
    <h1>Catálogo de productos</h1>

    <!-- Formulario con método GET: los filtros quedan en la URL -->
    <form class="filtros" action="<%= request.getContextPath() %>/catalogo" method="get">
        <label>Buscar
            <input type="search" name="q" value="<%= e(q) %>" placeholder="Nombre o SKU" maxlength="60">
        </label>
        <label>Categoría
            <select name="categoria">
                <option value="">Todas</option>
                <% for (Categoria c : categorias) { %>
                <option value="<%= c.getIdCategoria() %>"
                        <%= String.valueOf(c.getIdCategoria()).equals(categoria) ? "selected" : "" %>>
                    <%= e(c.getNombreCompleto()) %>
                </option>
                <% } %>
            </select>
        </label>
        <label>Ordenar por
            <select name="orden">
                <% for (String[] o : ordenes) { %>
                <option value="<%= o[0] %>" <%= o[0].equals(orden) ? "selected" : "" %>><%= o[1] %></option>
                <% } %>
            </select>
        </label>
        <button type="submit">Buscar</button>
        <a class="enlace" href="<%= request.getContextPath() %>/catalogo">Limpiar</a>
    </form>

    <p class="resumen"><%= productos.size() %> producto(s) encontrado(s)</p>

    <% if (productos.isEmpty()) { %>
    <p class="vacio">No hay productos que coincidan con la búsqueda.</p>
    <% } else { %>
    <section class="tarjetas">
        <% for (Producto p : productos) { %>
        <article class="tarjeta">
            <span class="categoria"><%= e(p.getCategoria()) %></span>
            <h2><%= e(p.getNombre()) %></h2>
            <p class="marca-producto"><%= p.getMarca() == null ? "Sin marca" : e(p.getMarca()) %> · <%= e(p.getSku()) %></p>
            <p class="precio"><%= pesos(p.getPrecio()) %></p>
            <% if (p.getStock() == 0) { %>
            <p class="estado agotado">Agotado</p>
            <% } else if (p.isStockBajo()) { %>
            <p class="estado">Últimas <%= p.getStock() %> unidades</p>
            <% } else { %>
            <p class="estado">Disponible</p>
            <% } %>
            <a class="boton" href="<%= request.getContextPath() %>/producto?id=<%= p.getIdProducto() %>">Ver detalle</a>
        </article>
        <% } %>
    </section>
    <% } %>
</main>

<jsp:include page="fragmentos/pie.jsp" />
</body>
</html>
