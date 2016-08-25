
public class Nodes {

	double x;
	double y;
	String name;
	int id;
		
	Nodes(double x, double y, String name,int id){
		this.x = x;
		this.y = y;
		this.name = name;
		this.id = id;
	}
		
	Nodes(){
	}
	
	void print(){
		System.out.printf("Name: %s, x: %f, y: %f, id: %d\n", name,x,y,id);
	}

}
