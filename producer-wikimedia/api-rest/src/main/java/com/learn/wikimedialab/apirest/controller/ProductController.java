package com.learn.wikimedialab.apirest.controller;

import com.learn.wikimedialab.apigenerator.openapi.api.products.ProductsApi;
import com.learn.wikimedialab.apigenerator.openapi.api.products.model.GetProductsResponseDTO;
import com.learn.wikimedialab.apirest.mapper.ProductsMapper;
import com.learn.wikimedialab.domain.services.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Products Controller
 */
@RestController
@RequiredArgsConstructor
public class ProductController implements ProductsApi {

    private final ProductsService productsService;

    private final ProductsMapper productsMapper;


    @Override
    public ResponseEntity<GetProductsResponseDTO> getProducts(String productName) {
        return ResponseEntity.ok(
                new GetProductsResponseDTO(this.productsMapper.toGetProductsResponse(
                        this.productsService.getProducts(productName)
                ))
        );
    }
}
