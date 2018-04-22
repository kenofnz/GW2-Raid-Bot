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
	private static final String DATE_FORMAT = "d-M-yyyy h:mm:ss a";
	
	static {
		timezones.put("Los Angeles", ZoneId.of("America/Los_Angeles"));
		timezones.put("Boston", ZoneId.of("America/New_York")); //-5
		timezones.put("Toronto", ZoneId.of("America/Toronto")); //-5
		timezones.put("London", ZoneId.of("Europe/London")); //0
		timezones.put("Jakarta", ZoneId.of("Asia/Jakarta"));//+7
		timezones.put("Kuala Lumpur", ZoneId.of("Asia/Kuala_Lumpur")); //+8
		timezones.put("Perth", ZoneId.of("Australia/Perth")); //+08:00
		timezones.put("Brisbane", ZoneId.of("Australia/Brisbane")); //+10
		timezones.put("Sydney", ZoneId.of("Australia/Sydney"));//+10
		timezones.put("Auckland", ZoneId.of("Pacific/Auckland")); //+12
	}
	

    @Override
    public void handleCommand(String command, String[] args, TextChannel channel, User author) {
    	String format = "|%13s|%10s|";
    	String response = "Please state a time in `HH:MM AM/PM` format. E.G. `+time 12:30 PM`\n"
    						+ "Use `+time now` for current time\n"
    						+ "For another timezone use `!time HH:MM AM/PM Timezone` E.G. `+time 12:30 PM GMT-8`\n"
    						+ "Timezones supported are **UTC** and **GMT**. Feel free to use them.\n";
    	try {
	    	if (args.length > 0){
	    		LocalDateTime now = LocalDateTime.now(timezones.get("Los Angeles"));
				LocalDateTime ldt = now;
				int customTimeZoneIndex = 1;

				if (!args[0].equalsIgnoreCase("now")) {
					customTimeZoneIndex = 2;
		    		response = String.format(format, "Time Zone", args[0] + " " + args[1].toUpperCase()) + "\n";
		    		String currentDate = String.format("%02d", now.getDayOfMonth()) + "-" + String.format("%02d", now.getMonthValue()) + "-" + now.getYear() + " " + args[0] + ":00 " + args[1].toUpperCase();
					ldt = LocalDateTime.parse(currentDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
				} else {
					response = "```" + String.format(format, "Time Zone", "Now") + "\n";
				}

				response += String.format(format, "-------------", "----------") + "\n";
				ZonedDateTime baseZonedTime = ldt.atZone(timezones.get("Los Angeles"));
				for (Map.Entry<String,ZoneId> entry : timezones.entrySet()) {
					ZonedDateTime targetZonedDateTime = baseZonedTime.withZoneSameInstant(entry.getValue());
					response += String.format(format, entry.getKey(), DateTimeFormatter.ofPattern(OUTPUT_FORMAT).format(targetZonedDateTime)) + "\n";
				}

				if (args.length > customTimeZoneIndex) {
					ZonedDateTime extraZonedDateTime = baseZonedTime.withZoneSameInstant(ZoneId.of(args[customTimeZoneIndex]));
					response += String.format(format, args[customTimeZoneIndex], DateTimeFormatter.ofPattern(OUTPUT_FORMAT).format(extraZonedDateTime)) + "\n";
				}

				response += "```";
	    	}

		} catch (Exception e){
			System.err.println(e);
		}
        channel.sendMessage(response).queue();
    }
}
