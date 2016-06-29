package lowbrain.mcgravity;

import java.util.LinkedList;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import lowbrain.mcgravity.Helper;
import lowbrain.mcgravity.MainLoop;

class Gravity
implements Runnable {
	
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

	
	public Boolean canc = false;

	private Block start;

	private Player player = null;

	private int curDelay = 40;

	public Gravity(Block start, Player player, int curDelay) {
		this.start = start;
		this.player = player;
		this.curDelay = curDelay;
		Random rnd = new Random();

	}

	public void recusiveSearchBlock(Block cur, Block start, LinkedList<Block> list) {
		// add
		Block tmp = null;
		int searchSpace = Gravity.r;
		for (int x = -searchSpace; x <= searchSpace; x++) {

			for (int z = -searchSpace; z <= searchSpace; z++) {
				// tmp = cur.getRelative(x, 0, z);
				tmp = cur.getWorld().getBlockAt(cur.getX() + x, cur.getY(), cur.getZ() + z);

				double xxx = Math.abs(tmp.getX() - start.getX());
				double zzz = Math.abs(tmp.getZ() - start.getZ());

				double dis = (xxx * xxx) + (zzz * zzz);
				dis = Math.pow(dis, 0.5);


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

	@SuppressWarnings("deprecation")
	@Override
	public void run() {

		Gravity.startTime = System.currentTimeMillis();
		Gravity.countFailed = 0;
		Gravity.countDone = 0;

		if (MainLoop.lostTime > MainLoop.maxTime) {
			return;
		}
		// check it's has near block or not

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

		boolean found = Helper.isThisBlockHasRoot(this.start, this.start, list);

		long timeUsed = System.currentTimeMillis() - Gravity.startTime;
		MainLoop.lostTime += timeUsed;

		if (found == false) {
			Material mat = this.start.getType();
			byte data = this.start.getData();
			this.start.setTypeId(0, true);
			this.start.getWorld().spawnFallingBlock(this.start.getLocation(), mat, data);

			

			//UsefulFunction.GetBlockStrength(this.start);
			for (int x = -Gravity.r; x <= Gravity.r; x++) {
				for (int y = -Gravity.r; y <= Gravity.r; y++) {
					for (int z = -Gravity.r; z <= Gravity.r; z++) {
						
						if(!BlockListener.allowDiagonal && Math.abs(x) + Math.abs(y) + Math.abs(z) > 1){
							continue;
						}
						
						b2 = this.start.getWorld().getBlockAt(this.start.getX() + x, this.start.getY() + y,
								this.start.getZ() + z);

						if (Gravity.needBlock(b2) == false) {
							continue;
						}

						MainLoop.jobs.put(b2.getLocation());
					}
				}

			}

		} // sync
	}
}

