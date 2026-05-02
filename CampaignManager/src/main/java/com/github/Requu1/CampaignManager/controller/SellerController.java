package com.github.Requu1.CampaignManager.controller;

import com.github.Requu1.CampaignManager.dto.seller.SellerLoginDto;
import com.github.Requu1.CampaignManager.dto.seller.SellerRegisterDto;
import com.github.Requu1.CampaignManager.dto.seller.SellerResponseDto;
import com.github.Requu1.CampaignManager.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {
    private final SellerService sellerService;

    @PostMapping("/register")
    public ResponseEntity<SellerResponseDto> register(@RequestBody @Valid SellerRegisterDto sellerRegisterDto){
        SellerResponseDto sellerResponseDto=sellerService.register(sellerRegisterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sellerResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<SellerResponseDto> login(@RequestBody @Valid SellerLoginDto sellerLoginDto){
        SellerResponseDto sellerResponseDto=sellerService.login(sellerLoginDto);
        return ResponseEntity.ok(sellerResponseDto);
    }

    @GetMapping("/{sellerId}")
    public ResponseEntity<SellerResponseDto> seller(@PathVariable("sellerId") UUID sellerId){
        SellerResponseDto sellerResponseDto= sellerService.getSellerById(sellerId);
        return ResponseEntity.ok(sellerResponseDto);
    }

}
