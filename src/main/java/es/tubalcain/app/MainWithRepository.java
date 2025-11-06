package es.tubalcain.app;

import java.time.LocalDate;
import java.util.List;

import es.tubalcain.domain.Alumno;
import es.tubalcain.domain.Curso;
import es.tubalcain.domain.Modulo;
import es.tubalcain.repository.AlumnoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando la aplicación de Gestión de Alumnos...");

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionAlumnosPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        // Instanciamos el repositorio
        AlumnoRepository alumnoRepo = new AlumnoRepository(em);

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

            // Guardar alumnos usando el repositorio
            alumnoRepo.save(a1);
            alumnoRepo.save(a2);

            em.getTransaction().commit();

            // ----------- CONSULTAS USANDO EL REPOSITORIO ----------------

            System.out.println("\n--- Listado de alumnos activos ---");
            List<Alumno> activos = alumnoRepo.findActivos();
            activos.forEach(a -> System.out.println(a.getNombre() + " " + a.getApellidos()));

            System.out.println("\n--- Buscar por DNI '12345678A' ---");
            alumnoRepo.findByDni("12345678A")
                      .ifPresentOrElse(
                          a -> System.out.println("Encontrado: " + a.getNombre() + " (" + a.getNumeroExpediente() + ")"),
                          () -> System.out.println("No se encontró el alumno.")
                      );

            System.out.println("\n--- Buscar por nombre parcial 'José' ---");
            alumnoRepo.findByNombreCompleto("José").forEach(a -> 
                System.out.println("Coincidencia: " + a.getNombre() + " " + a.getApellidos())
            );

            System.out.println("\n--- Total de alumnos: " + alumnoRepo.countAll() + " ---");

            // Mostrar relaciones (solo para comprobación visual)
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
