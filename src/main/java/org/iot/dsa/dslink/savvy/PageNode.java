package org.iot.dsa.dslink.savvy;

import org.iot.dsa.node.DSIObject;
import org.iot.dsa.node.DSNode;
import org.iot.dsa.node.DSString;

public class PageNode extends DSNode {
    
    protected SoupProxy proxy;
    protected String suffix;
    
    public PageNode() {
        
    }
    
    public PageNode(SoupProxy proxy, String suffix) {
        this.proxy = proxy;
        this.suffix = suffix;
    }
    
    @Override
    protected void onStarted() {
        super.onStarted();
        if (suffix == null) {
            DSIObject suffixobj = get("Suffix");
            if (suffixobj instanceof DSString) {
                suffix = suffixobj.toString();
            }
        }
        put("Suffix", DSString.valueOf(suffix));
    }
    
    @Override
    protected void onStable() {
        super.onStable();
        if (proxy == null) {
            DSNode parent = getParent();
            if (parent instanceof PageNode) {
                proxy = ((PageNode) parent).proxy;
            }
        }
        
        init();
    }
    
    public void init() {
        
    }

}
