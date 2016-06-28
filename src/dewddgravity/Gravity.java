package dewddgravity;

import dewddgravity.MainLoop;
import dewddgravity.UsefulFunction;
import java.util.LinkedList;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

class Gravity
implements Runnable {
	
	private int strength = -1;
	
	public static int r = 1;

	public static int stick = 5;
	static long startTime = 0;

	static long countFailed = 0;

	static long countDone = 0;

	public static boolean needBlock(Block block) {
		switch (block.getType()) {
		case STATIONARY_WATER:
		case WATER:
		case STATIONARY_LAVA:
		case LAVA:
		case AIR:
			// case STAINED_GLASS_PANE:
			return false;
		default:
			return true;
		}

	}

	public int getStrength(){
		if(this.strength < 0){
			strength = UsefulFunction.GetBlockStrength(this.start);
		}
		return this.strength;
	}
	
	public Boolean canc = false;

	private Block start;

	private Player player = null;

	private int curDelay = 40;

	public Gravity(Block start, Player player, int curDelay) {
		this.start = start;
		this.player = player;
		this.curDelay = curDelay;
		Random rnd = new Random();

		// Bukkit.getScheduler().scheduleSyncDelayedTask(DigEventListener2.ac,
		// this, rnd.nextInt(100) + 20);

		// Bukkit.getScheduler().scheduleSyncDelayedTask(DigEventListener2.ac,
		// this);

	}

	public void recusiveSearchBlock(Block cur, Block start, LinkedList<Block> list) {
		// add
		dprint.r.printC("recusiveSearchBlock called");
		Block tmp = null;
		int searchSpace = Gravity.r;
		for (int x = -searchSpace; x <= searchSpace; x++) {

			for (int z = -searchSpace; z <= searchSpace; z++) {
				// tmp = cur.getRelative(x, 0, z);
				tmp = cur.getWorld().getBlockAt(cur.getX() + x, cur.getY(), cur.getZ() + z);

				// dprint.r.printAll("loca " +
				// tr.locationToString(tmp.getLocation()));

				double xxx = Math.abs(tmp.getX() - start.getX());
				double zzz = Math.abs(tmp.getZ() - start.getZ());

				double dis = (xxx * xxx) + (zzz * zzz);
				dis = Math.pow(dis, 0.5);

				/*
				 * if (dis > stick) { continue; }
				 */

				if ((xxx > Gravity.stick) || (zzz > Gravity.stick)) {
					continue;
				}

				if (Gravity.needBlock(tmp) == true) {
					// open it

					boolean searchChest = false;
					for (int i = 0; i < list.size(); i++) {
						Block inList = list.get(i);

						if (tmp.getLocation().getBlockX() == inList.getX()) {
							if (tmp.getLocation().getBlockY() == inList.getY()) {
								if (tmp.getLocation().getBlockZ() == inList.getZ()) {
									searchChest = true;
									break;
								}
							}
						}
					}
					if (searchChest == true) {
						continue;
					}

					list.add(tmp);

					// dprint.r.printAll("found block " +
					// tr.locationToString(tmp.getLocation())
					// + " size " + list.size());

					// call recursive

					this.recusiveSearchBlock(tmp, start, list);

				} // chest
			}

		}

	}

	@Override
	public void run() {

		Gravity.startTime = System.currentTimeMillis();
		Gravity.countFailed = 0;
		Gravity.countDone = 0;

		if (MainLoop.lostTime > MainLoop.maxTime) {
			return;
		}
		// check it's has near block or not

		int r = Gravity.r;

		Block b2 = null;

		if (this.start.getY() == 0) {
			return;
		}
		if (Gravity.needBlock(this.start) == false) {
			return;
		}
		if (this.start.getRelative(0, -1, 0).getType() != Material.AIR) {
			return;
		}

		LinkedList<Block> list = new LinkedList<Block>();

		boolean found = UsefulFunction.isThisBlockHasRoot(this.start, this.start, list);

		long timeUsed = System.currentTimeMillis() - Gravity.startTime;
		MainLoop.lostTime += timeUsed;
		// dprint.r.printAll("><><> end " + (timeUsed) + " T " +
		// Gravity.countDone + " F " + Gravity.countFailed + " sum "
		// + MainLoop.lostTime);

		if (found == false) {
			Material mat = this.start.getType();
			byte data = this.start.getData();
			this.start.setTypeId(0, true);
			this.start.getWorld().spawnFallingBlock(this.start.getLocation(), mat, data);

			int counter = 0;

			//int tmpr = Gravity.stick;
			int tmpr = 1; //UsefulFunction.GetBlockStrength(this.start);
			
			//dprint.r.printC("block material : " + this.start.getType() + " , strength : " + tmpr);
			
			for (int x = -tmpr; x <= tmpr; x++) {
				for (int y = -tmpr; y <= tmpr; y++) {
					for (int z = -tmpr; z <= tmpr; z++) {
						counter++;
						// b2 = start.getRelative(x, y, z);

						b2 = this.start.getWorld().getBlockAt(this.start.getX() + x, this.start.getY() + y,
								this.start.getZ() + z);

						if (Gravity.needBlock(b2) == false) {
							continue;
						}

						MainLoop.jobs.put(b2.getLocation());
						// Gravity noop = new Gravity(b2, player, block, counter
						// * 30);

					}
				}

			}

		} // sync
	}
}

