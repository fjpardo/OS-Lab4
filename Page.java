//package Lab4;

public class Page {

	private int pageNum;
    private int processNum;
    private int loadTime;
    private int residencyTime;
    
    
    boolean wasEvicted = false;

  
    public Page(int page, int process) {
        
    	pageNum = page;
        processNum = process;
        
    }
    
    public int getPageNum() {
    	
    	return pageNum;
    	
    }
    
    public int getprocessNum() {
    	
    	return processNum;
    	
    }
    
    public int getLoadTime() {
    	
    	return loadTime;
    	
    }
    
    public void setLoadTime(int i) {
    	
    	loadTime = i;
    	
    }
    
    public int getResidencyTime() {
    	
    	return residencyTime;
    	
    }
    
    public void updateResidencyTime(int i) {
    	
    	residencyTime += i;
    	
    }

	
}
