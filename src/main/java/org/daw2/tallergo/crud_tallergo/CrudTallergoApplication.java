package org.daw2.tallergo.crud_tallergo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación TallerGO.
 * <p>
 * Punto de entrada del proyecto Spring Boot. Al ejecutarse, arranca el contenedor
 * embebido de Tomcat, inicializa el contexto de Spring y levanta todos los beans,
 * filtros de seguridad y configuraciones definidas en el proyecto.
 * </p>
 *
 * @author VidalB46 / jlmp
 * @version 0.0.1-SNAPSHOT
 */
@SpringBootApplication
public class CrudTallergoApplication {

	/**
	 * Método de arranque de la aplicación.
	 *
	 * @param args Argumentos de línea de comandos (p.ej. perfil de Spring activo).
	 */
	public static void main(String[] args) {
		SpringApplication.run(CrudTallergoApplication.class, args);
	}

}
