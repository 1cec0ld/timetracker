package net.ak1cec0ld.plugins.timetracker.files;

import net.ak1cec0ld.plugins.timetracker.TimeTracker;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class StorageFile {

    private static CustomYMLStorage yml;
    private static YamlConfiguration storage;
    private static LocalDate now;
    private static final DateFormat KEY_FORMAT = new SimpleDateFormat("yyyy-mm-dd");

    public StorageFile(){
        yml = new CustomYMLStorage(TimeTracker.instance(),"storage.yml","TimeTracker");
        storage = yml.getYamlConfiguration();
        yml.save();

        cleanFile();
    }

    public static void setTime(String uuid, long value){
        long oldValue = 0L;
        now = LocalDate.now();
        String date = now.toString();
        LocalDate given;
        ConfigurationSection player = storage.getConfigurationSection(uuid);
        if(player != null) {
            for (String eachDay : player.getKeys(false)) {
                given = LocalDate.parse(eachDay);
                Period p = Period.between(now, given);

                if (p.getDays() == 0) {
                    date = eachDay;
                    oldValue = storage.getLong(uuid + "." + eachDay);
                }
            }
        }
        storage.set(uuid+"."+date, oldValue+value);
        yml.save();
    }

    public static List<Long> getTimes(String uuid){
        List<Long> times = new ArrayList<>();
        ConfigurationSection player = storage.getConfigurationSection(uuid);
        if(player == null)return times;
        for(String eachDay : player.getKeys(false)){
            times.add(storage.getLong(uuid+"."+eachDay,0));
        }
        return times;
    }

    private static void cleanFile(){
        now = LocalDate.now();
        LocalDate given;
        for(String eachUuid : storage.getKeys(false)){
            for(String eachDay : storage.getConfigurationSection(eachUuid).getKeys(false)){
                given = LocalDate.parse(eachDay);
                Period p = Period.between(given,now);
                if(p.getDays() >= 7){
                    storage.set(eachUuid+"."+eachDay,null);
                }
            }
        }
        yml.save();
    }
}
