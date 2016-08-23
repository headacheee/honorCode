import java.util.ArrayList;


public class Path {
	ArrayList<Integer> path;
	double distance = -1;
	
	public Path(){
		path = new ArrayList<Integer>();
	}
	
	public Path(int[] list){
		path = new ArrayList<Integer>();
		setPath(list);
	}
	
	public Path(ArrayList<Integer> path){
		this.path = path;
	}
	
	public void printPath(){
		System.out.print("path: ");
		for(int i=0;i<path.size();i++){
			System.out.print(path.get(i)+" ");
		}
		System.out.println("distance: "+distance );
	}
	
	public void printPath(double lMatrix[][]){
		System.out.print("path: ");
		for(int i=0;i<path.size();i++){
			System.out.print(path.get(i)+" ");
		}
		setDistance(lMatrix);
		System.out.println("distance: "+distance );
	}
	
	public void setDistance(double lMatrix[][]){
		double distance = 0;
		int parent = -1;
		for(int i=0;i<this.path.size();i++){
			if(parent == -1){
				parent = this.path.get(i);				
			}else{
				int current = this.path.get(i);
				distance += lMatrix[parent][current];
				parent = current;
			}
		}
		this.distance = distance;
	}
	
	public void setPath(int[] list){
		for(int i=0;i<list.length;i++)
			this.path.add(list[i]);
	}
}
