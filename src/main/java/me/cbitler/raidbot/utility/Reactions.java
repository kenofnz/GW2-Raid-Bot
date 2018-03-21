package me.cbitler.raidbot.utility;

import me.cbitler.raidbot.RaidBot;
import net.dv8tion.jda.core.entities.Emote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reactions {
    /**
     * List of reactions representing classes
     */
    static String[] specs = {
            "Dragonhunter", //387295988282556417
            "Firebrand", //387296167958151169
            "Herald", //387296053659172869
            "Renegade", //387296192381321226
            "Berserker", //387296013947502592
            "Spellbreaker", //387296212421836800
            "Scrapper", //387296081823662081
            "Holosmith", //387296176770121738
            "Druid", // 387296044716916738
            "Soulbeast", //387296205488521216
            "Daredevil", //387296029533274113
            "Deadeye", //387296159716081664
            "Weaver", //387296219988361218
            "Tempest", //387296089340117002
            "Chronomancer", //387296021710897152
            "Mirage", //387296184114610185
            "Reaper", //387296061997318146
            "Scourge" //387296198928891905
    };

    static Emote[] reactions = {
            getEmoji("Dragonhunter"), // Dragonhunter
            getEmoji("Firebrand"), // Firebrand
            getEmoji("Herald"), // Herald
            getEmoji("Renegade"), // Renegade
            getEmoji("Berserker"), // Berserker
            getEmoji("Spellbreaker"), // Spellbreaker
            getEmoji("Scrapper"), // Scrapper
            getEmoji("Holosmith"), // Holosmith
            getEmoji("Druid"), // Druid
            getEmoji("Soulbeast"), // Soulbeast
            getEmoji("Daredevil"), // Daredevil
            getEmoji("Deadeye"), // Deadeye
            getEmoji("Weaver"), // Weaver
            getEmoji("Tempest"), // Tempest
            getEmoji("Chronomancer"), // Chronomancer
            getEmoji("Mirage"), // Mirage
            getEmoji("Reaper"), // Reaper
            getEmoji("Scourge"), // Scourge
            getEmoji("X_") // X_
    };

    /**
     * Get an emoji from it's emote ID via JDA
     * @param id The ID of the emoji
     * @return The emote object representing that emoji
     */
    private static Emote getEmoji(String name) {
        return RaidBot.getInstance().getJda().getEmotesByName(name, true).get(0);
    }

    /**
     * Get the list of reaction names as a list
     * @return The list of reactions as a list
     */
    public static List<String> getSpecs() {
        return new ArrayList<>(Arrays.asList(specs));
    }

    /**
     * Get the list of emote objects
     * @return The emotes
     */
    public static List<Emote> getEmotes() {
        return new ArrayList<>(Arrays.asList(reactions));
    }


}
