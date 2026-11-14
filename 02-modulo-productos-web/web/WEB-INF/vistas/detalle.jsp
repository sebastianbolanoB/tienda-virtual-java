<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragmentos/utilidades.jspf" %>
<%-- Acción jsp:useBean: toma el JavaBean "producto" que DetalleProductoServlet dejó en la petición --%>
<jsp:useBean id="producto" class="co.edu.sena.adso.tienda.modelo.Producto" scope="request" />
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= e(producto.getNombre()) %> | TiendaVirtual</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body>
<jsp:include page="fragmentos/encabezado.jsp">
    <jsp:param name="seccion" value="catalogo" />
</jsp:include>

<main class="contenido">
    <p><a class="enlace" href="javascript:history.back()">&larr; Volver al catálogo</a></p>
    <article class="detalle">
        <span class="categoria"><%= e(producto.getCategoria()) %></span>
        <h1><%= e(producto.getNombre()) %></h1>
        <p class="precio"><%= pesos(producto.getPrecio()) %></p>
        <p><%= producto.getDescripcion() == null || producto.getDescripcion().isBlank()
                ? "Sin descripción." : e(producto.getDescripcion()) %></p>
        <table class="ficha">
            <tr><th>SKU</th><td><jsp:getProperty name="producto" property="sku" /></td></tr>
            <tr><th>Marca</th><td><%= producto.getMarca() == null ? "Sin marca" : e(producto.getMarca()) %></td></tr>
            <tr><th>Unidades disponibles</th><td><jsp:getProperty name="producto" property="stock" /></td></tr>
        </table>
        <p class="estado <%= producto.getStock() == 0 ? "agotado" : "" %>">
            <%= producto.getStock() == 0 ? "Agotado" : producto.isStockBajo() ? "Últimas unidades" : "Disponible" %>
        </p>
    </article>
</main>

<jsp:include page="fragmentos/pie.jsp" />
</body>
</html>
