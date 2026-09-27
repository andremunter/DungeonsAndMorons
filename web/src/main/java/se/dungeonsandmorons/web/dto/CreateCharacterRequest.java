package se.dungeonsandmorons.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateCharacterRequest(
        @Schema(description = "Character name", example = "Ragnar")
        @NotBlank(message = "Character name is required")
        @Size(min = 2, max = 20, message = "Character name must be between 2 and 20 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Character name may only contain alphanumeric characters, hyphens and underscores.")
        String name,

        @Schema(description = "Class archetype", example = "WARRIOR")
        @NotBlank(message = "Character class is required")
        String characterClass
) {}
