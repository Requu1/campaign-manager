package com.github.Requu1.CampaignManager.service;

import com.github.Requu1.CampaignManager.dto.seller.SellerLoginDto;
import com.github.Requu1.CampaignManager.dto.seller.SellerRegisterDto;
import com.github.Requu1.CampaignManager.dto.seller.SellerResponseDto;
import com.github.Requu1.CampaignManager.exception.InsufficientFundsException;
import com.github.Requu1.CampaignManager.exception.SellerNotFoundException;
import com.github.Requu1.CampaignManager.model.Seller;
import com.github.Requu1.CampaignManager.repository.SellerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SellerService sellerService;

    @Test
    void registerSuccessfully() {
        SellerRegisterDto dto = SellerRegisterDto.builder()
                .email("test@test.com")
                .username("testuser")
                .password("password")
                .build();

        when(sellerRepository.existsByEmail(dto.email())).thenReturn(false);
        when(sellerRepository.existsByUsername(dto.username())).thenReturn(false);
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");

        SellerResponseDto response = sellerService.register(dto);

        assertNotNull(response);
        assertEquals(dto.username(), response.username());
        verify(sellerRepository, times(1)).save(any(Seller.class));
    }

    @Test
    void registerThrowsExceptionWhenEmailExists() {
        SellerRegisterDto dto = SellerRegisterDto.builder()
                .email("test@test.com")
                .username("testuser")
                .password("password")
                .build();

        when(sellerRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> sellerService.register(dto));
        verify(sellerRepository, never()).save(any(Seller.class));
    }

    @Test
    void loginSuccessfully() {
        SellerLoginDto dto = SellerLoginDto.builder()
                .email("test@test.com")
                .password("password")
                .build();

        Seller seller = Seller.builder()
                .email("test@test.com")
                .password("encodedPassword")
                .build();

        when(sellerRepository.findByEmail(dto.email())).thenReturn(Optional.of(seller));
        when(passwordEncoder.matches(dto.password(), seller.getPassword())).thenReturn(true);

        SellerResponseDto response = sellerService.login(dto);

        assertNotNull(response);
        assertEquals(dto.email(), response.email());
    }

    @Test
    void loginThrowsExceptionWhenInvalidPassword() {
        SellerLoginDto dto = SellerLoginDto.builder()
                .email("test@test.com")
                .password("wrongpassword")
                .build();

        Seller seller = Seller.builder()
                .email("test@test.com")
                .password("encodedPassword")
                .build();

        when(sellerRepository.findByEmail(dto.email())).thenReturn(Optional.of(seller));
        when(passwordEncoder.matches(dto.password(), seller.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> sellerService.login(dto));
    }

    @Test
    void chargeFundsSuccessfully() {
        UUID sellerId = UUID.randomUUID();

        Seller seller = Seller.builder()
                .id(sellerId)
                .emeraldBalance(BigDecimal.valueOf(100))
                .build();

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(seller));

        sellerService.chargeFunds(sellerId, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(50), seller.getEmeraldBalance());
    }

    @Test
    void chargeFundsThrowsExceptionWhenInsufficientFunds() {
        UUID sellerId = UUID.randomUUID();

        Seller seller = Seller.builder()
                .id(sellerId)
                .emeraldBalance(BigDecimal.valueOf(10))
                .build();

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(seller));

        assertThrows(InsufficientFundsException.class, () -> sellerService.chargeFunds(sellerId, BigDecimal.valueOf(50)));
    }

    @Test
    void refundFundsSuccessfully() {
        UUID sellerId = UUID.randomUUID();

        Seller seller = Seller.builder()
                .id(sellerId)
                .emeraldBalance(BigDecimal.valueOf(100))
                .build();

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(seller));

        sellerService.refundFunds(sellerId, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), seller.getEmeraldBalance());
    }

    @Test
    void findSellerThrowsExceptionWhenNotFound() {
        UUID sellerId = UUID.randomUUID();
        when(sellerRepository.findById(sellerId)).thenReturn(Optional.empty());

        assertThrows(SellerNotFoundException.class, () -> sellerService.findSeller(sellerId));
    }
}