package lowbrain.mcgravity;

import java.util.List;

import org.bukkit.block.Block;

/**
 * Extended version of org.bukkit.block
 * @author moo
 *
 */
public class ExtendedBlock{

	private Block _block = null;
	private Double _strength = null;
	private List<RelativePosition> _jointPositions = null;
	
	/**
	 * initialize a ExtendBlock with an existing org.bukkit.Block
	 * @param block
	 */
	public ExtendedBlock(Block block){
		this._block = block;
	}
	
	/**
	 * get the strength of the current block
	 * @return
	 */
	public double getStrength(){
		if(_strength == null){
			Helper.GetBlockStrength(_block);
		}
		return _strength;
	}
	
	/**
	 * true if the current block is considered a joint
	 * @return
	 */
	public boolean getIsJoint(){
		return getJointPosition().size() > 0;
	}
	
	
	/**
	 * get the list of the joint position (also known as direction)
	 * @return
	 */
	public List<RelativePosition> getJointPosition(){
		if(_jointPositions == null){
			_jointPositions = Helper.isThisBlockJoint(_block);
		}
		return _jointPositions;
	}
	
	/**
	 * get the current block
	 * @return
	 */
	public Block getBLocks(){
		return _block;
	}
}