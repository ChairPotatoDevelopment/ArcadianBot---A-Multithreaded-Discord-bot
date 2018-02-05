package discordbot.tobi;

import com.google.gson.Gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.core.*;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.managers.GuildController;
import org.json.JSONObject;


import javax.security.auth.login.LoginException;
import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;


public class Main implements EventListener{

    public static JDA jda;

    public static List<Thread> threadList = new ArrayList<Thread>();


    public static void main(String[] args){

        JDABuilder botBuilder = new JDABuilder(AccountType.BOT);
        botBuilder.setToken("MzkxNjI4MTk3MjM5NzE3ODg4.DS-vVA.ns0Syg8wYWliGhwj1sQRd4X6aus");
        botBuilder.setAutoReconnect(true);
        botBuilder.setGame(Game.playing("...?"));
        botBuilder.setStatus(OnlineStatus.DO_NOT_DISTURB);


        try {
            System.out.println("Attempting to Load Bot");
            jda = botBuilder.buildBlocking();
            System.out.println("Loaded bot.");

        } catch(LoginException e) {
            System.err.println("T0B1 ran into an exception during build:");
            e.printStackTrace();
        } catch(InterruptedException e) {
            System.err.println("T0B1 ran into an exception during build:");
            e.printStackTrace();
        } catch(RateLimitedException e) {
            System.err.println("T0B1 ran into an exception during build:");
            e.printStackTrace();
        }

        /*
        Bot Configuration Code for editing the bot's profile.
        */

        try{
            Icon ico = Icon.from(new File("D:/boticon.png"));
            jda.getSelfUser().getManager().setAvatar(ico).queue();
            jda.getSelfUser().getManager().setName("Arcadian");

        } catch(IOException err){

        }




        EventListener elisten = new EventListener() {

            @Override
            public void onEvent(Event event) {

                if (event instanceof MessageReceivedEvent) {

                    MessageReceivedEvent message = ((MessageReceivedEvent) event);

                    if (message.getMessage().getContentRaw().startsWith("!shutdown")) {
                        int i = 0;
                        for (Thread thread: threadList){


                            EmbedBuilder ebuilder = new EmbedBuilder().setTitle("SYSTEM -").setDescription("Terminating CommandServiceBranch#" + i).setColor(java.awt.Color.red);
                            MessageEmbed ebuilt = ebuilder.build();

                            message.getMessage().getTextChannel().sendMessage(ebuilt).queue();
                            thread.interrupt();
                            i++;
                        }

                        EmbedBuilder ebuilder = new EmbedBuilder().setTitle("SYSTEM -").setDescription("Terminating CoreServiceVendor#0").setColor(java.awt.Color.red);
                        MessageEmbed ebuilt = ebuilder.build();

                        message.getMessage().getTextChannel().sendMessage(ebuilt).complete();

                        System.exit(0);

                    } else {

                        CommandProcesser process = new CommandProcesser(message);
                        process.start();
                        threadList.add(process);
                    }


                }
            }
        };

        jda.addEventListener(elisten);


    }

    @Override
    public void onEvent(Event event) {

    }
}
