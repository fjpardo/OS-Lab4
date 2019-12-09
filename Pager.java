//package Lab4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Pager {
	
	private static final int QUANTUM = 3;
	
	//I am implementing the frame table by using an array of pages called ram
	static Page[] ram = null;
	static ArrayList<Process> processes = new ArrayList<Process>();
	
	static int machineSize;			
	static int pageSize;						
	static int sizeOfEachProcess;						
	static int jobMix;							
	static int numOfReferences;			
	static String algo;
	
	static Scanner scanner = null;
	static Scanner randomNums = null;
	
	//stack for lifo 
	static Stack<Page> lifo;
	
	//queue for lru
	static Queue<Page> lru;
	
	public static void main(String[] args) {
		
		try {	
			randomNums = new Scanner(new File("random-numbers.txt"));
       		 }

 
		catch (FileNotFoundException e) {
			
			System.out.println("make sure random-numbers.txt is in the directory");
			
		}
		
		machineSize = Integer.parseInt(args[0]);				
		pageSize = Integer.parseInt(args[1]);				
		sizeOfEachProcess = Integer.parseInt(args[2]);				
		jobMix = Integer.parseInt(args[3]);					
		numOfReferences = Integer.parseInt(args[4]);	
		algo = args[5];
		
		//here i initialize the size of the ram
		ram = new Page[machineSize/pageSize];
		
		int numOfPages = sizeOfEachProcess / pageSize;
		
		//here i am setting up the processes based on the job mix number
		if(jobMix == 1) {
			
			Process p1 = new Process(1, sizeOfEachProcess, numOfReferences, 1.0, 0, 0, numOfPages);
			processes.add(p1);
			
		}
		
		else if(jobMix == 2) {
			
			Process p1 = new Process(1, sizeOfEachProcess, numOfReferences, 1.0, 0, 0, numOfPages);
			Process p2 = new Process(2, sizeOfEachProcess, numOfReferences, 1.0, 0, 0, numOfPages);
			Process p3 = new Process(3, sizeOfEachProcess, numOfReferences, 1.0, 0, 0, numOfPages);
			Process p4 = new Process(4, sizeOfEachProcess, numOfReferences, 1.0, 0, 0, numOfPages);
			processes.add(p1);
			processes.add(p2);
			processes.add(p3);
			processes.add(p4);
			
		}
		
		else if(jobMix == 3) {
			
			Process p1 = new Process(1, sizeOfEachProcess, numOfReferences, 0, 0, 0, numOfPages);
			Process p2 = new Process(2, sizeOfEachProcess, numOfReferences, 0, 0, 0, numOfPages);
			Process p3 = new Process(3, sizeOfEachProcess, numOfReferences, 0, 0, 0, numOfPages);
			Process p4 = new Process(4, sizeOfEachProcess, numOfReferences, 0, 0, 0, numOfPages);
			processes.add(p1);
			processes.add(p2);
			processes.add(p3);
			processes.add(p4);
			
		}
			
		else if(jobMix == 4) {
			
			Process p1 = new Process(1, sizeOfEachProcess, numOfReferences, 0.75, 0.25, 0, numOfPages);
			Process p2 = new Process(2, sizeOfEachProcess, numOfReferences, 0.75, 0, 0.25, numOfPages);
			Process p3 = new Process(3, sizeOfEachProcess, numOfReferences, 0.75, 0.125, 0.125, numOfPages);
			Process p4 = new Process(4, sizeOfEachProcess, numOfReferences, 0.5, 0.125, 0.125, numOfPages);
			processes.add(p1);
			processes.add(p2);
			processes.add(p3);
			processes.add(p4);
			
		}
		
		else {
			
			System.out.println("Please provide a valid value for the job mix (Valid values are 1, 2, 3 ,4)");
			
		}
		
		printInput();
		run();
		printOutput();
						
	}
	
	public static void printInput() {
		
		System.out.println("The machine size is "+machineSize);
		System.out.println("The page size is "+pageSize);
		System.out.println("The process size is "+sizeOfEachProcess);
		System.out.println("The job mix number is "+jobMix);
		System.out.println("The number of references per process is "+numOfReferences);
		System.out.println("The replacement algorithm is "+algo);
		System.out.println();
		
	}
	
	public static void printOutput() {
		
		int totalFaults = 0;
		
		double overallresidency =0;
		int totalNumOfEvictions = 0;
		
		
		for(int i = 0; i < processes.size(); i++) {
			
			totalFaults += processes.get(i).getNumOfPageFaults();
			
			// if a process has no evictions, then print this. 
			if(processes.get(i).getAvgResidencyTime() == -1) {
				
				System.out.println("Process "+ processes.get(i).getProcessNum() +" had "+processes.get(i).getNumOfPageFaults()+ " faults. With no evictions, the average residence is undefined.");
				//System.out.println();
				
			}
			
			else {
			
				System.out.println("Process "+ processes.get(i).getProcessNum() +" had "+processes.get(i).getNumOfPageFaults()+ " faults and "+processes.get(i).getAvgResidencyTime()+" average residency.");
				overallresidency += processes.get(i).getAvgResidencyTime()* processes.get(i).getNumOfEvictions();
				totalNumOfEvictions += processes.get(i).getNumOfEvictions();
			}
				
		}
		
		System.out.println();
		
		System.out.println("The total number of faults is "+totalFaults+" and the overall average residency is "+overallresidency/totalNumOfEvictions+".");
		
	}
	
	public static void run() {
		
		lifo = new Stack<Page>();
		lru = new LinkedList<Page>();
		
		int time = 1;
		
		int totalNumOfReferences = processes.size()*numOfReferences;
		
		// here i am iterating through all the total references
		while(time <= totalNumOfReferences) {
			
			for(int i = 0; i < processes.size(); i++) {
			
				//iterating through the quantum, round robin quantum = 3. Also checking if the num of references per process is > 0. means that everytime we 
				//simulate one, we subtract it from the process
				for(int ref = 0; ref < QUANTUM && processes.get(i).getNumOfReferences() > 0; ref++) {
					
					int word;
					
					//if first ref of the process, set the word as described in the lab
					if(processes.get(i).nextWord == -1) {
						
						word = ((111*processes.get(i).getProcessNum() + sizeOfEachProcess) % sizeOfEachProcess);
						processes.get(i).nextWord = word;
						
					}
					else 
						word = ((processes.get(i).nextWord + sizeOfEachProcess) % sizeOfEachProcess);
					
					
						int pageNum = word / pageSize;
					
						//array list of each process' page list
						ArrayList<Page> pageList = processes.get(i).getPageList();
					
						Page current = null;
					
						//retrieve the current page from the page list of this process
						for(int k = 0; k < pageList.size(); k++) {
							if(pageList.get(k).getPageNum() == pageNum)
								current = pageList.get(k);
							
						
					}
					
					//check if the page we need is in the ram
					boolean contains = false;
					int hit = -1;
					
					for(int j = 0; j < ram.length; j++) {
						if(ram[j] == null)
							continue;
						
						if(ram[j].getPageNum() == pageNum && ram[j].getprocessNum()==current.getprocessNum() ) {
							contains = true;
							hit = j;
						}
					
					}
					
					// if the page is in the ram, then we just update the lru queue
					if(contains == true) {
						
						//System.out.println(processes.get(i).getProcessNum()+" references word "+ word + " (page "+pageNum+") at time "+time+": Hit in frame "+hit);
						
						if(algo.equalsIgnoreCase("lru")) {
						
							lru.remove(current);
							lru.add(current);
							
						}
						
						
					}
					
					//else if not in the ram
					else {
						
						boolean isEmpty = false;
						
						//check if there is an empty space in the ram
						for(int j = 0; j < ram.length; j++) 
							if(ram[j] == null) 
								isEmpty = true;
							
						//if there is an empty space, add it in 
						if(isEmpty == true) {
							
							addToRam(current, time, word);
							processes.get(i).increasePageFault(1);
							
						}
						
						//else you got to evict a page that is currently in the ram, so we call the evict method to figure out which page to swap out
						else {
							
							evict(current, time, word);
							processes.get(i).increasePageFault(1);
							
						}
						
					}
					
					
					// here i am setting the next ref for this process
					int r = Integer.parseInt(randomNums.next());
					
					//System.out.println("The random number is "+r);
					 
					double y = r / (Integer.MAX_VALUE + 1d);
					
					
					// do case 1
					if(y < processes.get(i).getA()) {
						
						processes.get(i).nextWord = (word + 1 + sizeOfEachProcess) % sizeOfEachProcess;
						
					}
					
					//do case 2
					else if(y < processes.get(i).getA() + processes.get(i).getB()) {
						
						processes.get(i).nextWord = (word - 5 + sizeOfEachProcess) % sizeOfEachProcess;
						
					}
					
					//do case 3
					else if(y < processes.get(i).getA() + processes.get(i).getB() + processes.get(i).getC()) {
						
						processes.get(i).nextWord = (word + 4 + sizeOfEachProcess) % sizeOfEachProcess;
						
					}
					
					// do case 4
					else {	
						int rr = Integer.parseInt(randomNums.next());
						
						//System.out.println("The random number is "+rr);
						
						processes.get(i).nextWord = (rr + sizeOfEachProcess) % sizeOfEachProcess;
						
						
					}
					//increase time
					//and decrease the num of ref needed to simulate within this process
					time++;
					processes.get(i).decreaseNumOfReferences(1);
					
					//if time passes totalNumOfRef return cause no need to cycle more
					if(time > totalNumOfReferences)
						return;
					
					
				}
			
			}
			
		}
		
	}
	
	//here i find an empty, highest numbered free frame to add p to
	public static void addToRam(Page p, int time, int word) {
		
		for(int i = ram.length -1; i >= 0; i--) {
			
			if(ram[i] == null) {
				
				ram[i] = p;
				
				if(algo.equalsIgnoreCase("lru")) 
				    lru.add(p);

				if(algo.equalsIgnoreCase("lifo")) 
				    lifo.add(p);


				//System.out.println(p.getprocessNum() + " references word "+word+" (page "+p.getPageNum()+") at time "+time+": Fault, using free frame "+i+".");
				p.setLoadTime(time);

				return;
                
           		}	
		}
	}
	
	public static void evict(Page p, int time, int word) {
		
		//bases eviction based on the replacement algorithm
		if(algo.equalsIgnoreCase("lru")) {
		
			//poll from lru and then remove that one below, and add p into its position and update the stats
			Page remove = lru.poll();
			
			for(int i = 0; i < ram.length; i++) {
				
				if(ram[i].getprocessNum() == remove.getprocessNum() && ram[i].getPageNum()==remove.getPageNum()) {
					
					int processN = ram[i].getprocessNum();
					
					processes.get(processN-1).increaseNumOfEvictions(1);
					
					//System.out.println("page faults of process " +processN+" faults "+processes.get(processN-1).getNumOfPageFaults());
					
					ram[i] = p;
					processes.get(processN-1).increaseAvgResidencyTime(time - remove.getLoadTime());
					
					p.setLoadTime(time);
                    
                    			lru.add(p);
                    
                    			//System.out.println(p.getprocessNum() + " references word " + word + " (page "+p.getPageNum()+") at time " + time + ": Fault, evicting page "+remove.getPageNum()+" of process "+remove.getprocessNum()+" from frame: " + i);
                    
                    			return;
					
				}
			
			}
			
		}
		
		else if(algo.equalsIgnoreCase("lifo")) {
			
			Page remove = lifo.pop();
			
			//pops from lifo and then remove that one below, and add p into its position and update the stats
			for(int i = 0; i < ram.length; i++) {
				
				if(ram[i].getprocessNum() == remove.getprocessNum() && ram[i].getPageNum()==remove.getPageNum()) {
					
					int processN = ram[i].getprocessNum();
					
					processes.get(processN-1).increaseNumOfEvictions(1);
					
					ram[i] = p;
					
					processes.get(processN-1).increaseAvgResidencyTime(time - remove.getLoadTime());
					
					p.setLoadTime(time);
                    
                    			lifo.add(p);
                    
                    			//System.out.println(p.getprocessNum() + " references word " + word + " (page "+p.getPageNum()+") at time " + time + ": Fault, evicting page "+remove.getPageNum()+" of process "+remove.getprocessNum()+" from frame: " + i);
                    
                    			return;
					
				}
				
			}
			
		}
		
		//chooses a random page to evict to make space for p
		else if(algo.equalsIgnoreCase("random")) {
			
			int randomNum = Integer.parseInt(randomNums.next());
			
			int random = randomNum % ram.length;
			
			//System.out.println("The random number is "+ randomNum);
			
            		Page pageToRemove = ram[random];
            
            		int processN = pageToRemove.getprocessNum();
            
            		processes.get(processN-1).increaseNumOfEvictions(1);
            
            
            		processes.get(processN-1).increaseAvgResidencyTime(time - pageToRemove.getLoadTime());
            
      
           		ram[random] = p;
            		p.setLoadTime(time);
            
            		//System.out.println(p.getprocessNum() + " references word " + word + " (page "+p.getPageNum()+") at time " + time + ": Fault, evicting page "+pageToRemove.getPageNum()+" of process "+pageToRemove.getprocessNum()+" from frame: " + random);
            
            		return;
			
		}
		
		else {
			//algo inputted is not a legal argument
			System.out.println("You have not provided a legal replacement algo. (Enter in lru, lifo, or random)");
			
		}
		
	}

}
