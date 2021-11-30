package com.administraciongarage.tpfinal.repository;

import com.administraciongarage.tpfinal.HibernateUtil;
import com.administraciongarage.tpfinal.entity.ZonaAsignada;
import com.administraciongarage.tpfinal.entity.Zona;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class ZonaAsignadaRepository {

    public List<Zona> obtenerZonas(Integer empleadoId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createNativeQuery("select z.* \n" +
                "from Zona z\n" +
                "where id not in (select zona_id from zonaasignada za where empleado_id = ?) ", Zona.class);
        List<Zona> zonas = query.setParameter(1,empleadoId).getResultList();

        session.close();
        return zonas;
    }

    public List<ZonaAsignada> obtenerZonasAsignadas(Integer empleadoId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createNativeQuery("select za.*\n" +
                "from zona z \n" +
                "inner join zonaasignada za \n" +
                "on z.id = za.zona_id \n" +
                "inner join empleado e \n" +
                "on za.empleado_id = e.id " +
                "where e.id = ?", ZonaAsignada.class);
        query.setParameter(1,empleadoId);
        List<ZonaAsignada> zonas = query.getResultList();

        session.close();
        return zonas;
    }

    public void crear(ZonaAsignada v) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        session.save(v);

        session.getTransaction().commit();
        session.close();
    }

    public void modificar(ZonaAsignada v) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        session.update(v);
        session.getTransaction().commit();
        session.close();
    }

    public void eliminar(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        ZonaAsignada v = (ZonaAsignada) session.createQuery("from ZonaAsignada za where za.id = ?").setParameter(0, id).getSingleResult();
        session.delete(v);

        session.getTransaction().commit();
        session.close();
    }

    public ZonaAsignada buscar(Integer idZona, Integer idEmpleado) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        ZonaAsignada v =  session.createNativeQuery("select * from zonaasignada za where za.empleado_id = ? and za.zona_id = ?",ZonaAsignada.class)
                .setParameter(1, idEmpleado)
                .setParameter(2, idZona)
                .getResultStream().findFirst().orElse(null);
        session.close();
        return v;
    }

    public List<Zona> buscarZonaConEmpleados(Integer zonaId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createNativeQuery("select z.*\n" +
                "from zona z \n" +
                "inner join zonaasignada za \n" +
                "on z.id = za.zona_id \n" +
                "inner join empleado e \n" +
                "on za.empleado_id = e.id " +
                "where z.id = ?", Zona.class);
        query.setParameter(1,zonaId);
        List<Zona> zonas = query.getResultList();

        session.close();
        return zonas;
    }

    public List<Zona> buscarZonasConGarages(Integer zonaId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createNativeQuery("select *\n" +
                "from zona z \n" +
                "inner join garage g \n" +
                "on z.id = g.zona_id \n" +
                "where g.zona_id = ?", Zona.class);
        query.setParameter(1,zonaId);
        List<Zona> zonas = query.getResultList();

        session.close();
        return zonas;
    }


}
