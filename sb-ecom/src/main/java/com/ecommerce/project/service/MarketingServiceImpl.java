package com.ecommerce.project.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Service;

/**
 * Implementation of MarketingService using LangChain4j with AWS Bedrock
 */
@Service
public class MarketingServiceImpl implements MarketingService {

    private final ChatLanguageModel chatModel;

    public MarketingServiceImpl(ChatLanguageModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public String generateMarketingDescription(String productName) {
        String prompt = String.format(
            "Create an engaging marketing description for a product called '%s'. " +
            "The description should be compelling, highlight key benefits, and encourage customers to purchase. " +
            "Keep it between 50-100 words and make it sound professional yet appealing.",
            productName
        );

        try {
            return chatModel.generate(prompt);
        } catch (Exception e) {
            // Fallback in case of AI service failure
            return String.format("Discover the amazing %s - a premium product designed to meet your needs with exceptional quality and value.", productName);
        }
    }

    @Override
    public String generateMarketingDescription(String productName, String category, Double price) {
        String priceContext = price != null ? String.format(" priced at $%.2f", price) : "";
        
        String prompt = String.format(
            "Create an engaging marketing description for a %s product called '%s'%s. " +
            "The description should be compelling, highlight key benefits specific to the %s category, " +
            "and encourage customers to purchase. Include value proposition if price is mentioned. " +
            "Keep it between 50-100 words and make it sound professional yet appealing.",
            category, productName, priceContext, category
        );

        try {
            return chatModel.generate(prompt);
        } catch (Exception e) {
            // Fallback in case of AI service failure
            return String.format("Discover the amazing %s in our %s collection%s - a premium product designed to meet your needs with exceptional quality and value.", 
                productName, category, priceContext.isEmpty() ? "" : " at an unbeatable price");
        }
    }
}