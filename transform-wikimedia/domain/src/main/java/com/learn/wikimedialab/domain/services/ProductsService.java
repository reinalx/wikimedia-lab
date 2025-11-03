package com.learn.wikimedialab.domain.services;

import com.learn.wikimedialab.domain.entities.products.Product;

import java.util.List;

public interface ProductsService {

    List<Product> getProducts(String name);
}
