import java.util.ArrayList;


public class Lambda {	
	
	boolean partial = true;
	
	public double getLambda(double epsilon,Network network,RoutingTable rt){
		double dL = 0;
		int n = network.n;
		int m = network.linkList.size();
		double l[][] = new double[n][n];
		double b[][] = new double[n][n];
		double delta = getDelta(epsilon,n,m);
		
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++){
				l[i][j] = 10;
				b[i][j] = 0;
			}
		for(int e = 0; e < m; e++){
			Link link = network.linkList.get(e);
			b[link.s][link.e] = link.c - link.flow;
			if(b[link.s][link.e] > 0 )
				l[link.s][link.e] = delta/b[link.s][link.e];
		}
		double Rsd[][] = new double[n][n];
		for(int i=0; i<n;i++)
			for(int j=0;j<n;j++)
				Rsd[i][j] = 0;
		
		//network.printFlow(b);
		
		
		/*   
		int a= SAPath(rt,2,1,l,network);   //test shortest path selection
		System.out.println(SAPath(rt,2,1,l,network));
		System.out.printf("%f %f %f %f",l[2][0],l[0][1],l[2][3],l[3][1]);
		*/
		int loopNumber = 0;
		int switches[] = network.switches;
		double incrementFlow[][] = new double[n][n];
		for(int i=0; i<n;i++)
			for(int j=0; j<n;j++)
				incrementFlow[i][j] = 0;
		
		while(dL<1){
			loopNumber++;
			double d[][] = new double[n][n];
			for(int j=0;j<n;j++)
				for(int k=0;k<n;k++)
					d[j][k] = network.sdnFlow[j][k];
			
			int flag = 0;
			for(int i=0;i<n;i++){
				if(checkU(switches,i,d)!=-1)
					flag = 1;
			}
			if(flag == 0){
				if(partial)
					return 0;
				return lambdaAll(network,b,0);
			}
			for(int i = 0;i<n;i++){
				
				int u = checkU(switches,i,d);   // i is the destination

				while(dL<1 && u!=-1){    //Dl < 1 and some d(u)>0
					
					Path[] paths =  SAPath1( rt, i, l, network, b);
//					Path path = SAPath(rt,u,i,l,network,b);
					Path path = paths[u];
					if(path == null){
						if(partial)
							return 0;
						return lambdaAll(network,b,0);
					}
					
//					path.printPath();
//					path.printMatrix(b);
//					network.printFlow(l);
//					network.printFlow(b);
					
					double c = c(b,path);
					
					double fu = fu(d[u][i],c);
								
					double Rho = rho(network,path,b,incrementFlow);
					
					if(Rho == 0 || Rho ==-1 || fu < 0){
						//System.out.print("Error Lambda Rho "+ Rho+", fu "+fu + ", c "+ c + ", switch "+u+", destination "+i);
						return 0;
						//System.exit(0);
					}
					 
					
					//double flow = fu/Rho;
					double flow = fu;
					
					d[u][i] -= flow;
					
					/*
					ms(d[u][i]>=0);
					
					if(d[u][i] < 0.00000001*network.sdnFlow[u][i])
						d[u][i] = 0;
					*/
					
//					System.out.println(d[u][i]);
					ms(d[u][i]>=0);
					
					Rsd[u][i] += flow;
					
					setSDNF(path,flow,network,incrementFlow);
					
					setl1(path,flow,l,epsilon,incrementFlow,b);
					
					dL = recompute(b,l);
					
					u = checkU(switches,i,d);
				}			
				
			}		
			// shortest admissible path
			
//			System.out.println("Dl: "+ dL);

			
		}
//		System.out.println("Loop : "+ loopNumber);
		
		
		
//		*/
		

