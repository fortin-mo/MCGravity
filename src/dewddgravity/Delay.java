/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  dewddtran.tr
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package dewddgravity;

import dewddgravity.DigEventListener2;
import dewddgravity.MainLoop;
import dewddtran.tr;
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

		long repeat = (int) tr.gettrint("CONFIG_GRAVITY_REPEAT_CHECKING_DELAY_AS_TICK");
		if (repeat <= 0) {
			repeat = 20;
		}

		Bukkit.getScheduler().scheduleSyncRepeatingTask(DigEventListener2.ac, mainLoop, 0, repeat);

	}
}

