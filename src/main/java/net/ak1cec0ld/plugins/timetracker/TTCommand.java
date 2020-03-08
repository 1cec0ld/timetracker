package net.ak1cec0ld.plugins.timetracker;

import com.sk89q.squirrelid.Profile;
import com.sk89q.squirrelid.resolver.HttpRepositoryService;
import com.sk89q.squirrelid.resolver.ProfileService;
import net.ak1cec0ld.plugins.timetracker.files.StorageFile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;
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
                if(getPlayerFromString(strings[0]) != null){
                    commandSender.sendMessage(strings[0] + ": " + displayFromMillis(getTotal(getPlayerFromString(strings[0]))));
                    return true;
                }
                if(getOfflinePlayerFromString(strings[0]) != null){
                    commandSender.sendMessage(strings[0] + ": " + displayFromMillis(getTotal(getOfflinePlayerFromString(strings[0]))));
                    return true;
                }
                commandSender.sendMessage("No player found");
                return false;
            default:
                commandSender.sendMessage("It's just /timetracker or /timetracker [target]");
                return false;
        }
    }

    private static String displayFromMillis(long duration){
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)-TimeUnit.MINUTES.toMinutes(hours);
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
    private static long getTotal(OfflinePlayer player){
        long total = 0L;
        for (Long each : StorageFile.getTimes(player.getUniqueId().toString())) {
            total += each;
        }
        return total;
    }


    public static OfflinePlayer getOfflinePlayerFromString(String string) {
        ProfileService resolver = HttpRepositoryService.forMinecraft();
        try {
            Profile profile = resolver.findByName(string);
            if(profile !=null){
                UUID uuid = profile.getUniqueId();
                return Bukkit.getOfflinePlayer(uuid);
            }
        } catch (IOException | InterruptedException e) {
            return null;
        }
        return null;
    }
}
