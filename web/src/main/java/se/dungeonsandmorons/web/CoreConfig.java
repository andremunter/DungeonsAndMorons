package se.dungeonsandmorons.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.dungeonsandmorons.repository.CharacterRepository;
import se.dungeonsandmorons.repository.PostgresCharacterRepository;
import se.dungeonsandmorons.service.DungeonService;
import se.dungeonsandmorons.service.SaveService;
import se.dungeonsandmorons.session.PlayerSessionService;

import javax.sql.DataSource;

@Configuration
public class CoreConfig {

    @Bean
    public CharacterRepository characterRepository(DataSource dataSource) {
        return new PostgresCharacterRepository(dataSource);
    }

    @Bean
    public SaveService saveService(CharacterRepository characterRepository) {
        return new SaveService(characterRepository);
    }

    @Bean
    public DungeonService dungeonService(SaveService saveService) {
        return new DungeonService(saveService);
    }

    @Bean
    public PlayerSessionService playerSessionService(CharacterRepository characterRepository) {
        return new PlayerSessionService(characterRepository);
    }
}
