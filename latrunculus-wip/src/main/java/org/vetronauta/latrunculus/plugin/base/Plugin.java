package org.vetronauta.latrunculus.plugin.base;

public interface Plugin {

    /**
     * Runs the Rubette.
     * This is the heart of the Rubette and implements the
     * actual computation. Input values are retrieved with the
     * getInput method and output values are stored using the
     * setOutput method. The runInfo parameter object has a method
     * stopped() which should be called regularly, and, in the case
     * it returns false, the run() method should exit gracefully.
     * @param runInfo contains information about the Runner that executes
     *        the network
     */
    void run(RunInfo runInfo);

}
