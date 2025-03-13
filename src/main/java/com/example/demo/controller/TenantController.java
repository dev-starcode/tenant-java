package com.example.demo.controller;

import com.example.demo.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @PostMapping("/create")
    public String createTenant(@RequestParam String tenantName) {
        try {
            tenantService.createTenant(tenantName);
            return "Tenant " + tenantName + " created and migration executed!";
        } catch (Exception e) {
            return "Error creating tenant: " + e.getMessage();
        }
    }
}
