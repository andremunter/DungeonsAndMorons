package se.dungeonsandmorons.session;

import org.springframework.stereotype.Service;
import se.dungeonsandmorons.repository.CharacterRepository;
import se.dungeonsandmorons.repository.CharacterSaveData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PlayerSessionService {
    private final Map<String, GameSession> sessions = new ConcurrentHashMap<>();
    private final CharacterRepository characterRepository;

    public PlayerSessionService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    public GameSession loadCharacter(String sessionId, String characterName) {
        CharacterSaveData saveData = characterRepository.findByName(characterName)
                .orElseThrow(() -> new IllegalArgumentException("Character not found: " + characterName));

        GameSession session = new GameSession(sessionId, saveData.player());
        sessions.put(sessionId, session);
        return session;
    }

    public GameSession getSession(String sessionId) {
        GameSession session = sessions.get(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("No active session found for id: " + sessionId);
        }
        return session;
    }

}
