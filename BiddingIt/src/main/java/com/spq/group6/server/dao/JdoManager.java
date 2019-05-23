package com.spq.group6.server.dao;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Singleton for restricting access to the JDO.
 * Contains the PersistenceManager & a Lock for accesing it.
 * Needed for concurrent ClientMain's requests.
 * This class is used by {@link BiddingDAO} for limiting access to a single
 * Thread at the same time.
 */
class JdoManager {
    static Lock pmLock = new ReentrantLock(); /*Lock for limiting accesses to the database*/
    private static PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
    private static PersistenceManager pm;

    /**
     * Static method for retrieving JdoManager singleton instance.
     *
     * @return JdoManager singleton instance.
     */
    static synchronized PersistenceManager getPersistanceManager() {
        if (pm == null) {
            pm = pmf.getPersistenceManager();
            pm.setDetachAllOnCommit(true);
            pm.getFetchPlan().setMaxFetchDepth(3);
        }
        return pm;
    }
}
