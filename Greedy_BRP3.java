package Algo_3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Greedy_BRP3 {
	static HashMap<Integer,Integer> sorted = new HashMap<Integer,Integer>();
	static HashMap<Integer,Integer> satisfied = new HashMap<Integer,Integer>();
	static Greedy_BRP3 gr=new Greedy_BRP3();
	static int scr=0,des,scr_value,des_value;
	static int Vn=0, S, vd = 233, L = 0, nBikes=10, nBS = 0, nv=0, CAP=0,n,Dv = 0;											 
	static int navail_bikes=0;
	static int flag =0,temp1;
	static Set<Integer> Vx = new HashSet<Integer>();									//street intersection
	static Set<Integer> Vs = new HashSet<Integer>();									//bikestation
	static Set<Integer> V = new HashSet<Integer>();										//total nodes
	static Set<Integer> Vd = new HashSet<Integer>();									//depot
	static int flag1 = 0;
	static TreeMap<Integer,Integer> bikes = new TreeMap<Integer,Integer>();
	static int[] temp_des=new int[91];
	static int[] temp_des_value=new int[91];
	static int[] bike_source = new int[90];
	static int[] bike_s=new int[90];
	static int[][] adj=new int[234][234];
	static int vertexs=234;
	static int cost=0,time=0;
	static int final_cost=0;
	static int bike=0;
	static int vehicles=1;
	static int[] data=new int[90];
	static int noofcluster,cnt=0;
	static int centroid[][];
	static HashMap<Integer,HashMap<Integer,Integer>> cls=new HashMap<Integer,HashMap<Integer,Integer>>();
	static HashMap<Integer,Integer> Mcluster =new HashMap<Integer,Integer>();
	static HashMap<Integer,ArrayList<Integer>> finalCluster=new HashMap<Integer,ArrayList<Integer>>();

	public static void iniMain() {
		
		Scanner sc1=new Scanner(System.in);
		Scanner sc2=new Scanner(System.in);
		char[] ch = null;
		String[] con = null;
		
		try {
		    File file1 = new File("P:\\ALGO\\Project\\sample_data\\sample_data\\topology_10.txt"); 
			sc1 = new Scanner(file1);
			File file2 = new File("P:\\ALGO\\Project\\sample_data\\sample_data\\bike_data_10.txt"); 
			sc2 = new Scanner(file2);
			} 
		
		catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
		  
		 Vn=sc1.nextInt();
		 L = sc1.nextInt();
		 nBS = sc1.nextInt();
		 System.out.println("Vn="+Vn+" L="+L+" nBS="+nBS);
		 int[] temp = new int[nBS];
		 
		 // get bikestations
		 for(int i=0;i<nBS;i++) {
			 int k=sc1.nextInt();
			 Vs.add(k);
			 data[i]=k;
		 }

		 for(int i=0;i<Vn;i++) {
			V.add(i);
		 } 
		 
		 Vx.addAll(V);
		 Vx.removeAll(Vs);
		 Vx.remove(0);
		 Vd.add(0);
		 temp1=sc1.nextInt();
		 System.out.println("temp1="+temp1);
		 String[] values = new String[temp1];
		 sc1.nextLine();
		 for(int i=0;i<temp1;i++) {
		   values[i]=sc1.nextLine();
		   values[i]=values[i].replace(" ",",");
		   values[i]=values[i].replace("(","");
		   values[i]=values[i].replace(")","");
		   values[i]=values[i].replace(") (",",");
		   con=values[i].split(",");
		   int x=0;
		   for(int j=0;j<con.length;j++) {
			   if(flag==0) {
				   x=Integer.parseInt(con[j]);
				   j++;
				   flag=1;
			   }
			  int y=Integer.parseInt(con[j]);
			  j++;
			  adj[x][y]=Integer.parseInt(con[j]);
		   }
		   flag=0; 
		 }
		 navail_bikes=sc2.nextInt();
		 System.out.println("bike="+navail_bikes);
		
		 for(int i=0;i<nBS;i++) {
			bikes.put(sc2.nextInt(),sc2.nextInt());
		 }
		 System.out.println(bikes);
		 //Sorting the bikes according to their ascending order of value.
		 sorted = bikes.entrySet().stream().sorted(Map.Entry.comparingByValue())
		            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			
	}
	
	
	public int minDistance(int distance[], Boolean span_tree[]) {
		
		//get minDistanced node from a given graph
		int min_value=Integer.MAX_VALUE,min_node=-1;
		for(int i=0;i<vertexs;i++) {
			if(span_tree[i]==false && distance[i]<=min_value) {
				min_value=distance[i];
				min_node=i;
			}
		}
		return min_node;
	}
	
	public int dijkstra(int adj[][], int source,int des) {
		// to find the shortest path form a source node to destination node within a adj matrix
		int temp2=0;
		int distance[]=new int[vertexs];
		Boolean span_tree[]=new Boolean[vertexs];
		
		for(int i=0;i<vertexs;i++) {
			distance[i]=Integer.MAX_VALUE;
			span_tree[i]=false;
		}
		distance[source]=0;
		for(int i=0;i<vertexs-1;i++) {
			int u=minDistance(distance,span_tree);
			span_tree[u]=true;
			for(int v=0;v<vertexs;v++) {
				if(!span_tree[v] && adj[u][v]!=0 && distance[u]!=Integer.MAX_VALUE && distance[u]
						+ adj[u][v]<distance[v]) {
					distance[v]=distance[u]+adj[u][v];
				}
			}
			temp2=distance[des];
		}
		printGraph(distance,vertexs);
		return temp2;
	}
	
	public void printGraph(int distance[], int n) {
		// print the given connected graph
		int k=0;
		for(int i=0;i<vertexs;i++) {
			if(i>142) {
				temp_des[k]=i;
				temp_des_value[k]=distance[i];
				k++;
			}
		}
		
		int min=temp_des_value[0];
		int min_value=0;
		for(int i=0;i<temp_des_value.length;i++) {
			if(temp_des_value[i]<min) {
            min = temp_des_value[i];
            min_value=i;
			}
		}
		Arrays.sort(temp_des_value);
	}
	
	
	public static int getRandom(int min, int max){
		
		// get a Random value from given 2 values
	    int x = (int)(Math.random()*((max-min)+1))+min;
	    return x;
	}
	
	
	public static int[][] getCentroid(int data[],int noofcluster,int centroid[][],int flag){
		
		// this method will give clusters of given size
		System.out.println("no of cluster:"+noofcluster);
		int distance[][]=new int[noofcluster][data.length];
		int cluster[] =new int[data.length];
		int nodecnt[]=new int[noofcluster];
		
		
		System.out.println("..........new Cluster .........");
		
		// this loop is for finding the minimum distance form depot to all the other bikestation
			for(int j=0;j<data.length;j++) {
				distance[0][j]=gr.dijkstra(adj,233,data[j]);
				Mcluster.put(data[j],distance[0][j]);
			}
		System.out.println("Cluster="+Mcluster);
		
		System.out.println("............New Centroid..............");
		// to find a random centroid for 1st time 
		Set<Integer> tempp = new HashSet<Integer>();
		if(flag == 0) {
			for(int i=0;i<noofcluster;i++) {
				centroid[1][i]=Mcluster.get(getRandom(143,220));
				while(tempp.contains(centroid[1][i])) {
					centroid[1][i]=Mcluster.get(getRandom(143,220));
				}
				tempp.add(centroid[1][i]);
				//System.out.println("Centroid"+centroid[1][i]);
			}
			flag=1;
		}
		
		System.out.println("...........Creating temp Cluster.......");
		
		// creating a list of all the bikestation nodes difference the centroid 
		for(int i=0;i<noofcluster;i++) {
			HashMap<Integer,Integer> mpp=new HashMap<Integer,Integer>();
			for(int j : Mcluster.keySet()) {
				if(centroid[1][i]>Mcluster.get(j)) {
					int mp = centroid[1][i]-Mcluster.get(j);
					mpp.put(j,mp);
				}
				else {
					int mp = Mcluster.get(j)-centroid[1][i];
					mpp.put(j,mp);
				}
			}
			cls.put(i, mpp);
		}
		
		System.out.println("...........Creating Final Cluster.......");
		
		// getting a cluster from given centroid  
		for(int i=0;i<noofcluster;i++) {
			ArrayList<Integer> temp = new ArrayList<>();
			finalCluster.put(i,temp);
		}
		int smallestDistance;
		for(int j : Mcluster.keySet()) {
			int f = 0;
			smallestDistance=Integer.MAX_VALUE;
			for(int i=0;i<noofcluster;i++) {
				if(cls.get(i).get(j)<=smallestDistance) {
					smallestDistance=cls.get(i).get(j);
					f=i;
				}
			}
			finalCluster.get(f).add(j);
		}
		
		
		System.out.println("Final"+finalCluster);
		System.out.println("........new centroid......");
		
		//final cluster
		for(int i : finalCluster.keySet()) {
			int sum=0, avg=0, count=0;
			for(int j=0;j<finalCluster.get(i).size();j++) {
				int temp=finalCluster.get(i).get(j);
				sum=sum+Mcluster.get(temp);
				count++;
			}
			avg=(int)sum/count;
			centroid[0][i]=avg;
		}
		
		boolean finalcentroid=true;
		//if (i-1) and (i)th cluster have the same centroid then we stop the loop
		for(int i=0;i<noofcluster;i++) {
			if(finalcentroid && centroid[0][i]==centroid[1][i]) {
				finalcentroid=true;
				continue;
			}else {
				finalcentroid=false;
				System.out.println("Different");
				cnt++;
				break;
			}
		}
		
		if(!finalcentroid) { 
			//if (i-1) and (i)th cluster dont have the same centroid then we continue the loop iteration
			centroid[1] = Arrays.copyOf(centroid[0], centroid[0].length);
			getCentroid(data,noofcluster,centroid,flag);
		}
		
		if(finalcentroid) {
			System.out.println("Final Cluster: "+finalCluster);
			for(int i=0;i<5;i++) {
				System.out.print("Final Centroid: "+centroid[1][i]+" ");
			}
		}
		return centroid;
	}
	
	
	public static int estimateVehicles( int adj[][]) {
		
		// estimate how many vehicles are needed for the cluster
		HashMap<Integer,Integer> hp=new HashMap<Integer,Integer>();
		for(int i=143;i<233;i++) {
			int tem=gr.dijkstra(adj, 233,i );
			hp.put(i, tem);
		}
		//System.out.println("HP: "+hp);
		HashMap<Integer,Integer>sort = hp.entrySet().stream().sorted(Map.Entry.comparingByValue())
	            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		int noOfVehicles=1;
		//System.out.println("SORT: "+sort);
		cost=0;
		for(int i: sort.keySet()) {
		if(flag1==0) {
			scr=233;
			des=i;
			time=gr.dijkstra(adj, scr, des);
			cost=cost+time;
			scr=des;
			flag1=1;
			continue;
		}
		else {
			des=i;
			time=gr.dijkstra(adj, scr, des);
			int t=gr.dijkstra(adj, des, 233);
			cost=cost+time+t;
			//System.out.println("cost flag1: "+cost);
			if(cost>=480) {
				scr=233;
				des=i;
				noOfVehicles++;
				cost=0;
				//System.out.println("scr: "+scr+" des: "+des);
				//System.out.println("noOfVehicles: "+noOfVehicles);
				continue;
			}
			cost=cost-t;
			scr=des;
			
			}	
		}
		System.out.println("noOfVehicles: "+noOfVehicles);
		return noOfVehicles;
	}
	
	
	public static void main(String[] args) {
				
		iniMain();									
		long start_time=System.nanoTime();			//start time 
		ArrayList<Integer> visited1=new ArrayList<Integer>();			
		int return_cost=0;
		int[] kmp=new int[90];

			int temp_cost=0;
			int capacity=0;
			
			int veh=estimateVehicles(adj);
			
			for(int j=0;j<90;j++) {
				List<Entry<Integer,Integer>> source_list = new ArrayList<Map.Entry<Integer, Integer>>(sorted.entrySet());
				Entry<Integer, Integer> last = source_list.get(source_list.size()-1);	
				kmp[j]=last.getKey();
				sorted.remove(kmp[j]);
			}
			int flag = 0;
			centroid=new int[2][veh];
			getCentroid(kmp,veh,centroid,flag);
			System.out.println(centroid);
			
			ArrayList<Integer> ar=new ArrayList<Integer>();
			HashMap<Integer,Integer> hm = new HashMap<Integer,Integer>();
			
		for (Map.Entry<Integer, ArrayList<Integer>> entry : finalCluster.entrySet()) {
			hm.clear();
			ar.clear();
			ar.addAll(entry.getValue());
				
			//System.out.println("...............................First cluster: "+ar);	
			
			for(int j=0;j<ar.size();j++) {
				if(bikes.get(ar.get(j))!=5) 
				hm.put(ar.get(j), bikes.get(ar.get(j)));
			}
			HashMap<Integer,Integer> sorted1 = new HashMap<Integer,Integer>();
			sorted1 = hm.entrySet().stream().sorted(Map.Entry.comparingByValue())
		            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			//System.out.println(sorted1);
			int counter=0;
			while(!sorted1.isEmpty()) {
				List<Entry<Integer,Integer>> source_list1 = new ArrayList<Map.Entry<Integer, Integer>>(sorted1.entrySet());
				Entry<Integer,Integer> last1 = source_list1.get(source_list1.size()-1);
				int extra=0;
				// for first time to go from depot to the nearest node
				if(flag1==0) {
					scr=233;
					des=last1.getKey();
					time=gr.dijkstra(adj, scr, des);
					cost=cost+time;
					if(last1.getValue()>5) {
						extra=last1.getValue()-5;
						bike=bike+extra;
						counter=counter+extra;
					}
					scr=des;
					flag1=1;
					satisfied.put(last1.getKey(), last1.getValue());
					visited1.add(last1.getKey());
					sorted1.remove(last1.getKey());
					continue;
				}
				// going from all the nodes to satisfy the demand
				else {
					des=last1.getKey();
					time=gr.dijkstra(adj, scr, des);
					int t=gr.dijkstra(adj, des, 233);
					cost=cost+time+t;
					if(cost>=480) {
						scr=233;
						des=last1.getKey();
						vehicles++;
						cost=0;
					//System.out.println("Counter..................................................."+counter);
					//System.out.println("bikes visited:"+visited1.size());
						continue;
					}
					cost=cost-t;
					
					if(last1.getValue()>=5) {
						extra=last1.getValue()-5;
						bike=bike+extra;
						counter=counter+extra;
						satisfied.put(last1.getKey(), last1.getValue());
						visited1.add(last1.getKey());
						scr=des;
						hm.put(last1.getKey(),hm.get(last1.getKey())-extra);
					}
					
					else {
						extra=5-last1.getValue();
						bike=bike-extra;
						counter=counter+extra;
						if(bike<0) {
							//if no more bikes left than break loop.
							break;
						}
						else {
							hm.put(last1.getKey(),hm.get(last1.getKey())+extra);
							satisfied.put(last1.getKey(), last1.getValue());
							visited1.add(last1.getKey());
						}
						scr=des;
					}
				}
				sorted1.remove(last1.getKey());
			
			}
			System.out.println("Size of HM: "+hm.size()+" HM: "+hm);
			System.out.println("Size of Visited: "+visited1.size()+" Visted:"+visited1);
			System.out.println("Size of Satisfied: "+satisfied.size()+" Stsisfied: "+satisfied);
			System.out.println();
			System.out.println("Total vehicles="+vehicles);
		//   if vehicles = 0 will will get different vehicles for the each cluster otherwise total vehicles 
			//vehicles=0;
			hm.clear();
			visited1.clear();
			
				}
		
			
		long end_time=System.nanoTime();
		long duration=(end_time-start_time)/1000;
		System.out.println("Total Time (micro seconds): "+duration);
		}
		
		
		

}

