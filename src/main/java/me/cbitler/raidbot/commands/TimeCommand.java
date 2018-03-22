package me.cbitler.raidbot.commands;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.LinkedHashMap;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.time.*;

public class TimeCommand implements Command {

	private static LinkedHashMap<String,ZoneId> timezones = new LinkedHashMap<>();
	private static final String OUTPUT_FORMAT = "h:mm a";
	private static final String DATE_FORMAT = "dd-M-yyyy h:mm:ss a";
	
	static {
		timezones.put("Los Angeles", ZoneId.of("America/Los_Angeles"));
		timezones.put("New York", ZoneId.of("America/New_York")); //-5
		timezones.put("Toronto", ZoneId.of("America/Toronto")); //-5
		timezones.put("London", ZoneId.of("Europe/London")); //0
		timezones.put("Kuala Lumpur", ZoneId.of("Asia/Kuala_Lumpur")); //+8
		timezones.put("Perth", ZoneId.of("Australia/Perth")); //+08:00
		timezones.put("Brisbane", ZoneId.of("Australia/Brisbane")); //+10
		timezones.put("Sydney", ZoneId.of("Australia/Sydney"));//+10
		timezones.put("Auckland", ZoneId.of("Pacific/Auckland")); //+12
	}
	

    @Override
    public void handleCommand(String command, String[] args, TextChannel channel, User author) {
    	String response = "Please state a time in `HH:MM AM/PM` format. E.G. `!time 12:30 PM`\n"
    						+ "For another timezone use `!time HH:MM AM/PM Timezone` E.G. `!time 12:30 PM GMT-8`\n"
    						+ "Timezones supported are **UTC** and **GMT**. Feel free to use them.";

    	if (args.length > 1){
    		try {
	    		LocalDateTime now = LocalDateTime.now();
	    		String currentDate = now.getDayOfMonth() + "-" + now.getMonthValue() + "-" + now.getYear() + " " + args[0] + ":00 " + args[1];
	    		LocalDateTime ldt = LocalDateTime.parse(currentDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
	    		ZonedDateTime baseZonedTime = ldt.atZone(timezones.get("Los Angeles"));
	    		response = "";
				for (Map.Entry<String,ZoneId> entry : timezones.entrySet()) {
					ZonedDateTime targetZonedDateTime = baseZonedTime.withZoneSameInstant(entry.getValue());
	    			response += "**" + entry.getKey() +" Time**: " + DateTimeFormatter.ofPattern(OUTPUT_FORMAT).format(targetZonedDateTime) + "\n";
				}

				if (args.length > 2) {
					ZonedDateTime extraZonedDateTime = baseZonedTime.withZoneSameInstant(ZoneId.of(args[2]));
		    		response += "**" + args[2] +" Time**: " + DateTimeFormatter.ofPattern(OUTPUT_FORMAT).format(extraZonedDateTime) + "\n";
				}
			} catch (Exception e){
				System.err.println(e);
			}
    	}
        channel.sendMessage(response).queue();
    }
}
