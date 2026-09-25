package se.dungeonsandmorons.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCharacterRequest(
        @NotBlank(message = "Character name is required")
        @Size(min = 2, max = 20, message = "Character name must be between 2 and 20 characters")
        String name,

        @NotBlank(message = "Character class is required")
        String characterClass
) {}
