import java.util.Arrays;


public class AlgorithmAngle {
	int id;
	double score;
	
	AlgorithmAngle(){
	}
	
	AlgorithmAngle(int id, double score){
		this.id = id;
		this.score = score;
	}
	
	
	
	int[] best(Network network,double theta,int num){	
		int n = network.link.length;
		AlgorithmAngle[] aa = new AlgorithmAngle[n];
	
		
		for(int i = 0;i<n;i++){
			aa[i] = new AlgorithmAngle(i,score(network,i,theta));
		}
		
		
		sort(aa);
		int[] best = new int[num];
		for(int i=0;i<num;i++)
			best[i] = aa[i].id;
		
//		printBest(aa,num);
		
		return best;
		
		
	}
	
	public void printBest(AlgorithmAngle[] aa){
		for(int i=0;i<aa.length;i++){
			System.out.printf("best %d: %d , score : %f ; ",i,aa[i].id,aa[i].score);
		}
		System.out.println();
	}
	
	public void printBest(AlgorithmAngle[] aa,int n){
		for(int i=0;i<n;i++){
			System.out.printf("best %d: %d , score : %f ; ",i,aa[i].id,aa[i].score);
		}
		System.out.println();
	}
	
	double score(Network network,int u,double theta){
		int n = network.link.length;
		int out=0;
		double sum =0;
		double factor=0;
		
		
		for(int j=0;j<n;j++){
			if(network.link[u][j] != null && network.link[u][j].c>0){
				Link link = network.link[u][j];
				out+=1;
				factor += link.total/link.c;
			}
		}	
		sum = theta * out + (1.0-theta)*factor;
	//	System.out.println(out);

		return sum;
	}
	
	void sort(AlgorithmAngle []aa){
		for(int i = 0;i<aa.length;i++){
			for(int j = 0; j<aa.length-1;j++){
				if(aa[j].score<aa[j+1].score){
					AlgorithmAngle b = aa[j];
					aa[j] = aa[j+1];
					aa[j+1] = b;
				}
			}
		}
	}
}
