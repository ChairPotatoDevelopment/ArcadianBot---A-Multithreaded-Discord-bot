package discordbot.tobi;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandProcessor extends Thread{

    public static boolean inUse;
    public static boolean isRequested;
    public static boolean ForcedActive;

    public static Message message;

    public CommandProcessor(Message msg, boolean isForcedActive) {
        message = msg;
        ForcedActive = isForcedActive;
    }

    @Override
    public void run() {

        processCommand(message);

    }

    public static boolean RequestCommand(Message msg){
        if(!inUse) {
            isRequested = true;
            message = msg;
            return true;
        } else{
            return false;
        }

    }

    public void processCommand(Message msg){
        inUse = true;
        message = msg;
        System.out.println("In Use #" + Main.commandVendors.indexOf(this));

        if (message.getContentRaw().toLowerCase().startsWith("!ping")) {


            EmbedBuilder ebuilder = new EmbedBuilder().setTitle("SYSTEM -").setDescription("PONG").setFooter("CommandVendor#" + Main.commandVendors.indexOf(this),"https://imgur.com/3RkJ0db.png").setColor(java.awt.Color.green);
            MessageEmbed ebuilt = ebuilder.build();

            message.getTextChannel().sendMessage(ebuilt).queue();

        } else if(message.getContentRaw().toLowerCase().startsWith("!o-thread")) {
            message.getTextChannel().sendMessage(new EmbedBuilder().setTitle("SYSTEM -").setDescription("Occupying thread until shutdown.").setFooter("CommandVendor#" + Main.commandVendors.indexOf(this),"https://imgur.com/3RkJ0db.png").setColor(java.awt.Color.green).build()).queue();
            while (true) {
                try {
                    synchronized (this) {
                        this.wait(100);
                    }
                } catch (InterruptedException err) {
                    message.getTextChannel().sendMessage(new EmbedBuilder().setAuthor("System").setTitle("CommandVendor#" + Main.commandVendors.indexOf(this) + " ERROR").setDescription("CommandVendor#" + Main.commandVendors.indexOf(this) + " reached an exception and must be forcefully shutdown. All data and activities on this thread shall be terminated.").setColor(Color.red).build()).complete();
                    return;
                }
            }
        }else {


        }



        isRequested = false;
        if(ForcedActive){
            System.out.println("Unused #" + Main.commandVendors.indexOf(this));
            inUse = false;
            return;
        } else{
            System.out.println("Unused #" + Main.commandVendors.indexOf(this));
            inUse = false;

            int tryCount = 0;

            while (!isRequested) {
                if (tryCount < 5) {
                    try {
                        synchronized (this) {
                            this.wait(1000);
                        }
                    } catch (InterruptedException err) {
                        message.getTextChannel().sendMessage(new EmbedBuilder().setAuthor("System").setTitle("CommandVendor#" + Main.commandVendors.indexOf(this) + " ERROR").setDescription("CommandVendor#" + Main.commandVendors.indexOf(this) + " reached an exception and must be forcefully shutdown. All data and activities on this thread shall be terminated.").setColor(Color.red).build()).complete();
                        return;
                    }
                    tryCount++;
                } else {
                    this.interrupt();
                    return;
                }

            }

            processCommand(message);

        }
    }
}
