package com.github.Requu1.CampaignManager.service;

import com.github.Requu1.CampaignManager.dto.seller.SellerLoginDto;
import com.github.Requu1.CampaignManager.dto.seller.SellerRegisterDto;
import com.github.Requu1.CampaignManager.dto.seller.SellerResponseDto;
import com.github.Requu1.CampaignManager.exception.SellerNotFoundException;
import com.github.Requu1.CampaignManager.model.Seller;
import com.github.Requu1.CampaignManager.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final static BigDecimal DEFAULT_EMERALD_BALANCE = BigDecimal.valueOf(1000);

    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;

    private SellerResponseDto mapToDto(Seller seller) {
        return SellerResponseDto.builder()
                .id(seller.getId())
                .username(seller.getUsername())
                .email(seller.getEmail())
                .emeraldBalance(seller.getEmeraldBalance())
                .build();
    }

    public SellerResponseDto register(SellerRegisterDto sellerRegisterDto){
        if(sellerRepository.existsByEmail(sellerRegisterDto.getEmail())){
            throw new IllegalArgumentException("Account with this email already exists");
        }
        if(sellerRepository.existsByUsername(sellerRegisterDto.getUsername())){
            throw new IllegalArgumentException("Account with this username already exists");
        }

        Seller seller = Seller.builder()
                .email(sellerRegisterDto.getEmail())
                .username(sellerRegisterDto.getUsername())
                .password(passwordEncoder.encode(sellerRegisterDto.getPassword()))
                .emeraldBalance(DEFAULT_EMERALD_BALANCE)
                .build();
        sellerRepository.save(seller);
        return mapToDto(seller);
    }

    public SellerResponseDto login(SellerLoginDto sellerLoginDto){
        Seller seller=sellerRepository.findByEmail(sellerLoginDto.getEmail())
                .orElseThrow(()-> new IllegalArgumentException("Invalid email or password"));

        if(!passwordEncoder.matches(sellerLoginDto.getPassword(),seller.getPassword())){
            throw new IllegalArgumentException("Invalid email or password");
        }

        return mapToDto(seller);
    }

    public SellerResponseDto getSellerById(UUID sellerId){
        return mapToDto(findSeller(sellerId));
    }


    public Seller findSeller(UUID sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(()->new SellerNotFoundException(String.format("Seller with ID:%s not found.",sellerId)));
    }

}
