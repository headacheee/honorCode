import java.util.ArrayList;


public class Network {
	
	int n;
	public Link link[][];
	ArrayList<Link> linkList = new ArrayList<Link>();
	double sdnFlow[][];
	int switches[];
	
	Network(int n){
		link = new Link[n][n];
		sdnFlow = new double[n][n];
		for(int i = 0; i<n; i++)
			for(int j = 0;j<n;j++){
				link[i][j] = null;
				sdnFlow[i][j] = 0;
			}
		this.n = n;
	}
	
	Network(int n,int[] switches){
		link = new Link[n][n];
		sdnFlow = new double[n][n];
		for(int i = 0; i<n; i++)
			for(int j = 0;j<n;j++){
				link[i][j] = null;
				sdnFlow[i][j] = 0;
			}
		this.n = n;
		this.switches = switches;
	}
	
	void setSwitches(int[] switches){
		this.switches = switches;
	}
	
	public void setMatrixByTopology(Topology to){
		int m = to.m;
		for(int i = 0;i<m;i++){
			Links links = to.links[i];
			this.addBiLink(links.capacity,links.length,links.start,links.end);
		}
	}
	
	void addBiLink(double c,double l,int s,int e){
		this.link[s][e] = new Link(c,l,s,e);
		linkList.add(this.link[s][e]);
		this.link[e][s] = new Link(c,l,e,s);
		linkList.add(this.link[e][s]);
	}
	
	public void setMatrix(){
		this.link[1][2] = new Link(2,1,1,2);
		this.link[1][3] = new Link(3,3,1,3);
		this.link[1][4] = new Link(2,3,1,4);
		this.link[2][4] = new Link(2,2,2,4);
		this.link[2][12] = new Link(1,4,2,12);
		this.link[0][2] = new Link(3,5,0,2);
		this.link[0][9] = new Link(4,2,0,9);
		this.link[0][12] = new Link(1,1,0,12);
		this.link[3][4] = new Link(1,1,3,4);
		this.link[3][5] = new Link(1,2,3,5);
		this.link[4][11] = new Link(2,2,4,11);
		this.link[4][12] = new Link(4,3,4,12);
		this.link[5][6] = new Link(3,2,5,6);
		this.link[5][7] = new Link(2,2,5,7);
		this.link[5][11] = new Link(1,3,5,11);
		this.link[6][7] = new Link(1,1,6,7);
		this.link[7][8] = new Link(2,2,7,8);
		this.link[7][11] = new Link(3,2,7,11);
		this.link[8][9] = new Link(1,1,8,9);
		this.link[8][10] = new Link(2,2,8,10);
		this.link[9][10] = new Link(3,2,9,10);
		this.link[10][11] = new Link(3,1,10,11);
		this.link[10][12] = new Link(1,2,10,12);
		
		for(int i = 0 ; i < this.n ; i++)
			for(int j = 0 ; j < i; j++){
				Link a = this.link[j][i];
				this.link[i][j] = new Link(a.c,a.l,a.e,a.s);
			}
	}
	
	
	
	
	public void setMatrix4(){
		/*		
		addLink(1,2,0,1);
		addLink(1,4,0,2);
		addLink(1,4,0,3);
		addLink(1,3,1,3);
		addLink(1,2,2,3);
 		*/		
//		/*
		addLink(3,2,0,1);
		addLink(4,4,0,2);
		addLink(2,4,0,3);
		addLink(2,3,1,3);
		addLink(2,2,2,3);

//		*/
		for(int i = 0 ; i < this.n ; i++)
			for(int j = 0 ; j < i; j++){
				Link a = this.link[j][i];
				if(a!=null)
					addLink(a.c,a.l,a.e,a.s);
			}

	}
	
	public void setMatrix3(){
		addLink(0,1,0,3);
		addLink(1,1,3,2);
		addLink(1,1,3,1);
		addLink(1,1,1,2);
		addLink(1,1,0,2);
		addLink(2,1,0,4);
		addLink(2,1,4,2);
	//	addLink(1,1,2,1);
	}
	
	public void setMatrix7(){
		int a[] = {1,1,0,2,
				1,1,0,3,
				2,1,2,3,
				2,1,1,2,
				1,1,1,7,
				1,1,2,7,
				2,1,2,5,
				1,1,3,4,
				2,1,5,4,
				1,1,5,6,
				2,1,7,6
				};
		setArrayMatrix(a);
	}
	
	void addLink(double c,double l,int s,int e){
		this.link[s][e] = new Link(c,l,s,e);
		linkList.add(this.link[s][e]);
	}
	
	void printSDNFlow(){
		for(int i = 0; i<n;i++){        //print sdn flow
			for(int j=0;j<n;j++){
				System.out.printf("%1.3f ",this.sdnFlow[i][j]);
			}
			System.out.println();
		}
	}
	
	void printFlow(double l[][]){
		for(int i=0;i<l.length;i++)
			System.out.format("  %4s",i);
		for(int i=0;i<l.length;i++){
			System.out.printf("\n%d ",i);
			for(int j=0;j<l[0].length;j++)
				System.out.printf("%1.3f ",l[i][j]);
		}
		System.out.println();
	}
	
	void printLinkFlow(){
		for(int i = 0; i<this.linkList.size();i++)   // print link flow
			System.out.println(this.linkList.get(i).s+" "+this.linkList.get(i).e +" "+(this.linkList.get(i).flow+this.linkList.get(i).sdnF));
	}
	
	public void setArrayMatrix(int[] a){
		if(a.length%4!=0){
			System.out.println("matrix array error");
			System.exit(0);
		}
		
		int i = 0;
		while(i<a.length){
			addLink(a[i],a[i+1],a[i+2],a[i+3]);
			i += 4;
		}
	}
}
