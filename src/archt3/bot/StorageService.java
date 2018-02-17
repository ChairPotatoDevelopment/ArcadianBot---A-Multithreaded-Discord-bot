package archt3.bot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

public class StorageService extends Thread{

    public boolean ForcedActive;
    public boolean inUse;
    private boolean isRequested;

    private Object result;

    private String requestedLocation;
    private String DataMode;
    private String currentDatabaseID;

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
                        System.out.println("StorageService#" + Main.DataServices.indexOf(this) + " ERROR");
                        return;
                    }
                    tryCount++;
                } else {
                    this.interrupt();
                    return;
                }

            }

            getStorage();

        }
    }

    private void getStorage(){

        String[] locSplit = requestedLocation.split("/");


        if(currentDatabaseID.startsWith("server")) {
            String serverID = currentDatabaseID.substring(5);

            Object fetchedData;
            JsonObject previousLevel;

            for (String cLevel : locSplit) {

            }
        }

        if(currentDatabaseID.startsWith("#botdata")) {
            Object fetchedData;
            JsonObject previousLevel;

            for (String cLevel : locSplit) {

            }
        }

        /*

        User data will be used for general bot stuff such as "Hound Cross-Server Banning"
        or "User Profiles" and not for things like "Local Mutes" and per-server data. That
        data will be stored in server Databases.

        For now, User Data is staying unused.

        if(currentDatabaseID.startsWith("#user")) {
            Object fetchedData;
            JsonObject previousLevel;

            for (String cLevel : locSplit) {

            }
        }*/
    }

    public Object RequestStorage(String databaseID, String location, String setOrAccess){
        if(!inUse) {

            isRequested = true;
            requestedLocation = location;
            DataMode = setOrAccess;
            result = null;
        }

        while(result == null){
            try {
                synchronized (this) {
                    this.wait(1000);
                }
            } catch (InterruptedException err) {
                System.out.println("StorageService#" + Main.DataServices.indexOf(this) + " ERROR");
                return err;
            }
        }

        return result;
    }

}
