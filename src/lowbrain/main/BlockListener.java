package lowbrain.main;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import lowbrain.main.Delay;
import lowbrain.main.Gravity;
import lowbrain.main.MainLoop;

public class BlockListener
implements Listener {
	public static JavaPlugin ac = null;
	public static boolean useFixedStrength = false;
	public static boolean useSquareRadius = false;
	public static double strengthRadius = 5.0;
	public static int maxTimeToDoJob = 1000;
	public static int checkingDelayAsTick = 1;
	public static boolean allowDiagonal = false;

	public BlockListener() {
		Delay delay = new Delay();
		Thread th = new Thread(delay);
		th.start();
	}

	/*
	@EventHandler
	public void eventja(PlayerCommandPreprocessEvent e) {
		String m[] = e.getMessage().split(" ");
		
		if (m[0].equalsIgnoreCase("/gravity")) {
			if (m.length == 1) {
				e.getPlayer().sendMessage("/gravity reload");

			} else if (m.length == 2) {
				if (m[1].equalsIgnoreCase("reload")) {
					
					Bukkit.getScheduler().cancelTasks(ac);
					EventListener.LoadConfig();
					Delay delay = new Delay();
					Thread th = new Thread(delay);
					th.start();
					e.getPlayer().sendMessage("MCGravity reloaded!!!");
				}
			}
		}
	}
	*/

	@EventHandler
	public void eventja(BlockBreakEvent e) {

		Block block = e.getBlock();
		Block b2 = null;

		int r = Gravity.r;
		for (int x = -r; x <= r; x++) {
			for (int y = -r; y <= r; y++) {
				for (int z = -r; z <= r; z++) {
					
					if(!BlockListener.allowDiagonal && Math.abs(x) + Math.abs(y) + Math.abs(z) > 1){
						continue;
					}
					
					b2 = block.getWorld().getBlockAt(block.getX() + x, block.getY() + y, block.getZ() + z);

					if (Gravity.needBlock(b2) == false) {
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

					if(!BlockListener.allowDiagonal && Math.abs(x) + Math.abs(y) + Math.abs(z) > 1){
						continue;
					}
					
					b2 = block.getWorld().getBlockAt(block.getX() + x, block.getY() + y, block.getZ() + z);

					if (Gravity.needBlock(b2) == false) {
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

					if(!BlockListener.allowDiagonal && Math.abs(x) + Math.abs(y) + Math.abs(z) > 1){
						continue;
					}

					b2 = block.getWorld().getBlockAt(block.getX() + x, block.getY() + y, block.getZ() + z);

					if (Gravity.needBlock(b2) == false) {
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
	        BlockListener.useSquareRadius = BlockListener.ac.getConfig().getBoolean("CONFIG_GRAVITY_USE_SQUARE_RADIUS");
	        BlockListener.maxTimeToDoJob = BlockListener.ac.getConfig().getInt("CONFIG_GRAVITY_MAXTIME_DO_THE_JOB");
	        BlockListener.strengthRadius = BlockListener.ac.getConfig().getDouble("CONFIG_GRAVITY_STRENGTH_RADIUS");
	        BlockListener.checkingDelayAsTick = BlockListener.ac.getConfig().getInt("CONFIG_GRAVITY_REPEAT_CHECKING_DELAY_AS_TICK");
	        BlockListener.allowDiagonal = BlockListener.ac.getConfig().getBoolean("CONFIG_GRAVITY_ALLOW_DIAGONAL");
		}
	}

}
