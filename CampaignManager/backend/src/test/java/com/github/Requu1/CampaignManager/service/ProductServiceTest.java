package com.github.Requu1.CampaignManager.service;

import com.github.Requu1.CampaignManager.dto.product.ProductCreateDto;
import com.github.Requu1.CampaignManager.dto.product.ProductResponseDto;
import com.github.Requu1.CampaignManager.exception.NoPermissionException;
import com.github.Requu1.CampaignManager.exception.ProductNotFoundException;
import com.github.Requu1.CampaignManager.model.Product;
import com.github.Requu1.CampaignManager.model.Seller;
import com.github.Requu1.CampaignManager.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SellerService sellerService;

    @InjectMocks
    private ProductService productService;

    @Test
    void getProductsForSellerSuccessfully() {
        UUID sellerId = UUID.randomUUID();

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("Product1")
                .build();

        when(sellerService.findSeller(sellerId)).thenReturn(new Seller());
        when(productRepository.findAllBySellerId(sellerId)).thenReturn(List.of(product));

        List<ProductResponseDto> responses = productService.getProductsForSeller(sellerId);

        assertEquals(1, responses.size());
        assertEquals("Product1", responses.getFirst().name());
    }

    @Test
    void addProductSuccessfully() {
        UUID sellerId = UUID.randomUUID();

        ProductCreateDto dto = ProductCreateDto.builder()
                .name("New Product")
                .build();

        Seller seller = Seller.builder()
                .id(sellerId)
                .build();

        when(productRepository.existsByNameAndSellerId(dto.name(), sellerId)).thenReturn(false);
        when(sellerService.findSeller(sellerId)).thenReturn(seller);

        ProductResponseDto response = productService.addProduct(sellerId, dto);

        assertNotNull(response);
        assertEquals(dto.name(), response.name());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void addProductThrowsExceptionWhenExists() {
        UUID sellerId = UUID.randomUUID();

        ProductCreateDto dto = ProductCreateDto.builder()
                .name("Existing Product")
                .build();

        when(productRepository.existsByNameAndSellerId(dto.name(), sellerId)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> productService.addProduct(sellerId, dto));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void changeProductNameSuccessfully() {
        UUID sellerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        ProductCreateDto dto = ProductCreateDto.builder()
                .name("Updated Product")
                .build();

        Seller seller = Seller.builder()
                .id(sellerId)
                .build();

        Product product = Product.builder()
                .id(productId)
                .name("Old Product")
                .seller(seller)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.existsByNameAndSellerId(dto.name(), sellerId)).thenReturn(false);

        ProductResponseDto response = productService.changeProductName(sellerId, productId, dto);

        assertEquals(dto.name(), response.name());
        assertEquals(dto.name(), product.getName());
    }

    @Test
    void deleteProductByIdSuccessfully() {
        UUID sellerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Seller seller = Seller.builder()
                .id(sellerId)
                .build();

        Product product = Product.builder()
                .id(productId)
                .seller(seller)
                .campaigns(Collections.emptyList())
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.deleteProductById(sellerId, productId);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void findOwnedProductThrowsExceptionWhenNoPermission() {
        UUID sellerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Seller differentSeller = Seller.builder()
                .id(UUID.randomUUID())
                .build();

        Product product = Product.builder()
                .id(productId)
                .seller(differentSeller)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThrows(NoPermissionException.class, () -> productService.findOwnedProduct(sellerId, productId));
    }

    @Test
    void findByIdThrowsExceptionWhenNotFound() {
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(productId));
    }
}