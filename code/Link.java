
public class Link {
	double c;  //capacity
	double l;  //length
	int s;  //start
	int e;  //end
	double flow = 0;
	double sdnF = 0;
	double total = 0;
	
	Link(double c, double l,int s,int e){
		this.c = c;
		this.l = l;
		this.s = s;
		this.e = e;
	}
	
	Link(double c, double l,int s,int e,double flow){
		this.c = c;
		this.l = l;
		this.s = s;
		this.e = e;
		this.flow = flow;
	}
	
	Link(double c, double l){
		this.c = c;
		this.l = l;
	}
}
