package lowbrain.mcgravity;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import lowbrain.mcgravity.Delay;
import lowbrain.mcgravity.Gravity;
import lowbrain.mcgravity.MainLoop;

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

	public BlockListener() {
		Delay delay = new Delay();
		Thread th = new Thread(delay);
		th.start();
	}

	@EventHandler
	public void eventja(BlockBreakEvent e) {

		Block block = e.getBlock();
		Block b2 = null;

		int r = Gravity.r;
		for (int x = -r; x <= r; x++) {
			for (int y = -r; y <= r; y++) {
				for (int z = -r; z <= r; z++) {
					
					if(x == 0 && y == 0 && z == 0){
						continue;
					}
					
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

	@EventHandler
	public void eventja(BlockExplodeEvent e) {

		Block block = e.getBlock();
		Block b2 = null;

		int r = Gravity.r;

		for (int x = -r; x <= r; x++) {
			for (int y = -r; y <= r; y++) {
				for (int z = -r; z <= r; z++) {

					if(x == 0 && y == 0 && z == 0){
						continue;
					}
					
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

	@EventHandler
	public void eventja(BlockPlaceEvent e) {

		Block block = e.getBlock();
		Block b2 = null;

		int r = Gravity.r;
		for (int x = -r; x <= r; x++) {
			for (int y = -r; y <= r; y++) {
				for (int z = -r; z <= r; z++) {

					if(x == 0 && y == 0 && z == 0){
						continue;
					}
					
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
			BlockListener.useFixedStrength = BlockListener.ac.getConfig().getBoolean("CONFIG_GRAVITY_USE_FIXED_STRENGTH");
			BlockListener.ac.getLogger().info("useFixedStrengh = " + BlockListener.useFixedStrength);
	        BlockListener.useSquareRadius = BlockListener.ac.getConfig().getBoolean("CONFIG_GRAVITY_USE_SQUARE_RADIUS");
	        BlockListener.ac.getLogger().info("useSquareRadius = " + BlockListener.useSquareRadius);
	        BlockListener.maxTimeToDoJob = BlockListener.ac.getConfig().getInt("CONFIG_GRAVITY_MAXTIME_DO_THE_JOB");
	        BlockListener.ac.getLogger().info("maxTimeToDoJob = " + BlockListener.maxTimeToDoJob);
	        BlockListener.strengthRadius = BlockListener.ac.getConfig().getDouble("CONFIG_GRAVITY_STRENGTH_RADIUS");
	        BlockListener.ac.getLogger().info("strengthRadius = " + BlockListener.strengthRadius);
	        BlockListener.checkingDelayAsTick = BlockListener.ac.getConfig().getInt("CONFIG_GRAVITY_REPEAT_CHECKING_DELAY_AS_TICK");
	        BlockListener.ac.getLogger().info("checkingDelayAsTick = " + BlockListener.checkingDelayAsTick);
	        BlockListener.allowDiagonal = BlockListener.ac.getConfig().getBoolean("CONFIG_GRAVITY_ALLOW_DIAGONAL");
	        BlockListener.ac.getLogger().info("allowDiagonal = " + BlockListener.allowDiagonal);
		}
	}

}

