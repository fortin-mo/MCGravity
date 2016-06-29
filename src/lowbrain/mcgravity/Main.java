package lowbrain.mcgravity;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import lowbrain.mcgravity.BlockListener;

public class Main
extends JavaPlugin {
    Logger log; 
    BlockListener ax = new BlockListener();

    @Override
    public void onDisable() {
        this.getServer().getPluginManager().disablePlugin((Plugin)this);
        
    }
    
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
        
        this.getServer().getPluginManager().registerEvents((Listener)this.ax, (Plugin)this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (cmd.getName().equalsIgnoreCase("mcgravity")) { 
    		if(args.length > 0 && args[0].equalsIgnoreCase("reload")){
    			Bukkit.getScheduler().cancelTasks(BlockListener.ac);
				
    			BlockListener.LoadConfig();
				Delay delay = new Delay();
				Thread th = new Thread(delay);
				th.start();
				
    			sender.sendMessage("MCGravity reloaded !!!");
    			return true;
    		}
    	} 
    	return false;
    }
}

