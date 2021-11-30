package com.administraciongarage.tpfinal.repository;

import com.administraciongarage.tpfinal.HibernateUtil;
import com.administraciongarage.tpfinal.entity.*;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class SocioRepository {
    public List<Socio> obtenerTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery("from Socio");
        List<Socio> socios = query.getResultList();

        session.close();

        return socios;

    }

    public void crear(Socio v) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        session.save(v);

        session.getTransaction().commit();
        session.close();
    }

    public void modificar(Socio v) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        session.update(v);
        session.getTransaction().commit();
        session.close();
    }

    public void eliminar(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Socio v = (Socio) session.createQuery("from Socio s where s.id = ?").setParameter(0, id).getSingleResult();
        session.delete(v);

        session.getTransaction().commit();
        session.close();
    }

    public Socio buscar(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Socio v = (Socio) session.createQuery("from Socio s where s.id = ?").setParameter(0, id).getSingleResult();
        session.close();
        return v;
    }

    public void agregarPropiedad(Integer idSocio, PropiedadGarage p) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Socio s = (Socio) session.createQuery("from Socio s where s.id = ?").setParameter(0, idSocio).getSingleResult();
        s.getPropiedades().add(p);
        session.merge(s);
        session.getTransaction().commit();
        session.close();
    }

    public void eliminarPropiedad(Integer idSocio, PropiedadGarage p) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Socio s = (Socio) session.createQuery("from Socio s where s.id = ?").setParameter(0, idSocio).getSingleResult();
        s.getPropiedades().remove(p);
        session.merge(s);
        session.getTransaction().commit();
        session.close();
    }

    public void agregarVehiculo(Integer idSocio, Vehiculo v) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Socio s = (Socio) session.createQuery("from Socio s where s.id = ?").setParameter(0, idSocio).getSingleResult();
        s.getVehiculos().add(v);
        session.merge(s);
        session.getTransaction().commit();
        session.close();
    }

    public Socio buscarSocioId (Integer id){
        Session session = HibernateUtil.getSessionFactory().openSession();

        Socio s = (Socio) session.
                createNativeQuery("select u.*, s.*\n" +
                        "from socio s \n" +
                        "inner join usuario u \n" +
                        "on s.id = u.id \n" +
                        "left join vehiculo v \n" +
                        "on s.id = v.socio_id \n" +
                        "where v.id = ?",Socio.class)
                .setParameter(1, id)
                .getResultStream().findFirst().orElse(null);

        session.close();
        return s;
    }

}
