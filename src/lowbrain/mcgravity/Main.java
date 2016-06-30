package lowbrain.mcgravity;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import lowbrain.mcgravity.BlockListener;

/**
 * Main plugin class
 * @author lowbrain
 *
 */
public class Main
extends JavaPlugin {
    Logger log; 
    BlockListener ax = new BlockListener();

    /**
     * called when the plugin is being disabled
     */
    @Override
    public void onDisable() {
        this.getServer().getPluginManager().disablePlugin((Plugin)this);
        
    }
    
    /**
     * Called when the plugin is first enabled
     */
    @Override
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
                config.addDefault("CONFIG_GRAVITY_ALLOW_DIAGONAL", (Object)true);
                config.options().copyDefaults(true);
                this.saveConfig();
            } else {
                this.getLogger().info("Config.yml found, loading saved data!"); 
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        BlockListener.ac = this;
        BlockListener.LoadConfig();
        
        this.getCommand("mcgravity").setExecutor(new GravityCommand(this));
        
        this.getServer().getPluginManager().registerEvents((Listener)this.ax, (Plugin)this);
    }
}

