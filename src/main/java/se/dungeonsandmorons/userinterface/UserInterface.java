package se.dungeonsandmorons.userinterface;

public class UserInterface {



    public static void helpMenu() {
        IO.print("""
            Available commands:
            attack monsterName - Engages in combat with specified monster
            stats              - Shows player stats
            monsters           - Shows monsters in area
            load playerName    - Loads specified character
            save               - Saves current character progress
            characters         - Lists all saved characters
            help               - Lists all available commands
            """);
    }


}
