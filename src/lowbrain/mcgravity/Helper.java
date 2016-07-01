package lowbrain.mcgravity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
		int strength = -1;
		
		
		switch (block.getType()) {
		//none
		
		case CHORUS_FLOWER:
		case YELLOW_FLOWER:
		case CHORUS_PLANT:
		case DOUBLE_PLANT:
		case LONG_GRASS:
		case BROWN_MUSHROOM:
		case RED_MUSHROOM:
		case SUGAR_CANE_BLOCK:
		case SEEDS:
		case MELON_SEEDS:
		case BEETROOT_SEEDS:
		case PUMPKIN_SEEDS:
		case SAPLING:
		case RED_ROSE:
		case NETHER_WARTS:
		case CARROT:
		case POTATO:
		case WATER_LILY:
		case MELON_STEM:
		case PUMPKIN_STEM:
		case WHEAT:
		case SUGAR_CANE:
		case DEAD_BUSH:
			strength = -1;
			break;
		
		//weakest
		
		case LEAVES:
		case LEAVES_2:
			strength = 3;
			break;
		
		//low strength
		case DIRT:
		case GRASS:
		case GRASS_PATH:
			strength = 4;
			break;
			
		case LAPIS_BLOCK:
			strength = 4;
			break;
			
		case CLAY:
			strength = 5;
			break;
			
		case ICE:
		case FROSTED_ICE:
			strength = 5;
			break;
		
		//medium low strength
			
		case GOLD_ORE:
		case EMERALD_ORE:
		case DIAMOND_ORE:
		case COAL_ORE:
		case GLOWING_REDSTONE_ORE:
		case IRON_ORE:
		case LAPIS_ORE:
		case QUARTZ_ORE:
		case REDSTONE_ORE:
			strength = 6;
			break;		
			
		case WOOD:
			strength = 6;
			break;
		
		//somehow strong
		
		case HARD_CLAY:
		case STAINED_CLAY:
			strength = 7;
			break;
			
		case COBBLESTONE:
		case GLOWSTONE:
		case SANDSTONE:
		case RED_SANDSTONE:
			strength = 8;
			break;
				
		//Strong	
			
		case STONE:
		case STONE_SLAB2:
		case DOUBLE_STONE_SLAB2:
		case ENDER_STONE:
			strength = 10;
			break;
			
		case LOG:
		case LOG_2:
			strength = 10;
			break;
			
		case BRICK:
		case SMOOTH_BRICK:
		case END_BRICKS:
		case RED_NETHER_BRICK:
		case NETHER_BRICK:
			strength = 12;
			break;
			
		//Strongest blocks
			
		case IRON_BLOCK:
		case BED_BLOCK:
		case EMERALD_BLOCK:
		case GOLD_BLOCK:
		case DIAMOND_BLOCK:
		case OBSIDIAN:
		case QUARTZ_BLOCK:
			strength = 15;
			break;
			
		//bedrock lol
		case BEDROCK:
			strength = 256;
			break;
			
		default:
			strength = 5;
			break;
		}
		
		return strength;// * getBlockStrengthMultiplier(block);
	}

	/**
	 * return the block strength multiplier
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
		
		int dx;//distance X
		int dy;//distance Y
		int dz;//distance Z
		List<ArrayList<Block>> connectedBlocks = new ArrayList<ArrayList<Block>>();
		
		//check if near blocks are at least connected to two other near blocks
		//if not, they won't be taking in account for the multiplier
		for (int i = 0; i < nearBlocks.size(); i++) {
			
			List<Block> tmpConnected = new ArrayList<Block>();
			
			Block current = nearBlocks.get(i);
			tmpConnected.add(current);
			
			int countConnected = 0;
			for (int j = 0; j < nearBlocks.size(); j++) {
				if(i == j) continue;//we don't want compare the same block
				Block next = nearBlocks.get(j);
				
				dx = Math.abs(current.getX() - next.getX());
				dy = Math.abs(current.getY() - next.getY());
				dz = Math.abs(current.getZ() - next.getZ());
				
				if(Math.abs(dx + dy + dz) != 1){
					continue;
				}
				countConnected += 1;
				tmpConnected.add(next);
			}
			if(countConnected >= 2) {
				if(connectedBlocks.size() == 0){
					connectedBlocks.add(new ArrayList<Block>());
					connectedBlocks.get(0).add(current);
				}
				else{
					for (int j = 0; j < connectedBlocks.size(); j++) {
						if(connectedBlocks.get(j).contains(current)){
							
						}
						else{
							
						}
					}
				}
			}
		}
		
		//For each group of connected blocks, we need at least one of them to be directly attached to the main block
		for (int i = 0; i < connectedBlocks.size(); i++) {
			boolean connected = false;
		
			for (int j = 0; j < connectedBlocks.get(i).size(); j++) {
				Block current = connectedBlocks.get(i).get(j);
				
				dx = Math.abs(block.getX() - current.getX());
				dy = Math.abs(block.getY() - current.getY());
				dz = Math.abs(block.getZ() - current.getZ());
				
				if(Math.abs(dx + dy + dz) == 1){
					connected = true;
					continue;
				}
			}
			if(connected) multiplier += connectedBlocks.get(i).size() * 0.1;
		}
		
		return multiplier;
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

