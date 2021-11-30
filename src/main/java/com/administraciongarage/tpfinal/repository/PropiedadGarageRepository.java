package com.administraciongarage.tpfinal.repository;

import com.administraciongarage.tpfinal.HibernateUtil;
import com.administraciongarage.tpfinal.entity.PropiedadGarage;
import com.administraciongarage.tpfinal.entity.Garage;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class PropiedadGarageRepository {

    public List<Garage> obtenerLibres() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createNativeQuery("select g.* from Garage g where g.propiedad_garage_id is null ", Garage.class);
        List<Garage> garages = query.getResultList();

        session.close();
        return garages;
    }

    public List<Garage> obtenerPropios(Integer socioId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createNativeQuery("select g.* \n" +
                "from garage g \n" +
                "inner join propiedadgarage p \n" +
                "on g.propiedad_garage_id  = p.id \n" +
                "inner join socio s \n" +
                "on s.id = p.socio_id \n" +
                "WHERE s.id = ?", Garage.class);
        query.setParameter(1,socioId);
        List<Garage> garages = query.getResultList();

        session.close();
        return garages;
    }

    public void crear(PropiedadGarage v) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        session.save(v);

        session.getTransaction().commit();
        session.close();
    }

    public void modificar(PropiedadGarage v) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        session.update(v);
        session.getTransaction().commit();
        session.close();
    }

    public void eliminar(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        PropiedadGarage v = (PropiedadGarage) session.createQuery("from PropiedadGarage where id = ?").setParameter(0, id).getSingleResult();
        session.delete(v);

        session.getTransaction().commit();
        session.close();
    }

    public PropiedadGarage buscar(Integer idGarage, Integer idSocio) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        PropiedadGarage v =  session.createNativeQuery("select * from propiedadgarage p where p.socio_id = ? and p.garage_id = ?",PropiedadGarage.class)
                .setParameter(1, idSocio)
                .setParameter(2, idGarage)
                .getResultStream().findFirst().orElse(null);
        session.close();
        return v;
    }

    public List<Garage> buscarGarageId(Integer garageId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createNativeQuery("select g.* \n" +
                "from garage g \n" +
                "inner join propiedadgarage p \n" +
                "on g.propiedad_garage_id  = p.id \n" +
                "inner join socio s \n" +
                "on s.id = p.socio_id \n" +
                "WHERE g.id = ?", Garage.class);
        query.setParameter(1,garageId);
        List<Garage> garages = query.getResultList();

        session.close();
        return garages;
    }

}
