package es.tubalcain.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import es.tubalcain.domain.Alumno;

class AlumnoRepositoryTest {
    
    private EntityManagerFactory emf;
    private EntityManager em;
    private AlumnoRepository repository;

    @BeforeEach
    void setUp() {
        // Inicializar EntityManagerFactory con la unidad de persistencia de test
        emf = Persistence.createEntityManagerFactory("gestionAlumnosTest");
        em = emf.createEntityManager();
        repository = new AlumnoRepository(em);
    }

    @Test
    void testSaveAlumno() {
        // Given
        Alumno alumno = new Alumno("Iñigo", "Vicente", "12345678A");
        alumno.setEmail("iñigo10@ejemplo.com");
        alumno.setNumeroExpediente("EXP001");

        // When
        em.getTransaction().begin();
        repository.save(alumno);
        em.getTransaction().commit();

        // Then
        assertNotNull(alumno.getId());
        Optional<Alumno> found = repository.findById(alumno.getId());
        assertTrue(found.isPresent());
        assertEquals("Iñigo", found.get().getNombre());
        assertEquals("12345678A", found.get().getDni());
    }

    @Test
    void testFindByDni() {
        // Given
        Alumno alumno = new Alumno("Marco", "Sangalli", "87654321B");
        em.getTransaction().begin();
        repository.save(alumno);
        em.getTransaction().commit();

        // When
        Optional<Alumno> found = repository.findByDni("87654321B");

        // Then
        assertTrue(found.isPresent());
        assertEquals("Marco", found.get().getNombre());
    }

    @Test
    void testUpdateAlumno() {
        // Given
        Alumno alumno = new Alumno("Pedro", "Munitis", "11111111C");
        em.getTransaction().begin();
        repository.save(alumno);
        em.getTransaction().commit();

        // When
        alumno.setEmail("pedro@ejemplo.com");
        em.getTransaction().begin();
        Alumno updated = repository.update(alumno);
        em.getTransaction().commit();

        // Then
        assertEquals("pedro@ejemplo.com", updated.getEmail());
        Optional<Alumno> found = repository.findById(alumno.getId());
        assertTrue(found.isPresent());
        assertEquals("pedro@ejemplo.com", found.get().getEmail());
    }

    @Test
    void testDeleteAlumno() {
        // Given
        Alumno alumno = new Alumno("Jeremy", "Arevalo", "22222222D");
        em.getTransaction().begin();
        repository.save(alumno);
        em.getTransaction().commit();
        Long id = alumno.getId();

        // When
        em.getTransaction().begin();
        repository.delete(alumno);
        em.getTransaction().commit();

        // Then
        Optional<Alumno> found = repository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        // Given
        em.getTransaction().begin();
        repository.save(new Alumno("Gonzalo", "Colsa", "33333333E"));
        repository.save(new Alumno("Óscar", "Serrano", "44444444F"));
        em.getTransaction().commit();

        // When
        List<Alumno> alumnos = repository.findAll();

        // Then
        assertFalse(alumnos.isEmpty());
        assertTrue(alumnos.size() >= 2);
    }

    @Test
    void testFindByNombreCompleto() {
        // Given
        Alumno alumno = new Alumno("Sergio", "Canales", "55555555G");
        em.getTransaction().begin();
        repository.save(alumno);
        em.getTransaction().commit();

        // When
        List<Alumno> found = repository.findByNombreCompleto("Sergio Canales");

        // Then
        assertFalse(found.isEmpty());
        assertTrue(found.stream().anyMatch(a -> a.getDni().equals("55555555G")));
    }
}