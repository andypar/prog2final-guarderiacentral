package com.administraciongarage.tpfinal.repository;

import com.administraciongarage.tpfinal.HibernateUtil;
import com.administraciongarage.tpfinal.entity.Empleado;
import com.administraciongarage.tpfinal.entity.ZonaAsignada;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class EmpleadoRepository {
    public List<Empleado> obtenerTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery("from Empleado");
        List<Empleado> empleados = query.getResultList();

        session.close();

        return empleados;
    }

    public void crear(Empleado v) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        session.save(v);

        session.getTransaction().commit();
        session.close();
    }

    public void modificar(Empleado v) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        session.update(v);
        session.getTransaction().commit();
        session.close();
    }

    public void eliminar(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Empleado v = (Empleado) session.createQuery("from Empleado e where e.id = ?").setParameter(0, id).getSingleResult();
        session.delete(v);

        session.getTransaction().commit();
        session.close();
    }

    public Empleado buscar(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Empleado v = (Empleado) session.createQuery("from Empleado e where e.id = ?").setParameter(0, id).getSingleResult();
        session.close();
        return v;
    }

    public void asignarZona(Integer idEmpleado, ZonaAsignada z) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Empleado e = (Empleado) session.createQuery("from Empleado e where e.id = ?").setParameter(0, idEmpleado).getSingleResult();
        e.getZonaEmpleado().add(z);
        session.merge(z);
        session.merge(e);
        session.getTransaction().commit();
        session.close();
    }

    public void listarZonasACargo(Integer idEmpleado, ZonaAsignada z) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Empleado e = (Empleado) session.createQuery("from Empleado e where e.id = ?").setParameter(0, idEmpleado).getSingleResult();
        e.getZonaEmpleado().remove(z);
        session.merge(e);
        session.getTransaction().commit();
        session.close();
    }

    public Empleado buscarEmpleadoPorCodigo(String codigo) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Empleado e = (Empleado) session.createQuery("from Empleado e where e.codigo = ?").setParameter(0, codigo)
                .getResultStream().findFirst().orElse(null);
        session.close();
        return e;
    }

}
