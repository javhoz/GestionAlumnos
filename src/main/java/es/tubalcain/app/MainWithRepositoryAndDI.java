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

/**
 * Ejemplo de inyección de dependencias manual.
 * La clase AppGestor recibe sus dependencias (repositorios y EntityManager)
 * a través del constructor, no las crea internamente.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Iniciando la aplicación de Gestión de Alumnos...");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ejemplopersistenciaJPA");
        EntityManager em = emf.createEntityManager();

        // Inyección manual de dependencias
        AlumnoRepository alumnoRepo = new AlumnoRepository(em);
        AppGestor gestor = new AppGestor(alumnoRepo, em);

        gestor.ejecutar();

        em.close();
        emf.close();
    }
}

/**
 * Clase de servicio que depende de AlumnoRepository y EntityManager,
 * inyectados mediante el constructor.
 */
class AppGestor {

    private final AlumnoRepository alumnoRepo;
    private final EntityManager em;

    public AppGestor(AlumnoRepository alumnoRepo, EntityManager em) {
        this.alumnoRepo = alumnoRepo;
        this.em = em;
    }

    public void ejecutar() {
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

            // Persistir usando el repositorio
            alumnoRepo.save(a1);
            alumnoRepo.save(a2);

            em.getTransaction().commit();

            // Consultas
            List<Alumno> activos = alumnoRepo.findActivos();
            System.out.println("\n--- Alumnos activos ---");
            activos.forEach(a -> System.out.println(a.getNombre() + " " + a.getApellidos()));

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
