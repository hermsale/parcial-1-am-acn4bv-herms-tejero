package com.example.lamontana.data;

import java.util.Arrays;
import java.util.List;

public class Servicio {

    public String nombre;
    public String descripcion;
    public boolean disponible;
    public double precio;

    public Servicio(String nombre, String descripcion, boolean disponible, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.disponible = disponible;
        this.precio = precio;
    }

    List<Servicio> servicios = Arrays.asList(
            new Servicio("fotocopiado_bn",
                    "Fotocopiado en blanco y negro por carilla, simple faz.",
                    true, 40),

            new Servicio("fotocopiado_color",
                    "Fotocopiado/color por carilla, simple faz.",
                    true, 120),

            new Servicio("doble_faz",
                    "Recargo o ajuste por impresión doble faz.",
                    true, 0.8),

            new Servicio("anillado",
                    "Anillado plástico o metálico estándar.",
                    true, 900),

            new Servicio("encuadernado",
                    "Encuadernado simple (tapa blanda).",
                    true, 1500)
    );
}
