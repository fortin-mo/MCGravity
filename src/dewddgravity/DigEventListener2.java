package dewddgravity;

import dewddgravity.Delay;
import dewddgravity.Gravity;
import dewddgravity.MainLoop;
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

public class DigEventListener2
implements Listener {
	public static JavaPlugin ac = null;
	public static boolean useFixedStrength = false;
	public static boolean useSquareRadius = false;
	public static double strengthRadius = 5.0;
	public static int maxTimeToDoJob = 1000;
	public static int checkingDelayAsTick = 1;
	
	private Random rnd = new Random();

	public DigEventListener2() {
		Delay delay = new Delay();
		Thread th = new Thread(delay);
		th.start();

	}

	// BlockPlaceEvent

	@EventHandler
	public void eventja(PlayerCommandPreprocessEvent e) {
		String m[] = e.getMessage().split(" ");
		
		if (m[0].equalsIgnoreCase("/gravity")) {
			if (m.length == 1) {
				e.getPlayer().sendMessage("/gravity reload");

			} else if (m.length == 2) {
				if (m[1].equalsIgnoreCase("reload")) {
					e.getPlayer().sendMessage("1.2");
					Bukkit.getScheduler().cancelTasks(ac);
					Delay delay = new Delay();
					Thread th = new Thread(delay);
					th.start();
				}
			}
		}
	}

	@EventHandler
	public void eventja(BlockBreakEvent e) {

		Block block = e.getBlock();
		Block b2 = null;

		int r = Gravity.r;
		int counter = 0;
		for (int x = -r; x <= r; x++) {
			for (int y = -r; y <= r; y++) {
				for (int z = -r; z <= r; z++) {
					
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
		int counter = 0;
		for (int x = -r; x <= r; x++) {
			for (int y = -r; y <= r; y++) {
				for (int z = -r; z <= r; z++) {

					// b2 = block.getRelative(x, y, z);

					b2 = block.getWorld().getBlockAt(block.getX() + x, block.getY() + y, block.getZ() + z);

					if (Gravity.needBlock(b2) == false) {
						continue;
					}

					// Gravity noop = new Gravity(b2, null, block, counter *
					// 25);
					MainLoop.jobs.put(b2.getLocation());

				}
			}

		}

	}

	// PlayerDeathEvent

	// PlayerInteractEvent
}

