package co.edu.sena.adso.tienda.controlador;

import java.math.BigDecimal;

/** Convierte los parámetros de los formularios (siempre llegan como texto). */
final class Parametros {

    private Parametros() {
    }

    static Integer entero(String valor) {
        try {
            return valor == null || valor.isBlank() ? null : Integer.valueOf(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static BigDecimal decimal(String valor) {
        try {
            return valor == null || valor.isBlank() ? null : new BigDecimal(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static String texto(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
