package org.drools.brms.client.modeldriven.model;


/**
 * Represents first order logic like Or, Not, Exists.
 * 
 * @author Michael Neale
 */
public class CompositeFactPattern implements IPattern {

    /**
     * this will one of: [Not, Exist, Or]
     */
    public String type;
    public FactPattern[] patterns;
    
    
    public CompositeFactPattern(String type) {
        this.type = type;
    }
    
    public CompositeFactPattern() {}
    
    
    public void addFactPattern(FactPattern pat) {
        if (this.patterns == null) this.patterns = new FactPattern[0];
        
        FactPattern[] list = this.patterns;
        FactPattern[] newList = new FactPattern[list.length + 1];
        
        for ( int i = 0; i < list.length; i++ ) {
            newList[i] =  list[i];
        }
        newList[list.length] = pat; 
        
        this.patterns = newList;    
    }
    
}
