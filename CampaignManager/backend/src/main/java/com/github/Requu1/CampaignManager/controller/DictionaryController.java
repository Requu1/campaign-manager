package com.github.Requu1.CampaignManager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.github.Requu1.CampaignManager.util.DictionaryUtil.getAllKeywords;
import static com.github.Requu1.CampaignManager.util.DictionaryUtil.getAllTowns;

@RestController
@RequestMapping("/api/dictionaries")
public class DictionaryController {
    @GetMapping("/keywords")
    public ResponseEntity<List<String>> getKeywords() {
        return ResponseEntity.ok(getAllKeywords());
    }

    @GetMapping("/towns")
    public ResponseEntity<List<String>> getTowns() {
        return ResponseEntity.ok(getAllTowns());
    }
}
