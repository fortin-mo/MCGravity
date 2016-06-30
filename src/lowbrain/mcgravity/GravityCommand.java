package lowbrain.mcgravity;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Command executor for MCGravity plugin
 * @author lowbrain
 *
 */
public class GravityCommand implements CommandExecutor{

	private final lowbrain.mcgravity.Main plugin;
	
	public GravityCommand(lowbrain.mcgravity.Main plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Called when the plugin receice a command
	 */
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	sender.sendMessage("executing mcgravity reload");
    	if (cmd.getName().equalsIgnoreCase("mcgravity")) { 
    		if(args.length > 0 && args[0].equalsIgnoreCase("reload")){
    			Bukkit.getScheduler().cancelTasks(BlockListener.ac);
    			
    			//BlockListener ax = new BlockListener();
    			
    			BlockListener.ac = plugin;
    			BlockListener.ac.reloadConfig();
    			BlockListener.LoadConfig();
    			
    			Delay delay = new Delay();
    			Thread th = new Thread(delay);
    			th.start();
    			
    	        //plugin.getServer().getPluginManager().registerEvents((Listener)ax, plugin);
				
    			sender.sendMessage("MCGravity reloaded !!!");
    			return true;
    		}
    	} 
    	return false;
    }

}
