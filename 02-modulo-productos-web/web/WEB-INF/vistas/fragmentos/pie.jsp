<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
         import="jakarta.servlet.RequestDispatcher, java.time.LocalDateTime, java.time.format.DateTimeFormatter" %>
<%-- Muestra la petición HTTP que generó la página: método (GET o POST) y URL original --%>
<%
    String uri = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
    String consulta = (String) request.getAttribute(RequestDispatcher.FORWARD_QUERY_STRING);
    if (uri == null) {
        uri = request.getRequestURI();
        consulta = request.getQueryString();
    }
    String url = consulta == null ? uri : uri + "?" + consulta;
    url = url.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
%>
<footer class="pie">
    <span>Petición: <strong><%= request.getMethod() %></strong> <code><%= url %></code></span>
    <span>Generado por JSP en el servidor el
        <%= LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) %></span>
</footer>
