package com.github.Requu1.CampaignManager.controller;

import com.github.Requu1.CampaignManager.dto.ProductCreateDto;
import com.github.Requu1.CampaignManager.dto.ProductResponseDto;
import com.github.Requu1.CampaignManager.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProductsForSeller(@RequestHeader("SellerId") UUID sellerId) {
        return ResponseEntity.ok(productService.getProductsForSeller(sellerId));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestHeader("SellerId") UUID sellerId,
                                                            @RequestBody @Valid ProductCreateDto productDto) {
        ProductResponseDto createProductDto=productService.addProduct(sellerId,productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createProductDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@RequestHeader("SellerId")UUID sellerId,
                                           @PathVariable("id") UUID productId) {
        productService.deleteProductById(sellerId,productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProductName(@RequestHeader("SellerId") UUID sellerId,
                                                                @PathVariable("id") UUID productId,
                                                                @RequestBody @Valid ProductCreateDto productDto){
        ProductResponseDto product=productService.changeProductName(sellerId,productId,productDto);
        return ResponseEntity.ok(product);
    }
}
