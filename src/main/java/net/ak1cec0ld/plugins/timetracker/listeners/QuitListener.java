package net.ak1cec0ld.plugins.timetracker.listeners;

import net.ak1cec0ld.plugins.timetracker.TimeTracker;
import net.ak1cec0ld.plugins.timetracker.files.StorageFile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    public QuitListener(){
        TimeTracker.instance().getServer().getPluginManager().registerEvents(this, TimeTracker.instance());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        if(event.getPlayer().hasMetadata("timetracker")){
            long loginTime = event.getPlayer().getMetadata("timetracker").get(0).asLong();
            StorageFile.setTime(event.getPlayer().getUniqueId().toString(), System.currentTimeMillis()-loginTime);
            TimeTracker.debug("posted settime");
        }
    }
}
