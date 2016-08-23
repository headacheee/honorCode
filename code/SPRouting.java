import java.util.ArrayList;

public class SPRouting {
	
	ArrayList<Integer> list = new ArrayList<Integer>();
		
	public Path[] routing(Network network, int start,int n){
		double distance[] = new double[n];
		int parents[] = new int[n];
		for(int i = 0; i<n;i++){
			distance[i] = -1;
			parents[i] = -1;
		}
		distance[start] = 0;
		Path paths[] = new Path[n];
		for(int i = 0;i<n;i++){
			paths[i] = new Path();
		}
		

		while(relax(distance,paths,network,parents,n)){}

		
		return paths;
	}
	
	Boolean relax(double distance[],Path paths[], Network network,int parents[],int n){
		double close = -1;
		int node = -1;
		for(int i=0;i<n;i++){
			if(close == -1){
				if(distance[i] >= 0){
					close = distance[i];
					node = i;
				}
			}else{
				if(close > distance[i] && distance[i] >=0){
					close = distance[i];
					node = i;
				}
			}
		}
		
		if(close==-1)
			return false;
		
		
		
		for(int i = 0; i<n; i++){
			Link link = network.link[node][i];
			if(link != null){            //     link's capacity is larger than 0
				double d = distance[node] + link.l;
				if(d < distance[i] ||distance[i] == -1){
					distance[i]=d;
					parents[i] = node;
				}
			}
		}
		
		paths[node].distance = distance[node];
		int j = parents[node];
		if(j!= -1){
			for(int i = 0; i < paths[j].path.size();i++)
			paths[node].path.add(paths[j].path.get(i));
		}
		
		paths[node].path.add(node);
		distance[node] = -2;
		return true;
	}
}
