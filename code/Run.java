//http://sndlib.zib.de/home.action
//"abilene.xml" 12 15 132
//"atlanta.xml" 15 22 210
//"brain.xml" 161 332 14311
//"cost266.xml" 37 57 1332
//ju xian xing wu fa chu li overflow   modify/ deal with overflow/ find better algorithm  / start thesis

public class Run {
	public static void main(String[] args){
		String[] name = {"abilene.xml","atlanta.xml","brain.xml","cost266.xml","dfn-bwin.xml","dfn-gwin.xml","di-yuan.xml","france.xml","geant.xml"};
		int proportion[] = {100,30,10,6,4,3,2};
		for(int i=0;i<name.length;i++){
			System.out.printf("\n"+name[i] +"\n");
			for(int j=0;j<proportion.length;j++){
				start(proportion[j],name[i]);
			}
		}
	}
	
	public static void start(int sdnProportion, String fileName){
		long start = System.nanoTime();
		
		int n;
		
		Topology to = new Topology();
		to.setTopology(fileName);
		n = to.n;
		Network network = new Network(n); // create network

		
	//	System.out.println(aa.best(network,0.4));
		
		Traffic traffic = new Traffic(n);  //randomly generate flow
//		traffic.generateTraffic();
		traffic.setTrafficByTo(to);
		network.setMatrixByTopology(to);
		System.out.print("Switches: "+n/sdnProportion+",");
		network.printNetworkInf(traffic);
			
		
//		traffic.printTrafficFlow();
//		traffic.printTrafficFlowForSetting();
		
		SPRouting a = new SPRouting();
		RoutingTable rt = new RoutingTable(n);
		for(int i=0;i<n;i++)
			rt.routing[i] = a.routing(network,i,n);
		
//		rt.printPath(n);
		
		rt.linkUtilization(traffic, network, n);
		
		AlgorithmAngle aa = new AlgorithmAngle();
	//	int[] switches = {2};
		int[] switches = aa.best(network, 0.5,n/sdnProportion);
		network.setSwitches(switches);
//		network.setAll();
		
		rt.linkUtilizationSwitch(traffic, network, n);
		
//		network.printLinkFlow();

//		network.printSDNFlow();		
		
		
		
		
		Lambda l = new Lambda();
		l.setPartial(true);
		System.out.println(l.getLambda(Math.pow(1.0*n, 5)/300000, network, rt));
		
//		System.out.println(l.getLambdaAll(0.1,network,rt));
		
		long end = System.nanoTime();
//		System.out.println("Running "+(end - start) / 1000000000);

	}
	
	public void ms(boolean a){
		if(a)
			return;
		System.out.println("assertation failed");
		System.exit(0);
	}
}
