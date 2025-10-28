package com.thb.bakery.controller;

import com.thb.bakery.dto.request.AddonDto;
import com.thb.bakery.service.AddonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addons")
public class AddonController {

    @Autowired
    private AddonService addonService;

    @GetMapping("/get-all-addon-items")
    public ResponseEntity<List<AddonDto>> getAllAddons() {
        List<AddonDto> addons = addonService.getAllAddons();
        return ResponseEntity.ok(addons);
    }

    @PostMapping("/add-add-on")
    public ResponseEntity<AddonDto> createAddon(@RequestBody AddonDto addonDto) {
        AddonDto createdAddon = addonService.createAddon(addonDto);
        return ResponseEntity.ok(createdAddon);
    }
}