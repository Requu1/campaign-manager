package com.github.Requu1.CampaignManager.service;

import com.github.Requu1.CampaignManager.exception.SellerNotFoundException;
import com.github.Requu1.CampaignManager.model.Seller;
import com.github.Requu1.CampaignManager.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;

    public Seller findSeller(UUID sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(()->new SellerNotFoundException(String.format("Seller with ID:%s not found.",sellerId)));
    }

}
