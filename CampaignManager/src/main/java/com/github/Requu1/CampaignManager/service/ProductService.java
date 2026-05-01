package com.github.Requu1.CampaignManager.service;

import com.github.Requu1.CampaignManager.exception.ProductNotFoundException;
import com.github.Requu1.CampaignManager.model.Product;
import com.github.Requu1.CampaignManager.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProductsForSeller(UUID sellerId){
        return productRepository.findAllBySellerId(sellerId);
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProductById(UUID id) {
        productRepository.delete(findById(id));
    }

    private Product findById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException(String.format("Product with ID:%s not found",id)));
    }
}
