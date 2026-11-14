<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Se agrega con la acción jsp:include; recibe el parámetro "seccion" con jsp:param --%>
<% String seccion = request.getParameter("seccion"); %>
<header class="barra">
    <a class="marca" href="<%= request.getContextPath() %>/catalogo">TiendaVirtual</a>
    <nav>
        <a href="<%= request.getContextPath() %>/catalogo"
           class="<%= "catalogo".equals(seccion) ? "activo" : "" %>">Catálogo</a>
        <a href="<%= request.getContextPath() %>/admin/productos"
           class="<%= "admin".equals(seccion) ? "activo" : "" %>">Administrar productos</a>
    </nav>
</header>
