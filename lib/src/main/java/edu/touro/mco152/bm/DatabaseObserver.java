package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;
import jakarta.persistence.EntityManager;
import edu.touro.mco152.bm.persist.EM;

public class DatabaseObserver implements IObserver{

    @Override
    public void update(DiskRun run) {
        EntityManager em = EM.getEntityManager();
        em.getTransaction().begin();
        em.persist(run);
        em.getTransaction().commit();
    }
}
