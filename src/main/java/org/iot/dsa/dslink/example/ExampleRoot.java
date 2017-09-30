package org.iot.dsa.dslink.example;

import org.iot.dsa.DSRuntime;
import org.iot.dsa.dslink.DSRootNode;
import org.iot.dsa.node.DSInfo;
import org.iot.dsa.node.DSInt;
import org.iot.dsa.node.action.ActionInvocation;
import org.iot.dsa.node.action.ActionResult;
import org.iot.dsa.node.action.DSAction;

/**
 * The root and only node of this link.
 *
 * @author Aaron Hansen
 */
public class ExampleRoot extends DSRootNode implements Runnable {

    ///////////////////////////////////////////////////////////////////////////
    // Constants
    ///////////////////////////////////////////////////////////////////////////

    private static String COUNTER = "Counter";
    private static String RESET = "Reset";

    ///////////////////////////////////////////////////////////////////////////
    // Fields
    ///////////////////////////////////////////////////////////////////////////

    private DSInfo counter = getInfo(COUNTER);
    private DSInfo reset = getInfo(RESET);
    private DSRuntime.Timer timer;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    protected void declareDefaults() {
        super.declareDefaults();
        declareDefault(COUNTER, DSInt.valueOf(0))
                .setTransient(true)
                .setReadOnly(true);
        declareDefault(RESET, DSAction.DEFAULT);
    }

    /**
     * Handles the reset action.
     *
     * <p>
     *
     * {@inheritDoc}
     */
    @Override
    public ActionResult onInvoke(DSInfo actionInfo, ActionInvocation invocation) {
        if (actionInfo == this.reset) {
            synchronized (this) {
                put(counter, DSInt.valueOf(0));
            }
            return null;
        }
        return super.onInvoke(actionInfo, invocation);
    }

    /**
     * Start the update timer.
     *
     * <p>
     *
     * {@inheritDoc}
     */
    @Override
    protected synchronized void onSubscribed() {
        timer = DSRuntime.run(this, System.currentTimeMillis() + 1000l, 1000l);
    }

    /**
     * Cancels an active timer if there is one.
     *
     * <p>
     *
     * {@inheritDoc}
     */
    @Override
    protected void onStopped() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * Cancel the update timer.
     *
     * <p>
     *
     * {@inheritDoc}
     */
    @Override
    protected synchronized void onUnsubscribed() {
        timer.cancel();
        timer = null;
    }

    /**
     * Called by the timer, increments the counter on a one second interval, only when this node is
     * subscribed.
     */
    @Override
    public synchronized void run() {
        DSInt value = (DSInt) counter.getValue();
        put(counter, DSInt.valueOf(value.toInt() + 1));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Inner Classes
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // Initialization
    ///////////////////////////////////////////////////////////////////////////

}
