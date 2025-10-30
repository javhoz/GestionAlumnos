package es.tubalcain.app;

import java.time.LocalDate;

import es.tubalcain.domain.Alumno;
import es.tubalcain.repository.AlumnoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando la aplicación de Gestión de Alumnos...");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ejemplopersistenciaJPA");
        EntityManager em = emf.createEntityManager();

		AlumnoRepository repo = new AlumnoRepository(em);

        try {
            em.getTransaction().begin();

            // Crear algunos alumnos de ejemplo
			Alumno alumno1 = new Alumno("Juan", "Muñíz", "12345678A");
            alumno1.setFechaNacimiento(LocalDate.of(2000, 1, 15));
            alumno1.setEmail("juan.garcia@email.com");
            alumno1.setTelefono("666111222");
			alumno1.setDireccion("Calle Mayor 1, Argoños");
            alumno1.setNumeroExpediente("EXP001");
            alumno1.setCursoActual(2);

			Alumno alumno2 = new Alumno("José Alberto", "López García", "87654321B");
			alumno2.setFechaNacimiento(LocalDate.of(1972, 5, 20));
			alumno2.setEmail("jal@email.com");
            alumno2.setTelefono("666333444");
			alumno2.setDireccion("Avenida Principal 23, Gijón");
            alumno2.setNumeroExpediente("EXP002");
            alumno2.setCursoActual(1);

			// Guardar usando el repositorio
			repo.save(alumno1);
			repo.save(alumno2);

            em.getTransaction().commit();
			System.out.println("Alumnos creados correctamente.\n");

			// Consultar todos los alumnos
			System.out.println("Todos los alumnos:");
			repo.findAll().forEach(a -> System.out.println(
                    String.format("ID: %d, Nombre: %s %s, DNI: %s, Expediente: %s",
							a.getId(), a.getNombre(), a.getApellidos(), a.getDni(), a.getNumeroExpediente())));

			// Buscar por DNI
			repo.findByDni("12345678A").ifPresent(
					a -> System.out.println("\nEncontrado por DNI: " + a.getNombre() + " " + a.getApellidos()));

			// Buscar por curso
			System.out.println("\nAlumnos en curso 2:");
			repo.findByCursoActual(2).forEach(a -> System.out.println(a.getNombre()));

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
            System.out.println("\nAplicación finalizada.");
        }
    }
}
