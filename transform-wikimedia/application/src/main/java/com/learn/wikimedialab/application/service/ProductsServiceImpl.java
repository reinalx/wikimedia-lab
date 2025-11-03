package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.entities.products.Product;
import com.learn.wikimedialab.domain.exceptions.ProductNotFoundException;
import com.learn.wikimedialab.domain.services.ProductsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {

    @Override
    public List<Product> getProducts(String name) {
        log.info("Getting products with criteria: {}", name);
        if (name == null ||  !name.isEmpty()) {
            log.info("Returning products matching name: {}", name);
            throw new ProductNotFoundException();
        }
        return List.of(
                Product.builder().id(1L).name("Sample Product").description("This is a sample product").price(9.99).build(),
                Product.builder().id(2L).name("Another Product").description("This is another sample product").price(19.99).build(),
                Product.builder().id(3L).name("Third Product").description("This is the third sample product").price(29.99).build()
        );
    }
}
