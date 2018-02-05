package discordbot.tobi;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandProcesser extends Thread{




    public static JDA jda;
    public static List<String> HoundList = new ArrayList<String>();
    public static Map<String, String> verificationList = new HashMap<String, String>();
    public static List<String> launchStatus = new ArrayList<String>();
    public static List<String> logs = new ArrayList<String>();

    private static MessageReceivedEvent message;

    public CommandProcesser(MessageReceivedEvent msg) {
        message = msg;
    }

    @Override
    public void run() {

        if (message.getMessage().getContentRaw().toLowerCase().startsWith("!ping")) {


            EmbedBuilder ebuilder = new EmbedBuilder().setTitle("SYSTEM -").setDescription("PONG").setColor(java.awt.Color.green);
            MessageEmbed ebuilt = ebuilder.build();

            message.getMessage().getTextChannel().sendMessage(ebuilt).queue();

        } else if(message.getMessage().getContentRaw().toLowerCase().startsWith("!event")) {

        }else {


        }
    }
}
