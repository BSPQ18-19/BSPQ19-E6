package com.spq.group6.server.dao;

import com.spq.group6.server.utils.logger.ServerLogger;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import java.util.concurrent.locks.Lock;

public class UtilsDAO {

    private PersistenceManager pm;
    private Lock pmLock;

    public UtilsDAO(){

        pm = JdoManager.getPersistanceManager();
        pmLock = JdoManager.pmLock;
    }

    public void updateObject(Object obj) {
        pmLock.lock();

        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();

            pm.makePersistent(obj);
            tx.commit();
        } catch (Exception ex) {

            ServerLogger.logger.error("* Exception inserting/updating data into db: " + ex.getMessage());

        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
    }
}