		double lambda = lambda(network,Rsd)/maxU(b,incrementFlow);
		if(partial)
			return lambda;
		return lambdaAll(network,b,lambda);

	}
	
	double avgU(double b[][], double incF[][]){
		double a= 0;
		double c = 0;
		for(int i=0; i<b.length;i++)
			for(int j=0;j<b.length;j++){
				if(b[i][j] > 0){
					a+=incF[i][j];
					c += b[i][j];
				}
			}
		return a/c;
	}
	
	int checkU(int switches[],int n,double[][] d){
		int flag = -1;
		for(int i = 0; i<switches.length; i++){
			if(d[switches[i]][n] > 0){
				flag = i;
				break;
			}
		}
		if(flag == -1)
			return -1;
		return switches[flag];
	}
	
	double maxU(double b[][], double incF[][]){
		double maxU = -1;
		for(int i=0; i<b.length;i++)
			for(int j=0;j<b.length;j++){
				if(b[i][j] > 0){
					double a = incF[i][j] / b[i][j];
					if(maxU < a)
						maxU = a;
				}
			}
		return maxU;
	}
	
	void printU(double b[][], double incF[][]){
		double maxU = -1;
		for(int i=0; i<b.length;i++)
			for(int j=0;j<b.length;j++){
				if(b[i][j] > 0){
					double a = incF[i][j] / b[i][j];
					System.out.printf("link [%d][%d] utilization: %f\n",i,j,a);
					if(maxU < a)
						maxU = a;
				}
			}
	}
	
	double rho(Network network,Path path,double[][] b,double incrementFlow[][]){
		if(path.path.size()==1)
			return 0;
		int parent = -1;
		double Rho = -1;
		for(int i=0;i<path.path.size();i++){
			if(parent == -1){
				parent = path.path.get(i);				
			}else{
				int current = path.path.get(i);
				if(Rho < incrementFlow[parent][current]/b[parent][current])
					Rho = incrementFlow[parent][current]/b[parent][current];
				parent = current;
			}
		}
		if(Rho < 1)
			Rho = 1;
		return Rho;
	}
	
	double c(double b[][],Path path){
		if(path.path.size()==1)
			return 0;
		int parent = -1;
		double c = -1;
		for(int i=0;i<path.path.size();i++){
			if(parent == -1){
				parent = path.path.get(i);				
			}else{
				int current = path.path.get(i);
				if(c > b[parent][current]||c == -1)
					c = b[parent][current];
				parent = current;
			}
		}
		return c;
	}
	
	double getDelta(double epsilon, double n ,double m){
		//return (1.0 + epsilon)/Math.pow((1.0+epsilon)*m,(1.0/epsilon));
		//return Math.pow((1.0-epsilon)/m,1.0/epsilon)/Math.pow((1.0+epsilon*n),(1.0-epsilon)/epsilon);
		return Math.pow(10, -40);
	}
	
	Path SAPath(RoutingTable rt, int u,int d,double l[][],Network network,double b[][]){
		Link[][] link = network.link;
		int n = network.n;
		int surround = -1;
		double shortPathLen = -1;
		//Path[] path = SPstart(l,network,b,d);
		
		for(int i = 0;i<n;i++){
			double pathL = pathLength(rt.routing[i][d],l,b);
			if(link[u][i]!=null&& rt.routing[i][d].path.size()!=0 &&pathL >= 0){  //	
				double pathLen;
				if(i==d){
					pathLen = pathL;
				}else{
					pathLen = pathL + l[u][i];
				}
		//		System.out.printf("%d,%d\n",i,d);
		//		rt.routing[i][d].printPath();
				if(shortPathLen == -1){
					shortPathLen = pathLen;
					surround = i;
				}else{
					if(pathLen < shortPathLen){
						shortPathLen = pathLen;
						surround = i;
					}
				}
			}
		}
		
		if(surround == -1){
			System.out.println("No link to sink: "+d);
			return null;
		}
		
		Path path = new Path();
		path.path.add(u);
		for(int i=0;i<rt.routing[surround][d].path.size();i++)
			path.path.add(rt.routing[surround][d].path.get(i));
		
		path.setDistance(l);
		return path;
	}
	
	Path[] SAPath1(RoutingTable rt,int d,double l[][],Network network,double b[][]){

		int[] switches = network.switches;
		int n = network.n;
		int[] child = new int[n];
		for(int i=0;i<n;i++){
			child[i]=-2;
		}
		child[d] = -1;
		
		Path[] paths = new Path[n];
		for(int i = 0; i<n;i++){   //copy paths
			paths[i] = new Path();
			
			Path origin = rt.routing[i][d];
			for(int j = 0; j<origin.path.size();j++)
				paths[i].path.add(origin.path.get(j));
			paths[i].setDistance(l);

		}


		for(int i=0;i<n;i++){    //sort paths by distance
			for(int j=0;j<n-1;j++){
				if(paths[j].distance>paths[j+1].distance){
					Path a = paths[j];
					paths[j] = paths[j+1];
					paths[j+1] = a;
				}
			}
		}
		

		for(int i=1;i<n;i++){
			int source = paths[i].path.get(0);
			if(isSwitch(source,switches)){
				Link[][] link = network.link;
				int surround = -1;
				double shortPathLen = -1;
				//Path[] path = SPstart(l,network,b,d);
				
				for(int j = 0;j<n;j++){
					int id = paths[j].path.get(0);
					if(link[source][id]!=null && b[source][id] >0){
						double pathL = paths[j].distance;//pathLength(rt.routing[j][d],l,b);
						paths[j].setDistance(l);
						if( (paths[j].path.size()!=0||id==d) &&pathL >= 0){  //	
							if(paths[j].path.size()>1)
								if(paths[j].path.get(1)==source)
									continue;
							double pathLen;
							if(id==d){
								pathLen = pathL;
							}else{
								pathLen = pathL + l[source][id];
							}
					//		System.out.printf("%d,%d\n",i,d);
					//		rt.routing[i][d].printPath();
							if(shortPathLen == -1){
								shortPathLen = pathLen;
								surround = id;
							}else{
								if(pathLen < shortPathLen){
									shortPathLen = pathLen;
									surround = id;
								}
							}
						}
					}
				}

				if(surround == -1){
					System.out.println("No link to sink: "+d);
					return null;
				}

				
				child[source] = surround;
				
			}else{
				child[source] = paths[i].path.get(1);
			}
			
			paths[i] = pathByChild(child,source,paths,i);
			if(paths[i] == null){
				 
			}
			paths[i].setDistance(l);
		}
		
		Path pathSub[] = new Path[n];
		for(int i=0;i<n;i++){
			int source = paths[i].path.get(0);
			pathSub[source] = paths[i];
		}
		return pathSub;
	}
	
	boolean isSwitch(int s, int[] switches){
		for(int i=0;i<switches.length;i++){
			if(switches[i]==s){
				return true;
			}
		}
		return false;
	}
	
	Path pathByChild(int[] child, int source,Path[] paths,int id){
		int next = source;
		Path path = new Path();
		int i=0;
		while(next != -1){
			i++;
			path.path.add(next);
			if(child[next]==-2){
				
				return paths[id];
			
			}
			if(i>child.length){
				/*
				System.out.println(next+" "+child[next]+" source:"+ source);
				for(int j=0;j<paths.length;j++)
					paths[j].printPath();
				for(int j=0;j<child.length;j++)
					System.out.print(child[j]+" ");
					*/
				return paths[id];
//				System.out.println("path By Child error");
//				System.exit(0);
			}
			next = child[next];
		}
		
		return path;
	}
	
	Path SAPathForFindRoute(RoutingTable rt, int u,int d,double l[][],Network network,double b[][]){
		Link[][] link = network.link;
		int n = network.n;
		int surround = -1;
		double shortPathLen = -1;
		Path[] path = SPstart(l,network,b,d);
		for(int i = 0;i<n;i++){
			if(link[u][i]!=null && path[i].distance != -1){  //
				double pathLen = path[i].distance + l[u][i];
				if(shortPathLen == -1){
					shortPathLen = pathLen;
					surround = i;
				}else{
					if(pathLen < shortPathLen){
						shortPathLen = pathLen;
						surround = i;
					}
				}
			}
		}
		
		if(surround == -1){
			System.out.println("No link to sink: "+d);
			return null;
		}
		path[surround].path.add(0,u);
		path[surround].distance += l[u][surround];
		return path[surround];
	}
	
	Path[] SPstart(double l[][],Network network,double b[][],int end){
		int n = network.n;
		double distance[] = new double[n];
		int parents[] = new int[n];
		for(int i = 0; i<n;i++){
			distance[i] = -1;
			parents[i] = -1;
		}
		distance[end] = 0;
		Path paths[] = new Path[n];
		for(int i = 0;i<n;i++){
			paths[i] = new Path();
		}

		while(Erelax(distance,paths,l,b,parents,n)){}

		
		return paths;
	}
	
	boolean Erelax(double distance[],Path paths[], double l[][], double b[][],int parents[],int n){
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
			if(b[i][node] >0 && l[i][node]>0 &&distance[i] != -2){            //     link's capacity is larger than 0
				double d = distance[node] + l[i][node];
				if(d < distance[i] ||distance[i] == -1){
					distance[i]=d;
					parents[i] = node;
				}
			}
		}
		
		paths[node].distance = distance[node];
	//	System.out.println(distance[node]+" "+node);
		paths[node].path.add(node);
		int j = parents[node];
		if(j!= -1){
			for(int i = 0; i < paths[j].path.size();i++)
			paths[node].path.add(paths[j].path.get(i));
		}

		distance[node] = -2;
		return true;
	}
	
	double pathLength(Path path, double l[][],double b[][]){
		if(path == null){
			System.out.println("pathLength in Lambda, path is null");
			System.exit(0);
		}
		
		if(path.path.size() == 1)
			return 0;
				
		int parent = -1;
		double length = 0;
		for(int i=0;i<path.path.size();i++){
			if(parent == -1){
				parent = path.path.get(i);				
			}else{
				int current = path.path.get(i);
				if(b[parent][current] <= 0)
					return -1;
				length += l[parent][current];
				parent = current;
			}
		}
		return length;
	}
		
	double pathLength(Path path, double l[][]){
		if(path == null){
			System.out.println("pathLength in Lambda, path is null");
			System.exit(0);
		}
		
		if(path.path.size() == 1)
			return 0;
				
		int parent = -1;
		double length = 0;
		for(int i=0;i<path.path.size();i++){
			if(parent == -1){
				parent = path.path.get(i);				
			}else{
				int current = path.path.get(i);
				length += l[parent][current];
				parent = current;
			}
		}
		return length;
	}
		
	double fu(double d, double c){
		if(d > c)
			return c;
		return d;
	}
	
	void setl(Path path, double flow,double l[][],double epsilon,double incF[][] ,double b[][]){
		if(path.path.size()==1)
			return;
		int parent = -1;
		for(int i=0;i<path.path.size();i++){
			if(parent == -1){
				parent = path.path.get(i);				
			}else{
				int current = path.path.get(i);
				l[parent][current]*=(incF[parent][current]*epsilon/b[parent][current] + 1.0);
				parent = current;
			}
		}
		return;
	}
	
	void setl1(Path path, double flow,double l[][],double epsilon,double incF[][] ,double b[][]){
		if(path.path.size()==1)
			return;
		int parent = -1;
		for(int i=0;i<path.path.size();i++){
			if(parent == -1){
				parent = path.path.get(i);				
			}else{
				int current = path.path.get(i);
				l[parent][current] *= Math.pow((1+epsilon), flow/b[parent][current]);
				parent = current;
			}
		}
		return;
	}
	
	void setl2(Path path, double flow,double l[][],double epsilon,double incF[][] ,double b[][],Network network){
		if(path.path.size()==1)
			return;
		int parent = -1;
		for(int i=0;i<path.path.size();i++){
			if(parent == -1){
				parent = path.path.get(i);				
			}else{
				int current = path.path.get(i);
				l[parent][current] *= Math.pow((1+epsilon), flow*network.link[parent][current].c/b[parent][current]/b[parent][current]);
				parent = current;
			}
		}
		return;
	}
	
	double recompute(double b[][],double l[][]){
		double sum = 0;
		for(int i=0;i<b.length;i++)
			for(int j = 0;j<b.length;j++){
				if(b[i][j]>0 && l[i][j]>0)
					sum += b[i][j]*l[i][j];
			}
				
		return sum;
	}
	
	double[][] lMatrix1(double[][] l, double[][] incF, double[][] b){
		int n = l.length;
		double lM[][] = new double[n][n];
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++){
				if(b[i][j]>0)
					lM[i][j] = incF[i][j]/b[i][j];
			}
		return lM;
	} 
	
	double[][] lMatrix(double[][] l, double[][] incF, double[][] b){
		int n = l.length;
		double lM[][] = new double[n][n];
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++){
				if(b[i][j]>0)
					if(incF[i][j]/b[i][j]>1){
						lM[i][j] = l[i][j]*incF[i][j]/b[i][j];
					}else{
						lM[i][j] = l[i][j];
					}
			}
		return lM;
	} 
	
	void setSDNF(Path path, double flow, Network network,double incF[][]){
		if(path.path.size()==1)
			return;
		int parent = -1;
		for(int i=0;i<path.path.size();i++){
			if(parent == -1){
				parent = path.path.get(i);				
			}else{
				int current = path.path.get(i);
				incF[parent][current] += flow;
				parent = current;
			}
		}
		return;
	}
	
	double lambda(Network network, double[][] Rsd){
		double lambda = -1;
		for(int i=0;i<network.n;i++)
			for(int j=0; j<network.switches.length;j++){
				if( network.sdnFlow[network.switches[j]][i]==0)
					continue;
				double a = Rsd[network.switches[j]][i] / network.sdnFlow[network.switches[j]][i];
				if(lambda == -1){
					lambda = a;
				}else{
					if(lambda > a)
						lambda = a;
				}	
			}	

		return lambda;
	}
	
	double lambdaAll(Network network,double b[][],double lambda){

		double c = -1;
		
		for(int e = 0; e < network.linkList.size(); e++){
			Link link = network.linkList.get(e);
			double a;
			if(b[link.s][link.e] > 0 && link.c>0 && lambda > 0){
				a = (b[link.s][link.e]/lambda + link.flow) / link.c;
			}else{
				a = -1;
				if(link.c>0)
					a = link.flow / link.c;
			}
			if(c == -1){
				c = a;
			}else{
				if(c < a&&a!= -1)
					c = a;
			}
		}
		return c;
	}
	
	public void ms(boolean a){
		if(a)
			return;
		System.out.println("assertation failed");
		System.exit(0);
	}
	
	public void testAll(Network network){
		int n = network.n;
		int[] switches = new int[n];
		for(int i = 0;i<n;i++){
			switches[i]=i;
		}
		network.setSwitches(switches);
	}
	
	public double getLambdaAll(double epsilon,Network network,RoutingTable rt){
		
		double dL = 0;
		int n = network.n;
		int m = network.linkList.size();
		double l[][] = new double[n][n];
		double b[][] = new double[n][n];
		double delta = getDelta(epsilon,n,m);
		
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++){
				l[i][j] = 10;
				b[i][j] = 0;
			}
		for(int e = 0; e < m; e++){
			Link link = network.linkList.get(e);
			b[link.s][link.e] = link.c - link.flow;
			if(b[link.s][link.e] > 0 )
				l[link.s][link.e] = delta/b[link.s][link.e];
		}
		double Rsd[][] = new double[n][n];
		for(int i=0; i<n;i++)
			for(int j=0;j<n;j++)
				Rsd[i][j] = 0;
		
		//network.printFlow(b);
		
		
		/*   
		int a= SAPath(rt,2,1,l,network);   //test shortest path selection
		System.out.println(SAPath(rt,2,1,l,network));
		System.out.printf("%f %f %f %f",l[2][0],l[0][1],l[2][3],l[3][1]);
		*/
		int loopNumber = 0;
		int switches[] = network.switches;
		double incrementFlow[][] = new double[n][n];
		for(int i=0; i<n;i++)
			for(int j=0; j<n;j++)
				incrementFlow[i][j] = 0;

		while(dL<1){
			loopNumber++;
			double d[][] = new double[n][n];
			for(int j=0;j<n;j++)
				for(int k=0;k<n;k++)
					d[j][k] = network.sdnFlow[j][k];
			
			int flag = 0;
			for(int i=0;i<n;i++){
				if(checkU(switches,i,d)!=-1)
					flag = 1;
			}
			if(flag == 0){
				if(partial)
					return 0;
				return lambdaAll(network,b,0);
			}
			for(int i = 0;i<n;i++){
				
				int u = checkU(switches,i,d);
				while(dL<1 && u!=-1){    //Dl < 1 and some d(u)>0
					
					Path path = SAPathAll(rt,u,i,l,network,b);
					
					
//					path.printPath();
//					path.printMatrix(b);
//					network.printFlow(l);
//					network.printFlow(b);
					
					double c = c(b,path);
					
					double fu = fu(d[u][i],c);
								
					double Rho = rho(network,path,b,incrementFlow);
					
					if(Rho == 0 || Rho ==-1 || fu < 0){
						System.out.print("Error Lambda Rho "+ Rho+", fu "+fu + ", c "+ c + ", switch "+u+", destination "+i);
						System.exit(0);
					}
					 
					
					//double flow = fu/Rho;
					double flow = fu;
					
					d[u][i] -= flow;
					
					/*
					ms(d[u][i]>=0);
					
					if(d[u][i] < 0.00000001*network.sdnFlow[u][i])
						d[u][i] = 0;
					*/
					
//					System.out.println(d[u][i]);
					ms(d[u][i]>=0);
					
					Rsd[u][i] += flow;
					
					setSDNF(path,flow,network,incrementFlow);
					
					//setl(path,flow,l,epsilon,incrementFlow,b);
					setl2(path,flow,l,epsilon,incrementFlow,b,network);
					
					dL = recompute(b,l);
					
					u = checkU(switches,i,d);
				}			
				
			}		
			// shortest admissible path
			
			System.out.println("Dl: "+ dL);
					
		}
		
		
		
//		*/


		double lambda = lambda(network,Rsd)/maxU(b,incrementFlow);
		if(partial)
			return lambda;
		return lambdaAll(network,b,lambda);

	}
	
	Path SAPathAll(RoutingTable rt, int u,int d,double l[][],Network network,double b[][]){
		Path[] path = SPstart(l,network,b,d);
		

		
		path[u].setDistance(l);
		return path[u];
	}
	
	public void setPartial(boolean partial){
		this.partial = partial;
	}
}
