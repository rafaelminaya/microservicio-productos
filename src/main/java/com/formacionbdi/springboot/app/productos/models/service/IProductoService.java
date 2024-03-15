package com.formacionbdi.springboot.app.productos.models.service;

import java.util.List;

import com.formacionbdi.springboot.app.commons.models.entity.Producto;

public interface IProductoService {
	
	List<Producto> findAll();

	Producto findById(Long id);
	
	Producto save(Producto producto);		
	
	void deleteById(Long id);
}
