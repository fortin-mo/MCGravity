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
        saveDefaultConfig();
        
        BlockListener.ac = this;
        BlockListener.LoadConfig();
        
        this.getCommand("mcgravity").setExecutor(new GravityCommand(this));
        
        this.getServer().getPluginManager().registerEvents((Listener)this.ax, (Plugin)this);
    }
}

