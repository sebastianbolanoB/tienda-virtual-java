<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ include file="fragmentos/utilidades.jspf" %>
<%
    // Con isErrorPage="true" la página recibe el objeto implícito exception
    Integer codigo = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
    String detalle = (String) request.getAttribute("jakarta.servlet.error.message");
    if (exception != null) {
        Throwable causa = exception.getCause() != null ? exception.getCause() : exception;
        detalle = exception.getMessage() + " (" + causa.getClass().getSimpleName() + ": " + causa.getMessage() + ")";
        application.log("Error en TiendaVirtual", exception);
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Error <%= codigo %> | TiendaVirtual</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body>
<jsp:include page="fragmentos/encabezado.jsp" />
<main class="contenido">
    <h1><%= codigo != null && codigo == 404 ? "Página o producto no encontrado" : "Ocurrió un error" %></h1>
    <p class="mensaje mensaje-error">Código <%= codigo %>: <%= e(detalle) %></p>
    <p><a class="enlace" href="<%= request.getContextPath() %>/catalogo">Volver al catálogo</a></p>
</main>
</body>
</html>
