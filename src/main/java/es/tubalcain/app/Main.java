package es.tubalcain.app;

import java.time.LocalDate;
import es.tubalcain.domain.Alumno;
import es.tubalcain.domain.Curso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando la aplicación de Gestión de Alumnos...");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ejemplopersistenciaJPA");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Crear curso
            Curso curso1 = new Curso("2º D.A.M.", "Segundo curso de Desarrollo de Aplicaciones Multiplataforma");
            em.persist(curso1);

            // Crear alumnos y asociarlos al curso
            Alumno a1 = new Alumno("Juan", "Muñíz", "12345678A");
            a1.setFechaNacimiento(LocalDate.of(2000, 1, 15));
            a1.setEmail("juan@example.com");
            a1.setNumeroExpediente("EXP001");
            curso1.addAlumno(a1); // bidireccional: añade alumno y setea curso

            Alumno a2 = new Alumno("José Alberto", "López García", "87654321B");
            a2.setFechaNacimiento(LocalDate.of(1972, 5, 20));
            a2.setEmail("jose@example.com");
            a2.setNumeroExpediente("EXP002");
            curso1.addAlumno(a2);

            // Persistir todo (IMPORTANTE USAR EL CASCADE = ALL)
            em.persist(curso1);

            em.getTransaction().commit();

            // Mostrar datos
            System.out.println("\nCurso guardado: " + curso1.getNombre());
            curso1.getAlumnos().forEach(a ->
                System.out.println("- Alumno: " + a.getNombre() + " " + a.getApellidos())
            );

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
