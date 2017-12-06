package me.cbitler.raidbot.handlers;

import me.cbitler.raidbot.RaidBot;
import me.cbitler.raidbot.creation.CreationStep;
import me.cbitler.raidbot.creation.RunNameStep;
import me.cbitler.raidbot.raids.Raid;
import me.cbitler.raidbot.raids.RaidManager;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Handle channel message-related events sent to the bot
 * @author Christopher Bitler
 */
public class ChannelMessageHandler extends ListenerAdapter {

    /**
     * Handle receiving a message. This checks to see if it matches the !createRaid or !removeFromRaid commands
     * and acts on them accordingly.
     *
     * @param e The event
     */
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        RaidBot bot = RaidBot.getInstance();
        if (e.getAuthor().isBot()) {
           return;
        }

        if (hasRaidLeaderRole(e.getMember())) {
            if (e.getMessage().getRawContent().equalsIgnoreCase("!createRaid")) {
                CreationStep runNameStep = new RunNameStep(e.getMessage().getGuild().getId());
                e.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(runNameStep.getStepText()).queue());
                bot.getCreationMap().put(e.getAuthor().getId(), runNameStep);
                e.getMessage().delete().queue();
            } else if (e.getMessage().getRawContent().toLowerCase().startsWith("!removefromraid")) {
                String[] split = e.getMessage().getRawContent().split(" ");
                if(split.length < 3) {
                    e.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Format for !removeFromRaid: !removeFromRaid [raid id] [name]").queue());
                } else {
                    String messageId = split[1];
                    String name = split[2];

                    Raid raid = RaidManager.getRaid(messageId);

                    if (raid != null && raid.getServerId().equalsIgnoreCase(e.getGuild().getId())) {
                        if(raid.getUserByName(name) != null) {
                            raid.removeUserByName(name);
                        }
                    } else {
                        e.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Non-existant raid").queue());
                    }
                }
                e.getMessage().delete().queue();
            }
        }

        if (e.getMember().getPermissions().contains(Permission.MANAGE_SERVER)) {
            if(e.getMessage().getRawContent().toLowerCase().startsWith("!setraidleaderrole")) {
                String[] commandParts = e.getMessage().getRawContent().split(" ");
                String raidLeaderRole = combineArguments(commandParts,1);
                RaidBot.getInstance().setRaidLeaderRole(e.getMember().getGuild().getId(), raidLeaderRole);
                e.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Raid leader role updated to: " + raidLeaderRole).queue());
                e.getMessage().delete().queue();
            }
        }
    }

    /**
     * Check to see if a member has the raid leader role
     * @param member The member to check
     * @return True if they have the role, false if they don't
     */
    private boolean hasRaidLeaderRole(Member member) {
        String raidLeaderRole = RaidBot.getInstance().getRaidLeaderRole(member.getGuild().getId());
        for (Role role : member.getRoles()) {
            if (role.getName().equalsIgnoreCase(raidLeaderRole)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Combine the strings in an array of strings
     * @param parts The array of strings
     * @param offset The offset in the array to start at
     * @return The combined string
     */
    private String combineArguments(String[] parts, int offset) {
        String text = "";
        for (int i = offset; i < parts.length; i++) {
            text += (parts[i] + " ");
        }

        return text.trim();
    }
}