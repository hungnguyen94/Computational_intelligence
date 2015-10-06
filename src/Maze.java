import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Maze {
	public int[] mazesize;
	public int[][] maze; 
	
	public Maze(int[] mazes, int[][] Maze){
		mazesize = mazes;
		maze = Maze;
	}
	public int[][] getMaze(){
		return maze;
	}
	public Maze read(String filename){
		Maze maze = null;
		try{
		File file = new File(filename);
		Scanner sc = new Scanner(file);
		int[] mazes = new int[]{sc.nextInt(), sc.nextInt()};
		for (int i = 0; i < mazes[0]; i++){
			for(int z = 0; z < mazes[1]; z++){
				
			}
		}
		}
		catch(FileNotFoundException e){
			System.out.println("file not found");
		}
		return maze;
	}
}
