package com.github.Requu1.CampaignManager.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dictionaries")
public class DictionaryController {
    @GetMapping("/keywords")
    public ResponseEntity<List<String>> getKeywords() {
        return ResponseEntity.ok(
                List.of("electronics","sport","gaming","home","fashion"));
    }

    @GetMapping("/towns")
    public ResponseEntity<List<String>> getTowns() {
        return ResponseEntity.ok(
                List.of("Kraków", "Warszawa", "Gdańsk", "Wrocław", "Poznań", "Łódź"));
    }
}
