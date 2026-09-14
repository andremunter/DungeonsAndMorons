package se.dungeonsandmorons.model.npc;

public record Npc(String name, String role, String dialogue) {

    public boolean matches(String query) {
        if (query == null || query.isBlank()) {
            return false;
        }

        String cleanQuery = query.trim();
        return name.equalsIgnoreCase(cleanQuery) || role.equalsIgnoreCase(cleanQuery);
    }

}
