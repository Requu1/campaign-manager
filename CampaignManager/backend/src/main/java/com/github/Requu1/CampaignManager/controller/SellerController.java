package com.github.Requu1.CampaignManager.controller;

import com.github.Requu1.CampaignManager.dto.seller.SellerLoginDto;
import com.github.Requu1.CampaignManager.dto.seller.SellerRegisterDto;
import com.github.Requu1.CampaignManager.dto.seller.SellerResponseDto;
import com.github.Requu1.CampaignManager.service.SellerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.github.Requu1.CampaignManager.util.SessionUtil.getSellerIdFromSession;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {
    private final SellerService sellerService;

    @PostMapping("/register")
    public ResponseEntity<SellerResponseDto> register(@RequestBody @Valid SellerRegisterDto sellerRegisterDto,
                                                      HttpSession session){
        SellerResponseDto sellerResponseDto=sellerService.register(sellerRegisterDto);
        session.setAttribute("LOGGED_IN_SELLER_ID",sellerResponseDto.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(sellerResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<SellerResponseDto> login(@RequestBody @Valid SellerLoginDto sellerLoginDto,
                                                   HttpSession session){
        SellerResponseDto sellerResponseDto=sellerService.login(sellerLoginDto);
        session.setAttribute("LOGGED_IN_SELLER_ID",sellerResponseDto.id());
        return ResponseEntity.ok(sellerResponseDto);
    }

    @GetMapping("/me")
    public ResponseEntity<SellerResponseDto> me(HttpSession session) {
        UUID sellerId = getSellerIdFromSession(session);
        SellerResponseDto sellerResponseDto = sellerService.getSellerById(sellerId);
        return ResponseEntity.ok(sellerResponseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }
}
