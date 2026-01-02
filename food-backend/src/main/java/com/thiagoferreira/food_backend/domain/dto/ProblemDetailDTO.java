package com.thiagoferreira.food_backend.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO para representar erros no formato ProblemDetail (RFC 7807)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response following RFC 7807 Problem Detail standard")
public class ProblemDetailDTO {

    @Schema(description = "A URI reference that identifies the problem type", example = "https://api.food-backend.com/problems/resource-not-found")
    private String type;

    @Schema(description = "A short, human-readable summary of the problem type", example = "Resource Not Found")
    private String title;

    @Schema(description = "The HTTP status code", example = "404")
    private Integer status;

    @Schema(description = "A human-readable explanation specific to this occurrence of the problem", example = "User not found with ID: 123")
    private String detail;

    @Schema(description = "Additional properties providing more details about the error")
    private Map<String, Object> properties;
}

