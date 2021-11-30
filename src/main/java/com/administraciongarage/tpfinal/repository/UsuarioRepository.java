package com.administraciongarage.tpfinal.repository;

import com.administraciongarage.tpfinal.HibernateUtil;
import com.administraciongarage.tpfinal.entity.Socio;
import com.administraciongarage.tpfinal.entity.Usuario;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;


@Repository
public class UsuarioRepository {

    public Usuario login(String user, String pwd) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Usuario v = (Usuario) session.createQuery("from Usuario u where u.user = ? and u.pwd = ?").setParameter(0, user).setParameter(1,pwd)
                .getResultStream().findFirst().orElse(null);
        session.close();
        return v;
    }

    public Usuario buscarUsuarioPorUser(String user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Usuario u = (Usuario) session.createQuery("from Usuario u where u.user = ?").setParameter(0, user)
                .getResultStream().findFirst().orElse(null);
        session.close();
        return u;
    }

    public Usuario buscarUsuarioPorDNI(Integer dni) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Usuario u = (Usuario) session.createQuery("from Usuario u where u.dni = ?").setParameter(0, dni)
                .getResultStream().findFirst().orElse(null);
        session.close();
        return u;
    }

}
