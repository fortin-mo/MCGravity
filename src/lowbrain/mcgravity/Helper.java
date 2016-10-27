package lowbrain.mcgravity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.block.Block;
import lowbrain.mcgravity.Gravity;

/**
 * Helper class for needed functions
 * @author lowbrain
 *
 */
class Helper {
	
	/**
	 * Check if the current block has root (connected to a block in his strength radius)
	 * @param cur current block
	 * @param start starting block
	 * @param usedList list of already checked block
	 * @param foundation not yet implemented
	 * @return
	 */
	public static boolean isThisBlockHasRoot(Block cur, Block start, LinkedList<Block> usedList, int foundation) {

		Block b2 = null;
		/*
		if(foundation >= BlockListener.foundation)
		{
			BlockListener.ac.getLogger().info("DEBUG : is foundation !!");
			return true;
		}
		*/

		if (cur.getY() == 0) {
			return Helper.needBlock(cur);
		}

		for (int y = -Gravity.r; y <= Gravity.r; y++) {
			for (int x = -Gravity.r; x <= Gravity.r; x++) {
				for (int z = -Gravity.r; z <= Gravity.r; z++) {

					if(!BlockListener.allowDiagonal && Math.abs(x) + Math.abs(y) + Math.abs(z) > 1){
						continue;
					}
					
					b2 = cur.getRelative(x,y,z);
					
					if (usedList.contains(b2)) {
						continue;
					}

					double xxx = Math.abs(b2.getX() - start.getX());
					double zzz = Math.abs(b2.getZ() - start.getZ());
					
					double strength;
					
					if(BlockListener.useFixedStrength){
						strength = BlockListener.strengthRadius;
					}
					else strength = Helper.GetBlockStrength(cur); 
					
					if(BlockListener.useSquareRadius){
						if ((xxx > strength) || (zzz > strength)) {
							Gravity.countFailed++;
							continue;
						}
					}
					else{
						double dis = Math.pow((xxx * xxx) + (zzz * zzz),0.5);
						
						if(dis > strength){
							Gravity.countFailed++;
							continue;
						}
					}

					usedList.add(b2);

					if (!Helper.needBlock(b2)) {
						Gravity.countFailed++;
						continue;
					}
					boolean found = Helper.isThisBlockHasRoot(b2, start, usedList, foundation += 1);

					if (found) {
						Gravity.countDone++;
						return true;
					} else {
						Gravity.countFailed++;
						continue;
					}

				}

			}
		}
		return false;
	}

	/**
	 * return the block's strength depending his material
	 * @param block
	 * @return
	 */
	public static double GetBlockStrength(Block block){
        double strength = BlockListener.blockStrength.getOrDefault(block.getType().name(), BlockListener.strengthRadius);

		if(BlockListener.useStrengthMultiplier) strength *= getBlockStrengthMultiplierV2(block);

		return strength;
		//return strength * getBlockStrengthMultiplier(block);
	}

	/**
	 * return the block strength multiplier... old
	 * @param block
	 * @return
	 */
	private static double getBlockStrengthMultiplier(Block block){		
		double multiplier = 1; //if you build a 4x4 block it will make it stronger
		
		List<Block> nearBlocks = new ArrayList<Block>();//list of none water,air,lava blocks that are in the range of the current block
		Block b2 = null;
		for (int y = -1; y <= 1 ; y++) {//only the blocks above and under and on the same level can make a difference
			for (int x = -3; x <= 3; x++) {//3x3 because we could go till the end, but except slowing shits down it wont change much
				for (int z = -3; z <= 3; z++) {//3x3 because we could go till the end, but except slowing shits down it wont change much
					if(x == 0 && y == 0 && z == 0){//current block
						continue;
					}
					b2 =  block.getRelative(x,y,z);
					if(b2 != null && Helper.needBlock(b2)){
						nearBlocks.add(b2);
					}
				}
			}
		}
		
		
		List<ArrayList<Block>> connectedBlocks = groupConnectedBlocks(nearBlocks);
		
		
		//For each group of connected blocks, we need at least one of them to be directly attached to the main block
		for (int i = 0; i < connectedBlocks.size(); i++) {
			boolean connected = false;
		
			for (int j = 0; j < connectedBlocks.get(i).size(); j++) {
				Block current = connectedBlocks.get(i).get(j);
				
				if(Helper.areTheseBlockFaced(block, current)){
					connected = true;
					continue;
				}
			}
			if(connected) multiplier += connectedBlocks.get(i).size() * 0.1;
		}
		//BlockListener.ac.getLogger().info("multiplier =  " + multiplier );
		return multiplier;
	}

	/**
	 * new version : return the block strength multiplier
	 * @param block
	 * @return
	 */
	private static double getBlockStrengthMultiplierV2(Block block){			
		double multiplier = 1;
		
		List<RelativePosition> lstFaces = new ArrayList<RelativePosition>();
		lstFaces.add(new RelativePosition(1,0,0));//right
		lstFaces.add(new RelativePosition(-1,0,0));//left
		lstFaces.add(new RelativePosition(0,1,0));//top
		lstFaces.add(new RelativePosition(0,-1,0));//bottom
		lstFaces.add(new RelativePosition(0,0,1));//front
		lstFaces.add(new RelativePosition(0,0,-1));//back
		
		int maxY = 1;
		int maxX = 3;
		int maxZ = 3;
		
		MutableInt sum = new MutableInt(0);
		ArrayList<Block> watchList = new ArrayList<Block>();
		
		for (int i = 0; i < lstFaces.size(); i++) {
			RelativePosition pos = lstFaces.get(i);
			Block b2 = block.getRelative(pos.getX(),pos.getY(),pos.getZ());
			if(!Helper.needBlock(b2)) continue;
			
			recursiveSearch(sum, watchList,maxY,maxX,maxZ, block, b2);
		}
		
		multiplier += sum.intValue() * BlockListener.strengthMultiplier;
		//BlockListener.ac.getLogger().info("multiplier =  " + multiplier );
		return multiplier;
	}
	
