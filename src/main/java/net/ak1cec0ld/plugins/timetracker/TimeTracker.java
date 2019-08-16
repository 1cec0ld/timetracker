package net.ak1cec0ld.plugins.timetracker;

import net.ak1cec0ld.plugins.timetracker.files.StorageFile;
import net.ak1cec0ld.plugins.timetracker.listeners.JoinListener;
import net.ak1cec0ld.plugins.timetracker.listeners.QuitListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TimeTracker extends JavaPlugin {
    private static TimeTracker instance;

    public void onEnable(){
        instance = this;


        new JoinListener();
        new QuitListener();
        new TTCommand();

        new StorageFile();
    }

    public static TimeTracker instance(){
        return instance;
    }
    public static void msgActionBar(Player player, String message, ChatColor color){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(message).color(color).create());
    }
    public static void debug(String string){
        Bukkit.getLogger().info("["+instance.getName()+"-debug] " + string);
    }

}
