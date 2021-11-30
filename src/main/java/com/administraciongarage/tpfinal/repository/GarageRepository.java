package com.administraciongarage.tpfinal.repository;

import com.administraciongarage.tpfinal.HibernateUtil;
import com.administraciongarage.tpfinal.entity.Garage;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class GarageRepository {

    public List<Garage> obtenerTodas() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery("from Garage");
        List<Garage> Garages = query.getResultList();

        session.close();

        return Garages;
    }

    public void crear(Garage v) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        session.save(v);

        session.getTransaction().commit();
        session.close();
    }

    public void modificar(Garage v) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        session.update(v);
        session.getTransaction().commit();
        session.close();
    }

    public void eliminar(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Garage v = (Garage) session.createQuery("from Garage where id = ?").setParameter(0, id).getSingleResult();
        session.delete(v);

        session.getTransaction().commit();
        session.close();
    }

    public Garage buscar(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Garage v = (Garage) session.createQuery("from Garage where id = ?").setParameter(0, id).getSingleResult();
        session.close();
        return v;
    }

    public List<Garage> buscarGarageLibres (){
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Garage> garages = session.createNativeQuery("select g.* from garage g where g.vehiculo_id is null",Garage.class)
                .getResultList();

        session.close();
        return garages;
    }

    public Garage buscarVehiculoId (Integer id){
        Session session = HibernateUtil.getSessionFactory().openSession();

        Garage g = (Garage) session.
                createNativeQuery("select g.* from garage g where g.vehiculo_id = ?",Garage.class)
                .setParameter(1, id)
                .getResultStream().findFirst().orElse(null);

        session.close();
        return g;
    }

    public Garage buscarGaragePorNumero(Integer numero) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Garage v = (Garage) session.createQuery("from Garage g where g.numero = ?").setParameter(0, numero)
                .getResultStream().findFirst().orElse(null);
        session.close();
        return v;
    }

}
