package com.ecommerce.project.config;

import dev.langchain4j.model.bedrock.BedrockChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;

/**
 * Configuration class for AWS Bedrock integration with LangChain4j
 */
@Configuration
public class BedrockConfig {

    @Value("${aws.bedrock.region:us-east-1}")
    private String region;

    @Value("${aws.bedrock.model:anthropic.claude-3-sonnet-20240229-v1:0}")
    private String modelId;

    @Value("${aws.bedrock.temperature:0.7}")
    private Double temperature;

    @Value("${aws.bedrock.max-tokens:500}")
    private Integer maxTokens;

    /**
     * Creates a ChatLanguageModel bean configured for AWS Bedrock
     */
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return BedrockChatModel.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .modelId(modelId)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .build();
    }
}