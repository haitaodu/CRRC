package com.smartflow.crrc.dao;

import com.smartflow.crrc.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class WorkpieceDaoImpl implements WorkpieceDao{
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public List<Workpiece> getWorkpieceListByPlantId(Integer plantId) {
        String jpql = "from Workpiece where plant_code in (select plant_code from Plant where id = "+ plantId +")";
        List<Workpiece> workpieceList = entityManager.createQuery(jpql).getResultList();
        return workpieceList;
    }

    @Override
    public Workpiece getWorkpiece(String workpieceid, String weldseamid) {
        String jpql = "from Workpiece where workpieceid = :workpieceid and weldseamid = :weldseamid";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("workpieceid", workpieceid);
        query.setParameter("weldseamid", weldseamid);
        query.setFirstResult(0);
        query.setMaxResults(1);
        return (Workpiece) query.getSingleResult();
    }

    @Override
    public List<Current> getCurrent(String workpieceid, String weldseamid) {
        String jpql = "from Current where workpieceid" +
                " = :workpieceid and weldseamid = :weldseamid and ltime >= curdate()";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("workpieceid", workpieceid);
        query.setParameter("weldseamid", weldseamid);
        return query.getResultList();
    }

    @Override
    public List<Voltage> getVoltage(String workpieceid, String weldseamid) {
        String jpql = "from Voltage where workpieceid = :workpieceid and weldseamid = :weldseamid and ltime >= curdate()";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("workpieceid", workpieceid);
        query.setParameter("weldseamid", weldseamid);
        return query.getResultList();
    }

    @Override
    public List<Sound> getSound(String workpieceid, String weldseamid) {
        String jpql = "from Sound where workpieceid = :workpieceid and weldseamid = :weldseamid and ltime >= curdate()";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("workpieceid", workpieceid);
        query.setParameter("weldseamid", weldseamid);
        return query.getResultList();
    }

    @Override
    public Image getImage(String workpieceid, String weldseamid) {
        String jpql = "from Image where workpieceid = :workpieceid and weldseamid = :weldseamid and ltime >= curdate() order by ltime desc";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("workpieceid", workpieceid);
        query.setParameter("weldseamid", weldseamid);
        query.setFirstResult(0);
        query.setMaxResults(1);
        return (Image) query.getSingleResult();
    }
}
