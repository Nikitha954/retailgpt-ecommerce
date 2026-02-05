package com.ecommerce.project.service;

/**
 * Service interface for generating marketing content using AI
 */
public interface MarketingService {
    
    /**
     * Generates a marketing description for a product using AI
     * 
     * @param productName the name of the product
     * @return AI-generated marketing description
     */
    String generateMarketingDescription(String productName);
    
    /**
     * Generates a marketing description with additional context
     * 
     * @param productName the name of the product
     * @param category the product category for better context
     * @param price the product price for value proposition
     * @return AI-generated marketing description with context
     */
    String generateMarketingDescription(String productName, String category, Double price);
}