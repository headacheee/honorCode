import java.util.Random;


public class Traffic {
	int p;
	int n;
	double trafficMatrix[][];
	
	Traffic(int n){
		trafficMatrix = new double[n][n];
		this.n = n;
		for(int i = 0; i<n; i++)
			for(int j = 0;j<n;j++)
				trafficMatrix[i][j] = 0;
	}
	
	void generateTraffic(){
		Random random = new Random();
		for(int i = 0; i<this.n;i++)
			for(int j = 0; j < this.n; j++){
				if(i!=j){
					this.trafficMatrix[i][j] = random.nextDouble()/2;
				}
			}
	}
	
	void setTraffic(){
		double matrix[] = {0,0,0.1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		for(int i = 0; i<this.n;i++)
			for(int j = 0; j < this.n; j++){
				this.trafficMatrix[i][j] = matrix[i*this.n+j];
			}
	}
	
	void setTrafficByTo(Topology to){
		this.p = to.p;
		for(int i = 0;i<to.p;i++){
			Demand demand = to.demand[i];
			this.trafficMatrix[demand.start][demand.end] = demand.demandValue/n;
		}
	}
	
	void setTraffic(int n){
	
		this.trafficMatrix[0][4] = 0.1;
		this.trafficMatrix[0][6] = 0.1;
		this.trafficMatrix[1][4] = 0.1;
		this.trafficMatrix[1][6] = 0.1;
	}
	
	void printTrafficFlow(){
		for(int i = 0;i<n;i++){        // print traffic flow
			for(int j = 0; j<n;j++)
				System.out.printf("%1.3f ",this.trafficMatrix[i][j]);
			System.out.println();
		}
	}
	
	void printTrafficFlowForSetting(){
		System.out.print("{");
		for(int i = 0;i<n;i++){        // print traffic flow for setting
			for(int j = 0; j<n;j++)
				System.out.printf("%1.3f,",this.trafficMatrix[i][j]);
		}
		System.out.print("0}");
		System.out.println();
	}
	
	void prinfTrafficInfo(){
		System.out.print("demands: "+p +"      ");
	}
}
