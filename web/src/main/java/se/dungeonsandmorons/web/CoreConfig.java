package se.dungeonsandmorons.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.dungeonsandmorons.repository.CharacterRepository;
import se.dungeonsandmorons.repository.PostgresCharacterRepository;
import se.dungeonsandmorons.service.SaveService;

@Configuration
public class CoreConfig {

    @Bean
    public CharacterRepository characterRepository() {
        return new PostgresCharacterRepository();
    }

    @Bean
    public SaveService saveService(CharacterRepository characterRepository) {
        return new SaveService(characterRepository);
    }
}
