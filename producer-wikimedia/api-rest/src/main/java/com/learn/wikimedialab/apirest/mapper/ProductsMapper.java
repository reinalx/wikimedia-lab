package com.learn.wikimedialab.apirest.mapper;

import com.learn.wikimedialab.apigenerator.openapi.api.products.model.GetProductsResponseDTO;
import com.learn.wikimedialab.apigenerator.openapi.api.products.model.ProductDTO;
import com.learn.wikimedialab.domain.entities.products.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductsMapper {

    List<ProductDTO> toGetProductsResponse(List<Product> products);
}
