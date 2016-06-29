package lowbrain.mcgravity;

import org.bukkit.Bukkit;

import lowbrain.mcgravity.BlockListener;
import lowbrain.mcgravity.MainLoop;

class Delay
implements Runnable {
    Delay() {
    }

    @Override
	public void run() {
		while (BlockListener.ac == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		MainLoop mainLoop = new MainLoop();

		long repeat = BlockListener.checkingDelayAsTick;
		if (repeat <= 0) {
			repeat = 1;
		}
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(BlockListener.ac, mainLoop, 0, repeat);

	}
}

