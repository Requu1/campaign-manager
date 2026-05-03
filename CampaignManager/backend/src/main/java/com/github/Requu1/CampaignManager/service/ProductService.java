package com.github.Requu1.CampaignManager.service;

import com.github.Requu1.CampaignManager.dto.product.ProductCreateDto;
import com.github.Requu1.CampaignManager.dto.product.ProductResponseDto;
import com.github.Requu1.CampaignManager.exception.NoPermissionException;
import com.github.Requu1.CampaignManager.exception.ProductNotFoundException;
import com.github.Requu1.CampaignManager.model.Product;
import com.github.Requu1.CampaignManager.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final SellerService sellerService;

    private ProductResponseDto mapToDto(Product product){
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .build();
    }

    public List<ProductResponseDto> getProductsForSeller(UUID sellerId){
        sellerService.findSeller(sellerId);
        return productRepository.findAllBySellerId(sellerId).stream()
                .map(this::mapToDto)
                .toList();
    }


    @Transactional
    public ProductResponseDto addProduct(UUID sellerId, ProductCreateDto productDto){
        if(productRepository.existsByNameAndSellerId(productDto.name(),sellerId)){
            throw new IllegalArgumentException("Product already exists");
        }

        Product product=Product.builder()
                .name(productDto.name())
                .seller(sellerService.findSeller(sellerId))
                .build();
        productRepository.save(product);
        return mapToDto(product);
    }

    @Transactional
    public ProductResponseDto changeProductName(UUID sellerId, UUID productId, ProductCreateDto productDto){
        Product product=findById(productId);

        if(!product.getSeller().getId().equals(sellerId)){
            throw new NoPermissionException(String.format("No permission to modify product with ID:%s",productId));
        }

        if(product.getName().equals(productDto.name())){
            return mapToDto(product);
        }

        if(productRepository.existsByNameAndSellerId(productDto.name(),sellerId)){
            throw new IllegalArgumentException("Product with this name already exists");
        }
        product.setName(productDto.name());
        return mapToDto(product);
    }


    @Transactional
    public void deleteProductById(UUID sellerId, UUID productId) {
        Product product = findById(productId);
        if (!product.getSeller().getId().equals(sellerId)) {
            throw new NoPermissionException(String.format("No permission to delete product with ID:%s",productId));
        }
        product.getCampaigns()
                .forEach(campaign-> sellerService.refundFunds(sellerId,campaign.getCampaignFund()));
        productRepository.delete(product);
    }

    Product findById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException(String.format("Product with ID:%s not found",id)));
    }

    Product findOwnedProduct(UUID sellerId, UUID productId) {
        Product product = findById(productId);
        if (!product.getSeller().getId().equals(sellerId)) {
            throw new NoPermissionException(String.format("No permission to access product with ID:%s", productId));
        }
        return product;
    }
}
