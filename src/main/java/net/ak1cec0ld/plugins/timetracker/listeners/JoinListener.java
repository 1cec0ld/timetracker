package net.ak1cec0ld.plugins.timetracker.listeners;

import net.ak1cec0ld.plugins.timetracker.TimeTracker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class JoinListener implements Listener {

    public JoinListener(){
        TimeTracker.instance().getServer().getPluginManager().registerEvents(this,TimeTracker.instance());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(event.getPlayer().hasPermission("timetracker.track")){
            event.getPlayer().setMetadata("timetracker", new FixedMetadataValue(TimeTracker.instance(), System.currentTimeMillis()));
        }
    }
}
