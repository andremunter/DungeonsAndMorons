package se.dungeonsandmorons.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

public record AllocateStatsRequest(
        @Schema(description = "Points to allocate to Strength", example = "2", defaultValue = "0")
        @Min(value = 0, message = "Strength points to allocate cannot be negative.")
        int strength,

        @Schema(description = "Points to allocate to Agility", example = "2", defaultValue = "0")
        @Min(value = 0, message = "Agility points to allocate cannot be negative.")
        int agility,

        @Schema(description = "Points to allocate to Intelligence", example = "2", defaultValue = "0")
        @Min(value = 0, message = "Intelligence points to allocate cannot be negative.")
        int intelligence,

        @Schema(description = "Points to allocate to Vitality", example = "2", defaultValue = "0")
        @Min(value = 0, message = "Vitality points to allocate cannot be negative.")
        int vitality
) {

    public int totalPoints() {
        return strength + agility + intelligence + vitality;
    }

}
