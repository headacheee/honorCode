
public class Demand {
	String source;
	String target;
	double demandValue;
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
		this.start = n1.id;
		this.end = n2.id;
	}
	
	void print(){
		System.out.printf("Source: %s, start: %d, Target: %s, end: %d, demand value: %f\n", source,start,target,end,demandValue);
	}
	
}
