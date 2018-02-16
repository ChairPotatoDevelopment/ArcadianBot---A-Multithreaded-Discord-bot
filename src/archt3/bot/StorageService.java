package archt3.bot;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

public class StorageService extends Thread{

    public boolean ForcedActive;
    public boolean inUse;
    public boolean isRequested;

    private Message message;

    public StorageService(boolean isForcedActive) {
        ForcedActive = isForcedActive;
    }

    @Override
    public void run() {
        isRequested = false;
        if(ForcedActive){
            System.out.println("Unused #" + Main.DataServices.indexOf(this));
            inUse = false;
            return;
        } else{
            System.out.println("Unused #" + Main.DataServices.indexOf(this));
            inUse = false;

            int tryCount = 0;

            while (!isRequested) {
                if (tryCount < 30) {
                    try {
                        synchronized (this) {
                            this.wait(1000);
                        }
                    } catch (InterruptedException err) {
                        message.getTextChannel().sendMessage(new EmbedBuilder().setAuthor("System").setTitle("CommandVendor#" + Main.DataServices.indexOf(this) + " ERROR").setDescription("StorageService#" + Main.DataServices.indexOf(this) + " reached an exception and must be forcefully shutdown. All data and activities on this thread shall be terminated.").setColor(Color.red).build()).complete();
                        return;
                    }
                    tryCount++;
                } else {
                    this.interrupt();
                    return;
                }

            }

            //processCommand(message);

        }
    }

    public void RequestCommand(Message msg){
        if(!inUse) {
            isRequested = true;
            message = msg;
        }

    }

}
