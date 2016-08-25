
public class Links {
	String source;
	String target;
	double capacity;
	double length;
	int start;
	int end;
	
	Nodes getNode(String s,Nodes[] nodes){
		for(int i=0;i<nodes.length;i++){
			if(s.equals(nodes[i].name))
				return nodes[i];
		}
		return null;
	}
	
	void setAll(Nodes[] nodes){
		Nodes n1 = getNode(source,nodes);
		Nodes n2 = getNode(target,nodes);
		this.length =Math.sqrt((n1.x-n2.x)*(n1.x-n2.x) + (n1.y-n2.y)*(n1.y-n2.y));
		this.start = n1.id;
		this.end = n2.id;
	}
	
	void setLength(Nodes[] nodes){
		Nodes n1 = getNode(source,nodes);
		Nodes n2 = getNode(target,nodes);
		this.length =Math.sqrt((n1.x-n2.x)*(n1.x-n2.x) + (n1.y-n2.y)*(n1.y-n2.y));
	}
	
	void print(){
		System.out.printf("Source: %s, start: %d, Target: %s, end: %d, capacity: %f, length: %f\n", source,start,target,end,capacity,length);
	}
}
