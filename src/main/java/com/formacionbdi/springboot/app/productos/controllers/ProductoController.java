package com.formacionbdi.springboot.app.productos.controllers;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.productos.models.entity.Producto;
import com.formacionbdi.springboot.app.productos.models.service.IProductoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;


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
	public Producto detalle(@PathVariable Long id) throws InterruptedException {
		
		/*
		 * 3° Simulación: Lanzamiento de excepción
		 * Lanzaremos una excepción para cuando el ID sea igual a 10
		 */	
		
		if(id.equals(10L)) {
			throw new IllegalStateException("Producto no encontrado.");
		}
		
		/*
		 * 4° Simulación: Forzamos un timeout de 5 segundos cuando el id sea 7
		 * Lanzaremos una excepción para cuando el ID sea igual a 10
		 */	
		
		if(id.equals(7L)) {
			// Equivalente a : Thread.sleep(5000L);
			TimeUnit.SECONDS.sleep(5L);
		}
		

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
		 * 1° Simulación: Lanzamiento de excepción
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
	
	@PostMapping("/crear")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Producto crear(@RequestBody Producto producto) {
		
		return this.productoService.save(producto);
	}
	
	@PutMapping("/editar/{id}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Producto editar(@RequestBody Producto producto, @PathVariable(name = "id") Long productoId ) {
		
		Producto productoFromDB = this.productoService.findById(productoId);
		productoFromDB.setNombre(producto.getNombre());
		productoFromDB.setPrecio(producto.getPrecio());
		
		return this.productoService.save(productoFromDB);
	}
	
	@DeleteMapping("/eliminar/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable(name = "id") Long productoId) {
		
		this.productoService.deleteById(productoId);
	}

}
