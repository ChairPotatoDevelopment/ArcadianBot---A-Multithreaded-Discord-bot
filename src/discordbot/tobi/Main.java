package discordbot.tobi;

import net.dv8tion.jda.core.*;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.EventListener;


import javax.security.auth.login.LoginException;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;


public class Main implements EventListener{

    public static JDA jda;

    public static List<CommandProcessor> commandVendors = new ArrayList<CommandProcessor>();
    public static List<Thread> gameVendors = new ArrayList<Thread>();
    public static List<Thread> dataVendors = new ArrayList<Thread>();
    public static List<Thread> miscVendors = new ArrayList<Thread>();
    public static List<Thread> dummyVendors = new ArrayList<Thread>();


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
            System.err.println("T0B1 ran into an exception whilst attempting to log in:");
            e.printStackTrace();
        } catch(InterruptedException e) {
            System.err.println("T0B1 ran into an InterruptedException during build:");
            e.printStackTrace();
        } catch(RateLimitedException e) {
            System.err.println("T0B1 ran into an RateLimitedException during build:");
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

                /*
                Some commands are awkward to work with in the CommandProcessor. This is for
                certain commands which would fit better outside of the CommandProcessor such as
                the shutdown command. If the bot is shutting down the Command Vendors, the
                process could terminate the thread that the shutdown is occurring on.
                 */

                if (event instanceof MessageReceivedEvent) {

                    MessageReceivedEvent message = ((MessageReceivedEvent) event);

                    if (message.getMessage().getContentRaw().toLowerCase().startsWith("!shutdown")) {
                        int i = 0;
                        for (Thread thread: commandVendors){


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

                    } else if(message.getMessage().getContentRaw().toLowerCase().startsWith("!thread")){

                        String[] parameters = message.getMessage().getContentRaw().split(" ", 10);

                        switch (parameters[1]){
                            case "create":
                                switch (parameters[2]){
                                    case "dummy":
                                        DummyThread process = new DummyThread();
                                        process.start();
                                        dummyVendors.add(process);
                                        message.getTextChannel().sendMessage(new EmbedBuilder().setAuthor("System").setTitle("DummyVendor#" + Main.dummyVendors.indexOf(process)).setDescription("DummyVendor#" + Main.dummyVendors.indexOf(process) + " has been successfully created.").setColor(Color.green).build()).complete();
                                        break;
                                    case "commandVendor":
                                        CommandProcessor cProcess = new CommandProcessor(message.getMessage(), true);
                                        cProcess.start();
                                        commandVendors.add(cProcess);
                                        break;
                                }
                            case "stop":
                                break;
                            case "list":
                                for (Thread thr: dummyVendors) {
                                    message.getTextChannel().sendMessage(new EmbedBuilder().setAuthor("System").setTitle("DummyVendor#" + Main.dummyVendors.indexOf(thr)).setDescription("DummyVendor#" + Main.dummyVendors.indexOf(thr) + " is running.").setColor(Color.magenta).build()).complete();

                                }
                                break;

                        }

                    } else {

                        CommandProcessor currentProcessor = null;

                        for (CommandProcessor cmdProcessor: commandVendors) {
                            if(!CommandProcessor.inUse){
                                currentProcessor = cmdProcessor;
                            }
                        }

                        if (currentProcessor != null){
                            currentProcessor.RequestCommand(message.getMessage());
                        } else{
                            CommandProcessor process = new CommandProcessor(message.getMessage(), false);
                            process.start();
                            commandVendors.add(process);
                        }
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
