package lowbrain.mcgravity;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.plugin.java.JavaPlugin;

import lowbrain.mcgravity.Delay;
import lowbrain.mcgravity.Gravity;
import lowbrain.mcgravity.MainLoop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockListener
implements Listener {
	public static JavaPlugin ac = null;
	public static boolean useFixedStrength = false;
	public static boolean useSquareRadius = false;
	public static double strengthRadius = 5.0;
	public static int maxTimeToDoJob = 1000;
	public static int checkingDelayAsTick = 1;
	public static boolean allowDiagonal = false;
	public static int foundation = 16;
	public static double strengthMultiplier = 0.25;
	public static boolean useStrengthMultiplier = true;
	public static List<String> whitelist = new ArrayList<>();
	public static List<String> blacklist = new ArrayList<>();
    public static HashMap<String,Double> blockStrength = new HashMap<String, Double>();

	public BlockListener() {
		Delay delay = new Delay();
		Thread th = new Thread(delay);
		th.start();
	}

	private boolean isWhitelisted(String n){
        for (String reg :
                whitelist) {
            if (n.matches(reg)){
                return true;
            }
        }
        return false;
    }

    private boolean isBlacklisted(String n){
        for (String reg :
                blacklist) {
            if (n.matches(reg)){
                return true;
            }
        }
        return false;
    }

    private boolean worldIsEnabled(String n){
        if (whitelist.isEmpty() && blacklist.isEmpty()){
            return true;
        }
        if (!whitelist.isEmpty() && isWhitelisted(n)){
            return true;
        }
        if (!blacklist.isEmpty() && isBlacklisted(n)){
            return false;
        }
        return true;
    }


	@EventHandler
	public void eventja(BlockBreakEvent e) {
        BlockEvent(e.getBlock());
	}

	@EventHandler
	public void eventja(BlockExplodeEvent e) {
        BlockEvent(e.getBlock());
	}

	@EventHandler
	public void eventja(BlockPlaceEvent e) {
        BlockEvent(e.getBlock());
	}

	@EventHandler
	public void eventja(BlockBurnEvent e){
        BlockEvent(e.getBlock());
    }

    @EventHandler
    public void evenja(LeavesDecayEvent e){
        BlockEvent(e.getBlock());
    }

	private void BlockEvent(Block block){
        if (block == null || !worldIsEnabled(block.getWorld().getName())){
            return;
        }
        Block b2 = null;
        int r = Gravity.r;
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    if(!BlockListener.allowDiagonal && Math.abs(x) + Math.abs(y) + Math.abs(z) > 1){
                        continue;
                    }
                    b2 = block.getRelative(x,y,z);
                    if (!Helper.needBlock(b2)) {
                        continue;
                    }
                    MainLoop.jobs.put(b2.getLocation());
                }
            }
        }
    }

	public static void LoadConfig(){
		if(BlockListener.ac != null){
			BlockListener.useFixedStrength = BlockListener.ac.getConfig().getBoolean("USE_FIXED_STRENGTH");
			BlockListener.ac.getLogger().info("useFixedStrengh = " + BlockListener.useFixedStrength);
	        BlockListener.useSquareRadius = BlockListener.ac.getConfig().getBoolean("USE_SQUARE_RADIUS");
	        BlockListener.ac.getLogger().info("useSquareRadius = " + BlockListener.useSquareRadius);
	        BlockListener.maxTimeToDoJob = BlockListener.ac.getConfig().getInt("MAXTIME_DO_THE_JOB");
	        BlockListener.ac.getLogger().info("maxTimeToDoJob = " + BlockListener.maxTimeToDoJob);
	        BlockListener.strengthRadius = BlockListener.ac.getConfig().getDouble("STRENGTH_RADIUS");
	        BlockListener.ac.getLogger().info("strengthRadius = " + BlockListener.strengthRadius);
	        BlockListener.checkingDelayAsTick = BlockListener.ac.getConfig().getInt("REPEAT_CHECKING_DELAY_AS_TICK");
	        BlockListener.ac.getLogger().info("checkingDelayAsTick = " + BlockListener.checkingDelayAsTick);
	        BlockListener.allowDiagonal = BlockListener.ac.getConfig().getBoolean("ALLOW_DIAGONAL");
	        BlockListener.ac.getLogger().info("allowDiagonal = " + BlockListener.allowDiagonal); 
	        BlockListener.useStrengthMultiplier = BlockListener.ac.getConfig().getBoolean("USE_STRENGTH_MULTIPLIER");
	        BlockListener.ac.getLogger().info("useStrengthMultiplier = " + BlockListener.useStrengthMultiplier);
	        BlockListener.strengthMultiplier = BlockListener.ac.getConfig().getDouble("STRENGTH_MULTIPLIER");
	        BlockListener.ac.getLogger().info("strengthMultiplier = " + BlockListener.strengthMultiplier);
			BlockListener.whitelist = BlockListener.ac.getConfig().getStringList("WHITELIST_WORKD");
			BlockListener.blacklist = BlockListener.ac.getConfig().getStringList("BLACKLIST_WORLD");
            blockStrength = new HashMap<String, Double>();
            ConfigurationSection blockSec = BlockListener.ac.getConfig().getConfigurationSection("BLOCKS_STRENGTH");
            if (blockSec != null) {
                for (String block :
                        blockSec.getKeys(false)) {
                    blockStrength.put(block, blockSec.getDouble(block, BlockListener.strengthRadius));
                }
            }
		}
	}
}