	/**
	 * recursive function for block strength multiplier... check connection between blocks
	 * @param sum amount of block with more then 1 connection
	 * @param watchList list of already watched block
	 * @param maxY max y
	 * @param maxX max x
	 * @param maxZ max z
	 * @param start starting block
	 * @param b1 next block
	 */
	private static void recursiveSearch(MutableInt sum,ArrayList<Block> watchList,int maxY, int maxX, int maxZ, Block start, Block b1){
		
		watchList.add(b1);
		
		int count = 0;
		
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					
					if(x == 0 && y == 0 && z == 0) continue;
					
					if(Math.abs(x) + Math.abs(y) + Math.abs(z) > 1){
						continue;
					}
					
					Block b2 = b1.getRelative(x,y,z);
					
					if(!needBlock(b2)) continue;
					
					if(watchList.contains(b2)){
						count += 1;
						continue;
					}
					
					int dx = Math.abs(start.getX() - b2.getX());
					int dy = Math.abs(start.getY() - b2.getY());
					int dz = Math.abs(start.getZ() - b2.getZ());
					
					if(dx >= maxX || dy >= maxY || dz >= maxZ){
						continue;
					}
					if(dx == 0 && dy == 0 && dz == 0) {
						count += 1;
						continue;
					}	
					
					count += 1;
					
					recursiveSearch(sum,watchList, maxY, maxX, maxZ, start, b2);
				}
			}
		}
		
		if(count >= 2){
			sum.increment();
		}
	}
	
	/**
	 * Take the near blocks list and regroup blocks that are connected to each other
	 * @param nearBlocks
	 * @return
	 */
	public static List<ArrayList<Block>> groupConnectedBlocks(List<Block> nearBlocks){

		long startTime = System.currentTimeMillis();
		
		List<ArrayList<Block>> connectedBlocks = new ArrayList<ArrayList<Block>>();
		
		for (int currentIndex = 0; currentIndex < nearBlocks.size(); currentIndex++) {
			
			Block current = nearBlocks.get(currentIndex);//the block that we are currently checking
			
			List<Block> tmp = new ArrayList<Block>(); //a temporary list of other block that are connected to the current one
			for (int i = 0; i < nearBlocks.size(); i++) {
				if(i == currentIndex) continue;//no need to check the same block
				Block b2 = nearBlocks.get(i);
				if(Helper.areTheseBlockFaced(current, b2)){//if the second block is connected to the current one, we add it to the temporary list
					tmp.add(b2);
				}
			}
			
			if(tmp.size() > 2){ //we beed at least 3 connection to be valid
				
				if(connectedBlocks.size() == 0){//list is empty so we add the first one
					connectedBlocks.add(new ArrayList<Block>());
					connectedBlocks.get(0).add(current);
				}
				else{
					boolean linked = false;
					//we check if one off the connected block is already in a group... if so we add the current one to that same list. they are now regrouped
					for (int i = 0; i < connectedBlocks.size(); i++) {
						for (int j = 0; j < tmp.size(); j++) {
							if(connectedBlocks.get(i).contains(tmp.get(j))){
								connectedBlocks.get(i).add(current);
								break;
							}
						}
					}
					if(!linked){//if not we create a new group for that block
						connectedBlocks.add(new ArrayList<Block>());
						connectedBlocks.get(connectedBlocks.size() -1 ).add(current);
					}
					
				}
			}
			
		}
		
		long elapsed = System.currentTimeMillis() - startTime;
			
		return connectedBlocks;
	}
	
	public static boolean areTheseBlockFaced(Block b1, Block b2){
		int dx;//distance X
		int dy;//distance Y
		int dz;//distance Z
		
		dx = Math.abs(b1.getX() - b2.getX());
		dy = Math.abs(b1.getY() - b2.getY());
		dz = Math.abs(b1.getZ() - b2.getZ());
		
		return Math.abs(dx + dy + dz) == 1;
	}
	
	/**
	 * Check if the block is made of water, lava or air
	 * @param block
	 * @return if the block is made of water, lava or air, return false otherwise return true;
	 */
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

	/**
	 * Return the list of relative position(direction/sense) of the block's joint
	 * @param block
	 * @return
	 */
	public static List<RelativePosition> isThisBlockJoint(Block block){
		List<RelativePosition> lst = new ArrayList<RelativePosition>();
		for (int x = -Gravity.r; x < Gravity.r; x++) {
			for (int z = -Gravity.r; z < Gravity.r; z++) {
				if(Helper.needBlock(block.getRelative(x,0,z)) && Helper.needBlock(block.getRelative(x,-1,z))){
					//is anchar at this face
					lst.add(new RelativePosition(x,0,z));
				}
			}
		}
		return lst;
	}
}

