package com.ecommerce.project.controller;

import com.ecommerce.project.service.MarketingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for marketing-related operations
 */
@RestController
@RequestMapping("/api/marketing")
@Tag(name = "Marketing", description = "AI-powered marketing content generation")
public class MarketingController {

    private final MarketingService marketingService;

    public MarketingController(MarketingService marketingService) {
        this.marketingService = marketingService;
    }

    @GetMapping("/description")
    @Operation(summary = "Generate marketing description", 
               description = "Generate an AI-powered marketing description for a product")
    public ResponseEntity<String> generateMarketingDescription(
            @Parameter(description = "Product name", required = true)
            @RequestParam String productName) {
        
        String description = marketingService.generateMarketingDescription(productName);
        return ResponseEntity.ok(description);
    }

    @GetMapping("/description/detailed")
    @Operation(summary = "Generate detailed marketing description", 
               description = "Generate an AI-powered marketing description with category and price context")
    public ResponseEntity<String> generateDetailedMarketingDescription(
            @Parameter(description = "Product name", required = true)
            @RequestParam String productName,
            @Parameter(description = "Product category")
            @RequestParam(required = false) String category,
            @Parameter(description = "Product price")
            @RequestParam(required = false) Double price) {
        
        String description = marketingService.generateMarketingDescription(productName, category, price);
        return ResponseEntity.ok(description);
    }
}