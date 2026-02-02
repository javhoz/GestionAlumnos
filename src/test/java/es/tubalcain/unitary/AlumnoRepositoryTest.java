package es.tubalcain.unitary;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import es.tubalcain.domain.Alumno;
import es.tubalcain.domain.User;
import es.tubalcain.repository.AlumnoRepository;

class AlumnoRepositoryTest {
    
    private EntityManagerFactory emf;
    private EntityManager em;
    private AlumnoRepository repository;

    @BeforeEach
    void setUp() {
        // Inicializar EntityManagerFactory con la unidad de persistencia de test
        emf = Persistence.createEntityManagerFactory("gestionAlumnosTestPersistenceUnit");
        em = emf.createEntityManager();
        repository = new AlumnoRepository(em);
    }

    @Test
    void testSaveAlumno() {
        // Given
        User user = new User("testuser", "password", "test@example.com");
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        
        Alumno alumno = new Alumno("Iñigo", "Vicente", "12345678A", "iñigo10@ejemplo.com");
        alumno.setEmail("iñigo10@ejemplo.com");
        alumno.setNumeroExpediente("EXP001");
        alumno.setUser(user);

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
        User user = new User("testuser2", "password", "test2@example.com");
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        
        Alumno alumno = new Alumno("Marco", "Sangalli", "87654321B", "marco10@ejemplo.com");
        alumno.setUser(user);
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
        User user = new User("testuser3", "password", "test3@example.com");
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        
        Alumno alumno = new Alumno("Pedro", "Munitis", "11111111C", "pedro10@ejemplo.com");
        alumno.setUser(user);
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
        User user = new User("testuser4", "password", "test4@example.com");
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        
        Alumno alumno = new Alumno("Jeremy", "Arevalo", "22222222D", "jeremy10@ejemplo.com");
        alumno.setUser(user);
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
        User user1 = new User("testuser5", "password", "test5@example.com");
        User user2 = new User("testuser6", "password", "test6@example.com");
        em.getTransaction().begin();
        em.persist(user1);
        em.persist(user2);
        em.getTransaction().commit();
        
        em.getTransaction().begin();
        Alumno alumno1 = new Alumno("Gonzalo", "Colsa", "33333333E", "gonzalo10@ejemplo.com");
        alumno1.setUser(user1);
        Alumno alumno2 = new Alumno("Óscar", "Serrano", "44444444F", "oscar10@ejemplo.com");
        alumno2.setUser(user2);
        repository.save(alumno1);
        repository.save(alumno2);
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
        User user = new User("testuser7", "password", "test7@example.com");
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        
        Alumno alumno = new Alumno("Sergio", "Canales", "55555555G", "sergio10@ejemplo.com");
        alumno.setUser(user);
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