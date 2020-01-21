package org.iot.dsa.dslink.savvy;

import java.util.Base64;
import org.iot.dsa.DSRuntime;
import org.iot.dsa.dslink.Action.ResultsType;
import org.iot.dsa.dslink.ActionResults;
import org.iot.dsa.dslink.DSMainNode;
import org.iot.dsa.node.DSIValue;
import org.iot.dsa.node.DSInfo;
import org.iot.dsa.node.DSInt;
import org.iot.dsa.node.DSMap;
import org.iot.dsa.node.DSMetadata;
import org.iot.dsa.node.DSString;
import org.iot.dsa.node.action.DSAction;
import org.iot.dsa.node.action.DSIAction;
import org.iot.dsa.node.action.DSIActionRequest;
import org.iot.dsa.table.DSIResultsCursor;
import org.iot.dsa.time.DSDateTime;
import org.jsoup.Jsoup;

/**
 * The main and only node of this link.
 *
 * @author Aaron Hansen
 */
public class MainNode extends DSMainNode {

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    // Nodes must support the public no-arg constructor.  Technically this isn't required
    // since there are no other constructors...
    public MainNode() {
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    

    ///////////////////////////////////////////////////////////////////////////
    // Protected Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Defines the permanent children of this node type, their existence is guaranteed in all
     * instances.  This is only ever called once per, type per process.
     */
    @Override
    protected void declareDefaults() {
        super.declareDefaults();
        declareDefault("Add Device", makeAddDeviceAction());
    }

    private DSAction makeAddDeviceAction() {
        DSAction act = new DSAction() {
            @Override
            public ActionResults invoke(DSIActionRequest request) {
                ((MainNode) request.getTarget()).addDevice(request.getParameters());
                return null;
            }
        };
        act.addParameter("Name", DSString.EMPTY, null);
        act.addParameter("Address", DSString.EMPTY, "The IP or URL of the device");
        act.addParameter("Username", DSString.EMPTY, null);
        act.addParameter("Password", DSString.EMPTY, null);
        return act;
    }
    
    private void addDevice(DSMap parameters) {
        String name = parameters.getString("Name");
        String address = parameters.getString("Address");
        String user = parameters.getString("Username");
        String pass = parameters.getString("Password");
        
        SoupProxy proxy = new SoupProxy(address, user, pass);
        put(name, new MainMenuNode(proxy));
    }

}
