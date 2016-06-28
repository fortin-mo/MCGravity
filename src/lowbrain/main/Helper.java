package lowbrain.main;

import java.util.LinkedList;
import org.bukkit.block.Block;

import lowbrain.main.Gravity;

class Helper {
	public static boolean isThisBlockHasRoot(Block cur, Block start, LinkedList<Block> usedList) {

		Block b2 = null;

		// add allow to usedList

		if (cur.getY() == 0) {
			return Gravity.needBlock(cur);
		}

		for (int y = -Gravity.r; y <= 0; y++) {
			for (int x = -Gravity.r; x <= Gravity.r; x++) {
				for (int z = -Gravity.r; z <= Gravity.r; z++) {

					if(!BlockListener.allowDiagonal && Math.abs(x) + Math.abs(y) + Math.abs(z) > 1){
						continue;
					}
					
					b2 = cur.getWorld().getBlockAt(cur.getX() + x, cur.getY() + y, cur.getZ() + z);
					if (usedList.contains(b2) == true) {
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

					if (!Gravity.needBlock(b2)) {
						Gravity.countFailed++;
						continue;
					}

					boolean ret = Helper.isThisBlockHasRoot(b2, start, usedList);

					if (ret) {
						Gravity.countDone++;
						return true;
					} else {
						Gravity.countFailed++;
						continue;
					}

				}

			}
		}
		// }
		//dprint.r.printC(cur.getType() + " has no root");
		return false;
	}

	public static int GetBlockStrength(Block cur){
		int strength = -1;
		
		
		switch (cur.getType()) {
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
			/*
			if(cur.getType().isSolid()){
				strength = 5;
			}
			else {
				strength = -1;
				dprint.r.printC("block material : " + cur.getType() + " , strength : " + strength);
			}
			*/
			break;
		}
		//dprint.r.printC("block material : " + cur.getType() + " , strength : " + strength);
		
		return strength;
	}
}

