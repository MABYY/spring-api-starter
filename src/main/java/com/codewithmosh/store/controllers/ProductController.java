package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDTO;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
@Tag(name="Product")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;


    @GetMapping
    public List<ProductDTO> getAllProducts (
            @RequestParam(name = "categoryId", required = false) Byte categoryId
    ){
        List<Product> products;
        if (categoryId !=null) {
            products = productRepository.findAllByCategoryId(categoryId);
        } else {
            products = productRepository.findAllWithCategory();
        }
       return products.stream()
                .map(productMapper::productToDTO)
                .toList();
    };

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct (
            @RequestBody ProductDTO productDTO,
            UriComponentsBuilder uriComponentsBuilder
    ){
       var category = categoryRepository.findById(productDTO.getCategoryId()).orElse(null);
       if( category == null) {
           return ResponseEntity.badRequest().build();
       }
       var product = productMapper.toEntity(productDTO);
       product.setCategory(category);
       productRepository.save(product);
       productDTO.setId(product.getId());
       return ResponseEntity.ok(productDTO);
    };

    @PutMapping("{id}")
    public ResponseEntity<ProductDTO> updateProduct (
            @PathVariable Long id, @RequestBody ProductDTO productDTO
    ) {
        var category = categoryRepository.findById(productDTO.getCategoryId()).orElse(null);
        if( category == null) {
            return ResponseEntity.badRequest().build();
        }

        var product = productRepository.findById(id).orElse(null);
        if (product == null){
            return ResponseEntity.notFound().build();
        }

        productMapper.update(productDTO,product); // the product dto does not have the id
        product.setCategory(category);
        productRepository.save(product);
        productDTO.setId(product.getId());

        return ResponseEntity.ok(productDTO);
    };


    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct (  @PathVariable Long id ){

        var product = productRepository.findById(id).orElse(null);
        if (product == null){
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    };

}
