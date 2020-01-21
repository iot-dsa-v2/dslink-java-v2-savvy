package org.iot.dsa.dslink.savvy;

public class MainMenuNode extends PageNode {
    
    public MainMenuNode() {
    }
    
    public MainMenuNode(SoupProxy proxy) {
        super(proxy, "/mainmenu.htm");
    }
    
    @Override
    protected void declareDefaults() {
        super.declareDefaults();
        
    }
    
    @Override
    protected void onStarted() {
        super.onStarted();
        if (proxy == null) {
            String address = get("Address").toString();
            String username = get("Username").toString();
            String password = get("Password").toString();
            proxy = new SoupProxy(address, username, password);
        } else {
            put("Address", proxy.address);
            put("Username", proxy.username);
            put("Password", proxy.password);
        }
    }
    
    @Override
    public void init() {
        put("Zones", new ZonesNode(proxy));
        put("Alarms", new AlarmsNode(proxy));
        put("Histories", new HistoriesNode(proxy));
        put("Schedules", new SchedulesNode(proxy));
    }

}
