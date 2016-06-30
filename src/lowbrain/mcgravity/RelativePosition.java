package lowbrain.mcgravity;

public class RelativePosition {
	private int x = 0;
	private int y = 0;
	private int z = 0;
	
	public RelativePosition(){
		
	}
	public RelativePosition(int x, int y, int z){
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	
}
