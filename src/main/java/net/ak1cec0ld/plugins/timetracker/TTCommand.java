package net.ak1cec0ld.plugins.timetracker;

import net.ak1cec0ld.plugins.timetracker.files.StorageFile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class TTCommand implements CommandExecutor {

    public TTCommand(){
        TimeTracker.instance().getServer().getPluginCommand("timetracker").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        switch(strings.length) {
            case 0:
                if (commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    commandSender.sendMessage(displayFromMillis(getTotal(player)));
                }
                return true;
            case 1:
                if(getPlayerFromString(strings[0]) == null){
                    commandSender.sendMessage("No player found");
                    return false;
                }
                commandSender.sendMessage(displayFromMillis(getTotal(getPlayerFromString(strings[0]))));
                return true;
            default:
                commandSender.sendMessage("It's just /timetracker or /timetracker [target]");
                return false;
        }
    }

    private static String displayFromMillis(long duration){
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        //long millis  = TimeUnit.MILLISECONDS.toMillis(duration)-TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(duration));

        return String.format("%dm %ds", minutes, seconds/*, millis*/);
    }
    private static Player getPlayerFromString(String input){
        for(Player each: Bukkit.getOnlinePlayers()){
            if(each.getName().toLowerCase().startsWith(input.toLowerCase())){
                return each;
            }
        }
        return null;
    }
    private static long getTotal(Player player){
        long total = 0L;
        for (Long each : StorageFile.getTimes(player.getUniqueId().toString())) {
            total += each;
        }
        return total;
    }
}
