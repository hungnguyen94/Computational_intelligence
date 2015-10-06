
public class Tile {
	public int type;
	public int pheromones;

	public Tile(int t, int p){
		type = t;
		pheromones = p;
	}
	public void setType(int t){
		type = t;
	}
	public void setPheromones(int t){
		pheromones = t;
	}
	public int getType(){
		return type;
	}
	public int getPheromones(){
		return pheromones;
	}
}