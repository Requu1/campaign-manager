package com.github.Requu1.CampaignManager.controller;

import com.github.Requu1.CampaignManager.dto.product.ProductCreateDto;
import com.github.Requu1.CampaignManager.dto.product.ProductResponseDto;
import com.github.Requu1.CampaignManager.exception.NoPermissionException;
import com.github.Requu1.CampaignManager.service.CampaignService;
import com.github.Requu1.CampaignManager.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.github.Requu1.CampaignManager.util.SessionUtil.getSellerIdFromSession;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CampaignService campaignService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProductsForSeller(HttpSession session) {
        UUID sellerId=getSellerIdFromSession(session);
        return ResponseEntity.ok(productService.getProductsForSeller(sellerId));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(HttpSession session,
                                                            @RequestBody @Valid ProductCreateDto productDto) {
        UUID sellerId=getSellerIdFromSession(session);
        ProductResponseDto createProductDto=productService.addProduct(sellerId,productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createProductDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(HttpSession session,
                                           @PathVariable("id") UUID productId) {
        UUID sellerId=getSellerIdFromSession(session);
        campaignService.getCampaignsForProduct(sellerId,productId)
                .forEach(campaign -> campaignService.removeCampaign(sellerId,productId,campaign.getId()));
        productService.deleteProductById(sellerId,productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProductName(HttpSession session,
                                                                @PathVariable("id") UUID productId,
                                                                @RequestBody @Valid ProductCreateDto productDto){
        UUID sellerId=getSellerIdFromSession(session);
        ProductResponseDto product=productService.changeProductName(sellerId,productId,productDto);
        return ResponseEntity.ok(product);
    }
}
