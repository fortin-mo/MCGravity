package lowbrain.mcgravity;

import java.util.LinkedList;
import org.bukkit.Location;
import org.bukkit.block.Block;

class TheJobType {
	private LinkedList<Location> loc = new LinkedList<Location>();

	public synchronized Location get() {
		boolean found = false;
		Location tmp2 = null;

		while (!found && this.loc.size() > 0) {
			
			Location tmp = this.loc.get(0);
			this.loc.remove(0);

			if (tmp == null) {
				continue;
			}

			if (!Helper.needBlock(tmp.getBlock())) {
				continue;
			}

			tmp2 = tmp;
			found = true;

		}

		return tmp2;
	}

	public int getSize() {
		return this.loc.size();
	}

	public void put(Location loc) {
		Block blo = loc.getBlock();
		if (blo == null) {
			return;
		}
		/*
		 * if (blo.getType().isSolid() == false) { return; }
		 */

		if (!Helper.needBlock(blo)) {
			return;
		}
		// search
		if (!this.loc.contains(loc)) {
			this.loc.add(loc);
		}
	}
}

