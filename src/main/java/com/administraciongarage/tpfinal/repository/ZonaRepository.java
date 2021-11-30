package com.administraciongarage.tpfinal.repository;

import com.administraciongarage.tpfinal.HibernateUtil;
import com.administraciongarage.tpfinal.entity.Garage;
import com.administraciongarage.tpfinal.entity.Zona;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class ZonaRepository {

    public List<Zona> obtenerTodas() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery("from Zona");
        List<Zona> zonas = query.getResultList();

        // session.getTransaction().commit(); no hace falta para una consulta
        // commit() es para CXUD
        session.close();

        return zonas;
    }

    public void crear(Zona v) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        session.save(v);

        session.getTransaction().commit();
        session.close();
    }

    public void agregarGarage(Integer idZona, Garage g) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Zona z = (Zona) session.createQuery("from Zona where id = ?").setParameter(0, idZona).getSingleResult();
        z.getGarages().add(g);
        session.merge(z);
        session.getTransaction().commit();
        session.close();
    }

    public void modificar(Zona v) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        session.merge(v);
        session.getTransaction().commit();
        session.close();
    }

    public void eliminar(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Zona v = (Zona) session.createQuery("from Zona where id = ?").setParameter(0, id).getSingleResult();
        session.delete(v);

        session.getTransaction().commit();
        session.close();
    }

    public Zona buscar(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Zona v = (Zona) session.createQuery("from Zona where id = ?").setParameter(0, id).getSingleResult();

        session.close();
        return v;
    }

    public Zona buscarGarageId (Integer id){
        Session session = HibernateUtil.getSessionFactory().openSession();

        Zona v = (Zona) session.
                createNativeQuery("select z.* from zona z inner join garage g on (z.id=g.zona_id) where g.id = ?",Zona.class)
                .setParameter(1, id)
                .getResultStream().findFirst().orElse(null);

        session.close();
        return v;
    }

    public Zona buscarZonaPorLetra(String letra) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Zona v = (Zona) session.createQuery("from Zona z where z.letra = ?").setParameter(0, letra)
                .getResultStream().findFirst().orElse(null);

        session.close();
        return v;
    }

}
