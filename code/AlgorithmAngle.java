
public class AlgorithmAngle {
	
	int best(Network network,double theta){
		int n = network.link.length;
		double score = 0;
		int best = -1;
		
		for(int i = 0;i<n;i++){
			double a = score(network,i,theta);
			if(score < a){
				best = i;
				score = a;
			}
		}
		return best;
	}
	
	double score(Network network,int u,double theta){
		int n = network.link.length;
		int out=0;
		double sum =0;
		double factor=0;
		
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++){
				if(network.link[i][j] != null){
					Link link = network.link[i][j];
					out+=1;
					factor += link.c/(link.flow+link.sdnF);
				}
			}
		sum = theta * out + factor * (1.0-theta);

		return sum;
	}
}
