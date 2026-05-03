package com.github.Requu1.CampaignManager.service;

import com.github.Requu1.CampaignManager.dto.campaign.CampaignCreateDto;
import com.github.Requu1.CampaignManager.dto.campaign.CampaignResponseDto;
import com.github.Requu1.CampaignManager.exception.CampaignNotFoundException;
import com.github.Requu1.CampaignManager.exception.NoPermissionException;
import com.github.Requu1.CampaignManager.model.Campaign;
import com.github.Requu1.CampaignManager.model.Product;
import com.github.Requu1.CampaignManager.model.Seller;
import com.github.Requu1.CampaignManager.repository.CampaignRepository;
import com.github.Requu1.CampaignManager.util.DictionaryUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CampaignServiceTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private ProductService productService;

    @Mock
    private SellerService sellerService;

    @InjectMocks
    private CampaignService campaignService;

    private MockedStatic<DictionaryUtil> mockedDictionaryUtil;

    @BeforeEach
    void setUp() {
        mockedDictionaryUtil = mockStatic(DictionaryUtil.class);
    }

    @AfterEach
    void tearDown() {
        mockedDictionaryUtil.close();
    }

    @Test
    void getCampaignsForProductSuccessfully() {
        UUID sellerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Seller seller = Seller.builder()
                .id(sellerId)
                .emeraldBalance(BigDecimal.valueOf(100))
                .build();

        Product product = Product.builder()
                .id(productId)
                .seller(seller)
                .build();

        Campaign campaign = Campaign.builder()
                .id(UUID.randomUUID())
                .product(product)
                .build();

        when(productService.findOwnedProduct(sellerId, productId)).thenReturn(product);
        when(campaignRepository.findAllByProductId(productId)).thenReturn(List.of(campaign));

        List<CampaignResponseDto> responses = campaignService.getCampaignsForProduct(sellerId, productId);

        assertEquals(1, responses.size());
        assertEquals(campaign.getId(), responses.getFirst().id());
    }

    @Test
    void createCampaignSuccessfully() {
        UUID sellerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        CampaignCreateDto dto = CampaignCreateDto.builder()
                .name("New Campaign")
                .town("Warsaw")
                .keywords(List.of("keyword"))
                .campaignFund(BigDecimal.valueOf(100))
                .build();

        Seller seller = Seller.builder()
                .id(sellerId)
                .emeraldBalance(BigDecimal.valueOf(1000))
                .build();

        Product product = Product.builder()
                .id(productId)
                .seller(seller)
                .build();

        when(productService.findOwnedProduct(sellerId, productId)).thenReturn(product);
        when(campaignRepository.existsByNameAndProductId(dto.name(), productId)).thenReturn(false);
        mockedDictionaryUtil.when(() -> DictionaryUtil.isTownValid(dto.town())).thenReturn(true);
        mockedDictionaryUtil.when(() -> DictionaryUtil.areKeywordsValid(dto.keywords())).thenReturn(true);

        CampaignResponseDto response = campaignService.createCampaign(sellerId, productId, dto);

        assertNotNull(response);
        assertEquals(dto.name(), response.name());
        verify(sellerService, times(1)).chargeFunds(sellerId, dto.campaignFund());
        verify(campaignRepository, times(1)).save(any(Campaign.class));
    }

    @Test
    void createCampaignThrowsExceptionWhenNameExists() {
        UUID sellerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        CampaignCreateDto dto = CampaignCreateDto.builder()
                .name("Existing Campaign")
                .build();

        when(productService.findOwnedProduct(sellerId, productId)).thenReturn(new Product());
        when(campaignRepository.existsByNameAndProductId(dto.name(), productId)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> campaignService.createCampaign(sellerId, productId, dto));
    }

    @Test
    void removeCampaignSuccessfully() {
        UUID sellerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID campaignId = UUID.randomUUID();

        Seller seller = Seller.builder()
                .id(sellerId)
                .build();

        Product product = Product.builder()
                .id(productId)
                .seller(seller)
                .build();

        Campaign campaign = Campaign.builder()
                .id(campaignId)
                .product(product)
                .campaignFund(BigDecimal.valueOf(50))
                .build();

        when(productService.findOwnedProduct(sellerId, productId)).thenReturn(product);
        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));

        campaignService.removeCampaign(sellerId, productId, campaignId);

        verify(sellerService, times(1)).refundFunds(sellerId, campaign.getCampaignFund());
        verify(campaignRepository, times(1)).delete(campaign);
    }

    @Test
    void removeCampaignThrowsExceptionWhenNoPermission() {
        UUID sellerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID campaignId = UUID.randomUUID();

        Product correctProduct = Product.builder()
                .id(productId)
                .build();

        Product wrongProduct = Product.builder()
                .id(UUID.randomUUID())
                .build();

        Campaign campaign = Campaign.builder()
                .id(campaignId)
                .product(wrongProduct)
                .build();

        when(productService.findOwnedProduct(sellerId, productId)).thenReturn(correctProduct);
        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));

        assertThrows(NoPermissionException.class, () -> campaignService.removeCampaign(sellerId, productId, campaignId));
    }

    @Test
    void updateCampaignThrowsExceptionWhenCampaignNotFound() {
        UUID sellerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID campaignId = UUID.randomUUID();

        CampaignCreateDto dto = CampaignCreateDto.builder()
                .build();

        when(productService.findOwnedProduct(sellerId, productId)).thenReturn(new Product());
        when(campaignRepository.findById(campaignId)).thenReturn(Optional.empty());

        assertThrows(CampaignNotFoundException.class, () -> campaignService.updateCampaign(sellerId, productId, campaignId, dto));
    }
}