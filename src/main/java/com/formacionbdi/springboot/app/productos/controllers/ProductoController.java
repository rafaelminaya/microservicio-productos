package com.formacionbdi.springboot.app.productos.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.productos.models.entity.Producto;
import com.formacionbdi.springboot.app.productos.models.service.IProductoService;

@RestController
public class ProductoController {
	// Atributo para obtener el puerto configurado en el archivo ".properties"
	@Autowired
	private Environment env;

	// Inyectamos el valor del key "server.port" configurado en el archivo
	// ".properties"
	@Value("${server.port}")
	private Integer port;

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
			producto.setPort(this.port);
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
		producto.setPort(this.port);

		return producto;
	}

}
