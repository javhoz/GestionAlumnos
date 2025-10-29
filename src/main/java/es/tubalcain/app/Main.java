package es.tubalcain.app;

import es.tubalcain.domain.Alumno;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando la aplicación de Gestión de Alumnos...");
        
        // Crear EntityManagerFactory usando el nombre de la unidad de persistencia
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ejemplopersistenciaJPA");
        EntityManager em = emf.createEntityManager();
        
        try {
            // Iniciar transacción
            em.getTransaction().begin();
            
            // Crear algunos alumnos de ejemplo
            Alumno alumno1 = new Alumno("Juan", "García López", "12345678A");
            alumno1.setFechaNacimiento(LocalDate.of(2000, 1, 15));
            alumno1.setEmail("juan.garcia@email.com");
            alumno1.setTelefono("666111222");
            alumno1.setDireccion("Calle Mayor 1, Madrid");
            alumno1.setNumeroExpediente("EXP001");
            alumno1.setCursoActual(2);
            
            Alumno alumno2 = new Alumno("María", "López García", "87654321B");
            alumno2.setFechaNacimiento(LocalDate.of(2001, 5, 20));
            alumno2.setEmail("maria.lopez@email.com");
            alumno2.setTelefono("666333444");
            alumno2.setDireccion("Avenida Principal 23, Madrid");
            alumno2.setNumeroExpediente("EXP002");
            alumno2.setCursoActual(1);
            
            // Persistir los alumnos
            em.persist(alumno1);
            em.persist(alumno2);
            
            // Commit de la transacción
            em.getTransaction().commit();
            System.out.println("Alumnos creados correctamente.");
            
            // Consultar y mostrar los alumnos creados
            System.out.println("\nAlumnos en la base de datos:");
            em.createQuery("SELECT a FROM Alumno a", Alumno.class)
                .getResultList()
                .forEach(alumno -> System.out.println(
                    String.format("ID: %d, Nombre: %s %s, DNI: %s, Expediente: %s",
                        alumno.getId(),
                        alumno.getNombre(),
                        alumno.getApellidos(),
                        alumno.getDni(),
                        alumno.getNumeroExpediente())
                ));
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al crear los alumnos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerrar EntityManager y EntityManagerFactory
            em.close();
            emf.close();
            System.out.println("\nAplicación finalizada.");
        }
    }
}