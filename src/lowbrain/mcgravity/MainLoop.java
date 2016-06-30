
package lowbrain.mcgravity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

import lowbrain.mcgravity.BlockListener;
import lowbrain.mcgravity.Gravity;
import lowbrain.mcgravity.TheJobType;

class MainLoop
implements Runnable {
	public static TheJobType jobs = new TheJobType();

	public static long lostTime = 0;
	public static long maxTime = 50;

	public MainLoop() {

	}

	@Override
	public void run() {
		// always get Item from jobs

		maxTime = (long) BlockListener.maxTimeToDoJob;
		if (maxTime <= 0) {
			maxTime = 1000;
		}

		long startTime = System.currentTimeMillis();

		if (MainLoop.lostTime >= maxTime) {
			MainLoop.lostTime -= maxTime;

			if (MainLoop.lostTime < 0) {
				MainLoop.lostTime = 0;
			}

			return;
		} 

		while (MainLoop.jobs.getSize() > 0 && lostTime < maxTime) {
			if (MainLoop.lostTime > maxTime) {
				return;
			}

			Location loc = MainLoop.jobs.get();

			if (loc != null) {

				Block blo = loc.getBlock();
				if (Gravity.needBlock(blo) == false) {
					continue;
				}

				Gravity noop = new Gravity(blo);
				Bukkit.getScheduler().scheduleSyncDelayedTask(BlockListener.ac, noop);

			}

		}
		
		

	}
}

