package com.administraciongarage.tpfinal.repository;

import com.administraciongarage.tpfinal.HibernateUtil;
import com.administraciongarage.tpfinal.entity.Garage;
import com.administraciongarage.tpfinal.entity.Vehiculo;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class VehiculoRepository {
    public List<Vehiculo> obtenerTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery("from Vehiculo");
        List<Vehiculo> vehiculos = query.getResultList();

        session.close();

        return vehiculos;

    }

    public void crear(Vehiculo v) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        session.save(v);

        session.getTransaction().commit();
        session.close();
    }

    public void modificar(Vehiculo v) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Garage g = v.getGarage();
        g.setVehiculo(null);
        session.merge(g);

        session.update(v);
        session.getTransaction().commit();
        session.close();
    }

    public void eliminar(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Vehiculo v = (Vehiculo) session.createQuery("from Vehiculo where id = ?").setParameter(0, id).getSingleResult();
        Garage g = v.getGarage();
        g.setVehiculo(null);
        session.merge(g);
        session.delete(v);

        session.getTransaction().commit();
        session.close();
    }

    public Vehiculo buscar(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Vehiculo v = (Vehiculo) session.createQuery("from Vehiculo where id = ?").setParameter(0, id).getSingleResult();
        session.close();
        return v;
    }

    public void agregarGarage(Integer idVehiculo, Garage g) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Vehiculo v = (Vehiculo) session.createQuery("from Vehiculo where id = ?").setParameter(0, idVehiculo).getSingleResult();
        v.setGarage(g);
        g.setVehiculo(v);
        session.merge(v);
        session.merge(g);
        session.getTransaction().commit();
        session.close();
    }

    public List<Vehiculo> obtenerPropios(Integer socioId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createNativeQuery("select v.*\n" +
                "from socio s \n" +
                "inner join usuario u \n" +
                "on s.id = u.id \n" +
                "inner join vehiculo v \n" +
                "on s.id = v.socio_id \n" +
                "where s.id = ?", Vehiculo.class);
        query.setParameter(1,socioId);
        List<Vehiculo> vehiculos = query.getResultList();

        session.close();
        return vehiculos;
    }

    public Vehiculo buscarVehiculPorMatricula(String matricula) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Vehiculo v = (Vehiculo) session.createQuery("from Vehiculo v where v.matricula = ?").setParameter(0, matricula)
                .getResultStream().findFirst().orElse(null);
        session.close();
        return v;
    }

    public Vehiculo buscarGarageId (Integer id){
        Session session = HibernateUtil.getSessionFactory().openSession();

        Vehiculo v = (Vehiculo) session.
                createNativeQuery("\n" +
                        "select v.* from vehiculo v where v.garage_id = ?",Vehiculo.class)
                .setParameter(1, id)
                .getResultStream().findFirst().orElse(null);

        session.close();
        return v;
    }

}
