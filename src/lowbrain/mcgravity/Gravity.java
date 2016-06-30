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

	public Gravity(Block start) {
		this.start = start;
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

		boolean found = Helper.isThisBlockHasRoot(this.start, this.start, list,1);

		long timeUsed = System.currentTimeMillis() - Gravity.startTime;
		MainLoop.lostTime += timeUsed;

		if (found == false) {
			Material mat = this.start.getType();
			byte data = this.start.getData();
			this.start.setTypeId(0, true);
			this.start.getWorld().spawnFallingBlock(this.start.getLocation(), mat, data);

			for (int x = -Gravity.r; x <= Gravity.r; x++) {
				for (int y = -Gravity.r; y <= Gravity.r; y++) {
					for (int z = -Gravity.r; z <= Gravity.r; z++) {
						
						if(!BlockListener.allowDiagonal && Math.abs(x) + Math.abs(y) + Math.abs(z) > 1){
							continue;
						}
						
						//b2 = this.start.getWorld().getBlockAt(this.start.getX() + x, this.start.getY() + y,
							//	this.start.getZ() + z);
						
						b2 = this.start.getRelative(x,y,z);

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

