package com.formacionbdi.springboot.app.productos.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.productos.models.entity.Producto;
import com.formacionbdi.springboot.app.productos.models.service.IProductoService;

@RestController
public class ProductoController {
	// Atributo para obtener el puerto configurado en el archivo ".properties"
	// @Autowired
	// private Environment env;

	// Inyeccion el valor del key "server.port" configurado en el archivo
	// ".properties"
	@Value("${server.port}")
	private Integer port;

	// Inyección de esta clase que permite obtener el puerto de la instancia actual
	@Autowired
	private ServletWebServerApplicationContext webServerAppCtxt;

	@Autowired
	private IProductoService productoService;

	// Establecemos los endpoints para los otros servicios que se comuniquen con
	// estos.
	@GetMapping("/listar")
	public List<Producto> listar() {
		return this.productoService.findAll().stream().map(producto -> {
			// 1° Opcion usando la interfaz "Environment"
			// producto.setPort(Integer.parseInt(env.getProperty("local.server.port")));

			// 2° Opcion usando la anotación "@Value"
			// producto.setPort(this.port);

			// 3° Opcion usando la clase "ServletWebServerApplicationContext"
			producto.setPort(this.webServerAppCtxt.getWebServer().getPort());

			return producto;
		}).collect(Collectors.toList());

	}

	@GetMapping("/ver/{id}")
	public Producto detalle(@PathVariable Long id) {

		Producto producto = this.productoService.findById(id);
		// 1° Opcion usando la interfaz "Environment"
		/*
		 * Setearemos el atributo "port" con la variable "server.port" del archivo
		 * "application.properties" Integer.parseInt() : Necesario para convertir a
		 * "Integer" ya que el valor obtenido es "String" "local." : Palabra necesaria
		 * para acceder a variables dentro de un archivo ".properties" "server.port" :
		 * Es la variable a utilizar
		 */
		// producto.setPort(Integer.parseInt(env.getProperty("local.server.port")));

		// 2° Opcion usando la anotación "@Value"
		// producto.setPort(this.port);

		// 3° Opcion usando la clase "ServletWebServerApplicationContext"
		producto.setPort(this.webServerAppCtxt.getWebServer().getPort());

		/*
		 * 1° Simulación:
		 * Forzamos un error en este microservicio, para probar el "circuit breker" de "Hystrix"
		 * Por lo que abriremos un "circuito alternativo" en el controlador del proyecto "servicio-items" 
		 */

		/*
		boolean ok = false;
		if (!ok) {
			throw new RuntimeException("No se pudo cargar el producto!");
		}
		*/
		
		/*
		 * 2° Simulación: Forzamos  un timeout de 2 segundos.
		 * El timeout por defecto en Ribbon y Hystrix es de 1 segundo
		 * Por lo que se lanzaría un error luego del 1 segundo a menos que sea configure el camino alternativo.
		 */		
		
		/*
		try {
			Thread.sleep(3000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

		return producto;
	}

}
