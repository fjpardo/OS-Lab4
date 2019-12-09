//package Lab4;

import java.util.ArrayList;

public class Process {

	private int processNum;
	private int size;
	private int numOfReferences;
	private double a;
	private double b;
	private double c;
	private int numOfPages;
	
	double runningSum = 0.0;
	
	private ArrayList<Page> pages;
	
	
	private int numOfPageFaults = 0;
	private int numOfEvictions = 0;
	
	//i mostly use this as the running sum
	private double avgResidencyTime = 0.0;
	
	//if -1, then it means it is the first ref in the process
	int nextWord = -1;
	

	public Process(int processNum, int size, int numOfReferences, double a, double b, double c, int numOfPages) {
		
		this.processNum = processNum;
		this.size = size;
		this.numOfReferences = numOfReferences;
		this.a = a;
		this.b = b;
		this.c = c;
		this.numOfPages = numOfPages;
		
		pages = new ArrayList<Page>();
		
		//adds the processes' pages into an arraylist
		for(int i = 0; i < numOfPages; i++) {
			
			pages.add(new Page(i, processNum));
			
			
		}
		
		
	}
	
	public int getProcessNum() {
		
		return processNum;
		
	}
	
	public ArrayList<Page> getPageList(){
		
		return pages;
		
	}
	
	public int getSize() {
		
		return size;
		
	}
	
	public int getNumOfReferences() {
		
		return numOfReferences;
		
	}
	
	public int getNumOfEvictions() {
		
		return numOfEvictions;
		
	}	
	
	public void increaseNumOfEvictions(int i) {
		
		numOfEvictions += i;
		
	}
	
	public void decreaseNumOfReferences(int i) {
		
		numOfReferences-= i;
		
	}
	
	public double getA() {
		
		return a;
		
	}
	
	public double getB() {
		
		return b;
		
	}

	public double getC() {
	
		return c;
	
	}
	
	public int getNumOfPageFaults() {
		
		return numOfPageFaults;
		
	}
	
	
	public void increaseAvgResidencyTime(int i ) {
		
		
		avgResidencyTime += i;
		
		
	}
	
	
	
	public double getAvgResidencyTime() {

		if(numOfEvictions == 0)
			return -1;
		
		//returns the avg residency time by dividing the sum of time/num of evictions
		return avgResidencyTime/numOfEvictions;
		
	}
	
	public void increasePageFault(int i) {
		
		numOfPageFaults += i;
		
	}
	

}
