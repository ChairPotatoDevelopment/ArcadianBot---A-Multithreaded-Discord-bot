package discordbot.tobi;

import net.dv8tion.jda.core.*;

import net.dv8tion.jda.core.entities.*;
//import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


import javax.security.auth.login.LoginException;

import java.awt.*;
//import java.io.*;
import java.util.*;
import java.util.List;


public class Main extends ListenerAdapter{

    private static JDA jda;

    public static List<CommandProcessor> commandVendors = new ArrayList<>();
    //public static List<Thread> gameVendors = new ArrayList<>();
    //public static List<Thread> dataVendors = new ArrayList<>();
    //public static List<Thread> miscVendors = new ArrayList<>();
    private static List<Thread> dummyVendors = new ArrayList<>();


    public static void main(String[] args){

        JDABuilder botBuilder = new JDABuilder(AccountType.BOT);
        botBuilder.setToken("MzkxNjI4MTk3MjM5NzE3ODg4.DS-vVA.ns0Syg8wYWliGhwj1sQRd4X6aus");
        botBuilder.setAutoReconnect(true);
        botBuilder.setGame(Game.playing("Architecture-T3"));
        botBuilder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        botBuilder.addEventListener(new Main());


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

            //Icon ico = Icon.from(new File("D:/boticon.png"));
            //jda.getSelfUser().getManager().setAvatar(ico).queue();
            jda.getSelfUser().getManager().setName("Arcadian").queue();


    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent message) {
        if (message.getAuthor().isBot()) return;
        System.out.println("[MAIN]Message!");
        System.out.println("[MAIN]<PROCESSING> Message-" + message.getAuthor().getName() + "-" + message.getMessage().getTextChannel().getName() + "-" + message.getMessage().getContentRaw());

                /*
                Some commands are awkward to work with in the CommandProcessor. This is for
                certain commands which would fit better outside of the CommandProcessor such as
                the shutdown command. If the bot is shutting down the Command Vendors, the
                process could terminate the thread that the shutdown is occurring on.
                 */
        if (message.getMessage().getContentRaw().toLowerCase().startsWith("!")) {
            System.out.println("[MAIN]Is Command!");
            System.out.println("[MAIN]<APPROVED> Message-" + message.getAuthor().getName() + "-" + message.getMessage().getTextChannel().getName() + "-" + message.getMessage().getContentRaw());
            if (message.getMessage().getContentRaw().toLowerCase().startsWith("!shutdown")) {
                int i = 0;
                for (Thread thread : commandVendors) {


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

            } else if (message.getMessage().getContentRaw().toLowerCase().startsWith("!thread")) {
                System.out.println("Is thread Command!!");
                String[] parameters = message.getMessage().getContentRaw().split(" ", 10);

                switch (parameters[1]) {
                    case "create":
                        switch (parameters[2]) {
                            case "dummy":
                                DummyThread process = new DummyThread();
                                process.start();
                                dummyVendors.add(process);
                                message.getMessage().getTextChannel().sendMessage(new EmbedBuilder().setTitle("DummyVendor#" + Main.dummyVendors.indexOf(process)).setDescription("DummyVendor#" + Main.dummyVendors.indexOf(process) + " has been successfully created.").setColor(Color.green).build()).complete();

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
                        for (Thread thr : dummyVendors) {
                            message.getMessage().getTextChannel().sendMessage(new EmbedBuilder().setAuthor("System").setTitle("DummyVendor#" + Main.dummyVendors.indexOf(thr)).setDescription("DummyVendor#" + Main.dummyVendors.indexOf(thr) + " is running.").setColor(Color.magenta).build()).complete();

                        }
                        for (CommandProcessor thr : commandVendors) {
                            String active = null;
                            if (thr.inUse) {
                                active = "True";
                            } else {
                                active = "False";
                            }
                            message.getMessage().getTextChannel().sendMessage(new EmbedBuilder().setAuthor("System").setTitle("CommandVendor#" + Main.commandVendors.indexOf(thr)).setDescription("CommandVendor#" + Main.commandVendors.indexOf(thr) + " is running.").addField("Is Active?", active, true).setColor(Color.magenta).build()).complete();

                        }
                        break;

                }

            } else {

                CommandProcessor currentProcessor = null;

                for (CommandProcessor cmdProcessor : commandVendors) {
                    if (!cmdProcessor.inUse) {
                        currentProcessor = cmdProcessor;
                    }
                }

                if (currentProcessor != null) {
                    System.out.println("[MAIN]Old Thread #" + Main.commandVendors.indexOf(currentProcessor));
                    currentProcessor.RequestCommand(message.getMessage());
                } else {

                    CommandProcessor process = new CommandProcessor(message.getMessage(), false);
                    process.start();
                    commandVendors.add(process);
                    System.out.println("[MAIN]Fresh thread #" + Main.commandVendors.indexOf(process));
                }
            }


        }

    }
}
