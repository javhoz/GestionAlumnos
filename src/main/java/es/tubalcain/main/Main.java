package es.tubalcain.main;

import java.time.LocalDate;

import es.tubalcain.domain.Alumno;
import es.tubalcain.domain.Curso;
import es.tubalcain.domain.Modulo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando la aplicación de Gestión de Alumnos...");

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionAlumnosPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Crear curso
            Curso curso1 = new Curso("2º D.A.M.", "Segundo curso de Desarrollo de Aplicaciones Multiplataforma");
            em.persist(curso1);

            // Crear módulos
            Modulo redes = new Modulo("Redes", "Instalación y mantenimiento de redes");
            Modulo sistemas = new Modulo("Sistemas Operativos", "Gestión de sistemas operativos");
            em.persist(redes);
            em.persist(sistemas);

            // Crear alumnos
            Alumno a1 = new Alumno("Juan", "Muñíz", "12345678A");
            a1.setFechaNacimiento(LocalDate.of(2000, 1, 15));
            a1.setEmail("juan@example.com");
            a1.setNumeroExpediente("EXP001");
            curso1.addAlumno(a1);

            Alumno a2 = new Alumno("José Alberto", "López García", "87654321B");
            a2.setFechaNacimiento(LocalDate.of(1972, 5, 20));
            a2.setEmail("jose@example.com");
            a2.setNumeroExpediente("EXP002");
            curso1.addAlumno(a2);

            // Asignar módulos a los alumnos (ManyToMany)
            a1.addModulo(redes);
            a1.addModulo(sistemas);
            a2.addModulo(redes);

            // Persistir curso (en cascada) y módulos
            em.persist(curso1);
            em.persist(redes);
            em.persist(sistemas);

            em.getTransaction().commit();

            // Mostrar relaciones
            System.out.println("\nMódulos de " + a1.getNombre() + ":");
            a1.getModulos().forEach(m -> System.out.println("- " + m.getNombre()));

            System.out.println("\nAlumnos matriculados en " + redes.getNombre() + ":");
            redes.getAlumnos().forEach(a -> System.out.println("- " + a.getNombre()));

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
            System.out.println("\nAplicación finalizada.");
        }
    }
}
