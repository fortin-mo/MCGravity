package dewddgravity;

import dewddgravity.DigEventListener2;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
extends JavaPlugin {
    Logger log; 
    DigEventListener2 ax = new DigEventListener2();

    public void onDisable() {
        this.getServer().getPluginManager().disablePlugin((Plugin)this);
        
    }
    
    public void onEnable() {
       
        this.getLogger().info("Loading MCGravity.jar");
        try {
            File file;
            if (!this.getDataFolder().exists()) {
                this.getDataFolder().mkdirs();
            }
            if (!(file = new File(this.getDataFolder(), "config.yml")).exists()) {
                this.getLogger().info("Config.yml not found! Creating new one ...");
                FileConfiguration config = this.getConfig();
                config.addDefault("CONFIG_GRAVITY_MAXTIME_DO_THE_JOB", (Object)1000);
                config.addDefault("CONFIG_GRAVITY_REPEAT_CHECKING_DELAY_AS_TICK", (Object)1);
                config.addDefault("CONFIG_GRAVITY_STRENGTH_RADIUS", (Object)5);
                config.addDefault("CONFIG_GRAVITY_USE_FIXED_STRENGTH", (Object)false);
                config.addDefault("CONFIG_GRAVITY_USE_SQUARE_RADIUS", (Object)false);
                config.options().copyDefaults(true);
                this.saveConfig();
            } else {
                this.getLogger().info("Config.yml found, loading saved data!"); 
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        DigEventListener2.ac = this;
        DigEventListener2.useFixedStrength = this.getConfig().getBoolean("CONFIG_GRAVITY_USE_FIXED_STRENGTH");
        DigEventListener2.useSquareRadius = this.getConfig().getBoolean("CONFIG_GRAVITY_USE_SQUARE_RADIUS");
        DigEventListener2.maxTimeToDoJob = this.getConfig().getInt("CONFIG_GRAVITY_MAXTIME_DO_THE_JOB");
        DigEventListener2.strengthRadius = this.getConfig().getDouble("CONFIG_GRAVITY_STRENGTH_RADIUS");
        DigEventListener2.checkingDelayAsTick = this.getConfig().getInt("CONFIG_GRAVITY_REPEAT_CHECKING_DELAY_AS_TICK");
        
        this.getServer().getPluginManager().registerEvents((Listener)this.ax, (Plugin)this);
    }
}

