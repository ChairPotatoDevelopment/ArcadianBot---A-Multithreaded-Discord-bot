package archt3.bot;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;

public class CommandProcessor extends Thread{

    public boolean inUse;
    private boolean isRequested;
    private boolean ForcedActive;

    private Message message;

    public CommandProcessor(Message msg, boolean isForcedActive) {
        message = msg;
        ForcedActive = isForcedActive;
    }

    @Override
    public void run() {

        processCommand(message);

    }

    public void RequestCommand(Message msg){
        if(!inUse) {
            isRequested = true;
            message = msg;
        }

    }

    private void processCommand(Message msg){
        inUse = true;
        message = msg;
        System.out.println("[CMD-P]In Use #" + Main.commandVendors.indexOf(this));

        if (message.getContentRaw().toLowerCase().startsWith("!ping")) {


            EmbedBuilder ebuilder = new EmbedBuilder().setTitle("SYSTEM -").setDescription("PONG").setFooter("CommandVendor#" + Main.commandVendors.indexOf(this),"https://imgur.com/3RkJ0db.png").setColor(java.awt.Color.green);
            MessageEmbed ebuilt = ebuilder.build();

            message.getTextChannel().sendMessage(ebuilt).queue();

        } else if(message.getContentRaw().toLowerCase().startsWith("!o-thread")) {
            message.getTextChannel().sendMessage(new EmbedBuilder().setTitle("SYSTEM -").setDescription("Occupying thread until shutdown.").setFooter("CommandVendor#" + Main.commandVendors.indexOf(this),"https://imgur.com/3RkJ0db.png").setColor(java.awt.Color.green).build()).queue();
            while (true) {
                try {
                    synchronized (this) {
                        this.wait(1000);
                    }
                } catch (InterruptedException err) {
                    message.getTextChannel().sendMessage(new EmbedBuilder().setAuthor("System").setTitle("CommandVendor#" + Main.commandVendors.indexOf(this) + " ERROR").setDescription("CommandVendor#" + Main.commandVendors.indexOf(this) + " reached an exception and must be forcefully shutdown. All data and activities on this thread shall be terminated.").setColor(Color.red).build()).complete();
                    return;
                }
            }
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
                if (tryCount < 30) {
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
