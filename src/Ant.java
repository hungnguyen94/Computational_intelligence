
public class Ant {

	public int[] location;
	public Maze maze;

	public Ant(int[] loc, Maze m){
		location = loc;
		maze = m;
	}
	public int[] getLocation(){
		return location;
	}
	
	public String move(){
		int count = 0;
		int[][] m1 = maze.getMaze();
		if (m1[location[0]][location[1] - 1] == 0){
			location[1] = location[1] -1;
			
		}
		return "moved";
	}
}
