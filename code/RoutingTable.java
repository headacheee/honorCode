
public class RoutingTable {
	Path routing[][];
	
	public RoutingTable(int n){
		routing = new Path[n][n];
	}
	
	void linkUtilization(Traffic traffic,Network network,int n ){
		for(int i=0;i < n;i++){
			for(int j = 0;j<n;j++){
				double flow = traffic.trafficMatrix[i][j];
				if(flow > 0){
					int parent = -1;
					for(int k = 0 ; k<this.routing[i][j].path.size();k++){
						if(parent == -1){
							parent = this.routing[i][j].path.get(k);
						}else{
							int current = this.routing[i][j].path.get(k);
							network.link[parent][current].flow += flow;
							parent = current;
						}
					}
				}
			}
		}
	}

	void linkUtilizationSwitch(Traffic traffic,Network network,int n ){
		int switches[] = network.switches;
		for(int i=0;i < n;i++){
			for(int j = 0;j<n;j++){
				double flow = traffic.trafficMatrix[i][j];
				if(flow > 0){
					int parent = -1;
					for(int k = 0 ; k<this.routing[i][j].path.size();k++){
						if(parent == -1){
							parent = this.routing[i][j].path.get(k);
							int position = checkSwitch(parent,switches);
							if(position != -1){
								int sink = this.routing[i][j].path.get(this.routing[i][j].path.size()-1);
								network.sdnFlow[parent][sink] += flow;
								break;
							}
						}else{
							int current = this.routing[i][j].path.get(k);
							int position = checkSwitch(current,switches);
							network.link[parent][current].flow += flow;
							parent = current;
							if(position != -1){
								int sink = this.routing[i][j].path.get(this.routing[i][j].path.size()-1);
								if(sink != current)
									network.sdnFlow[current][sink] += flow;
								break;
							}
						}
					}
				}
			}
		}
	}
	
	int checkSwitch(int n, int[] switches){
		for(int i=0;i<switches.length;i++)
			if(switches[i] == n)
				return i;
		return -1;
	}
	
	void printPath(int n){
		for(int k =0; k<n;k++){
			for(int i = 0; i<n ;i++){       //print path
				for(int j = 0 ; j<this.routing[k][i].path.size();j++)
					System.out.print(this.routing[k][i].path.get(j)+" ");
				System.out.println();
			}
			System.out.println();
		}
	}
}
