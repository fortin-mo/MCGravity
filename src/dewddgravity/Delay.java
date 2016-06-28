package dewddgravity;

import dewddgravity.DigEventListener2;
import dewddgravity.MainLoop;
import org.bukkit.Bukkit;

class Delay
implements Runnable {
    Delay() {
    }

    @Override
	public void run() {
		while (DigEventListener2.ac == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		MainLoop mainLoop = new MainLoop();

		long repeat = (int) DigEventListener2.ac.getConfig().getInt("CONFIG_GRAVITY_REPEAT_CHECKING_DELAY_AS_TICK");
		if (repeat <= 0) {
			repeat = 1;
		}

		Bukkit.getScheduler().scheduleSyncRepeatingTask(DigEventListener2.ac, mainLoop, 0, repeat);

	}
}

