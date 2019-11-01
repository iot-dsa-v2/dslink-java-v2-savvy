package org.iot.dsa.dslink.example;

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

/**
 * The main and only node of this link.
 *
 * @author Aaron Hansen
 */
public class MainNode extends DSMainNode implements Runnable {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    private static String COUNTER = "Counter";
    private static String RESET = "Reset";

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    // Nodes store children and meta-data about the relationship in DSInfo instances.
    // Storing infos as Java fields eliminates subsequent name lookups, but should only be
    // done with declared defaults.  It can be done with dynamic children, but extra
    // care will be required.
    private final DSInfo<DSInt> counter = getInfo(COUNTER);
    private final DSInfo<DSIAction> reset = getInfo(RESET);
    private DSRuntime.Timer timer;

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

    /**
     * Handles the reset action.
     */
    @Override
    public ActionResults invoke(DSIActionRequest req) {
        if (req.getActionInfo() == reset) {
            synchronized (counter) {
                put(counter, DSInt.valueOf(0));
                // The following line would have also worked, but it would have
                // required a name lookup.
                // put(COUNTER, DSInt.valueOf(0));
            }
            return null;
        }
        return super.invoke(req);
    }

    /**
     * Called by the timer, increments the counter.
     */
    @Override
    public void run() {
        synchronized (counter) {
            put(counter, DSInt.valueOf(counter.get().toInt() + 1));
            // Without the counter field, this method would have required at least one lookup.
            // The following is the worst performance option (not that it really matters here):
            // DSInt val = (DSInt) get(COUNTER);
            // put(COUNTER, DSInt.valueOf(val.toInt() + 1));
        }
    }

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
        //Logger l = Logger.getLogger("transport");
        //l.setLevel(DSLogger.all);
        // Change the following URL to your README
        declareDefault("Help",
                       DSString.valueOf("https://github.com/iot-dsa-v2/dslink-java-v2-example"))
                .setTransient(true)
                .setReadOnly(true);
        declareDefault(COUNTER, DSInt.valueOf(0))
                .setAdmin(true)
                .setTransient(true)
                .setReadOnly(true);
        declareDefault("Writable", DSInt.valueOf(0));
        declareDefault("Date Time", DSDateTime.now()).setTransient(true);
        declareDefault(RESET, DSAction.DEFAULT);
        declareDefault("Values Action", new DSAction() {
            @Override
            public ActionResults invoke(DSIActionRequest request) {
                return makeResults(request, DSInt.valueOf(0), DSInt.valueOf(1));
            }

            {
                addColumnMetadata("first", DSInt.NULL);
                addColumnMetadata("second", DSInt.NULL);
                setResultsType(ResultsType.VALUES);
            }
        });
        declareDefault("Stream Action", new DSAction() {
            @Override
            public ActionResults invoke(DSIActionRequest request) {
                return makeResults(request, new DSIResultsCursor() {

                    private int count;
                    private DSDateTime next;
                    private DSRuntime.Timer update = DSRuntime.run(() -> update(),
                                                                   System.currentTimeMillis()
                                                                           + 1000,
                                                                   1000);

                    @Override
                    public int getColumnCount() {
                        return 1;
                    }

                    @Override
                    public void getColumnMetadata(int index, DSMap bucket) {
                        new DSMetadata(bucket).setName("timestamp").setType(DSDateTime.NULL);
                    }

                    @Override
                    public DSIValue getValue(int index) {
                        DSDateTime ret = next;
                        next = null;
                        return ret;
                    }

                    @Override
                    public boolean next() {
                        return next != null;
                    }

                    private void update() {
                        if (!request.isOpen() || (++count > 10)) {
                            update.cancel();
                            request.close();
                        } else {
                            next = DSDateTime.now();
                            request.sendResults();
                        }
                    }
                });
            }
        }.setResultsType(ResultsType.STREAM));
    }

    /**
     * Cancels an active timer if there is one.
     */
    @Override
    protected void onStopped() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * Starts the timer.
     */
    @Override
    protected void onSubscribed() {
        // Use DSRuntime for timers and its thread pool.
        timer = DSRuntime.run(this, System.currentTimeMillis() + 1000, 1000);
    }

    /**
     * Cancels the timer.
     */
    @Override
    protected void onUnsubscribed() {
        timer.cancel();
        timer = null;
    }

}
