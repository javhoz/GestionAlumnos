package es.tubalcain.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import es.tubalcain.domain.Alumno;
import es.tubalcain.domain.Curso;
import es.tubalcain.domain.Modulo;
import es.tubalcain.repository.AlumnoRepository;

public class GestionAlumnosIntegrationTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private AlumnoRepository alumnoRepository;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("gestionAlumnosTest");
        em = emf.createEntityManager();
        alumnoRepository = new AlumnoRepository(em);
    }

    @AfterEach
    void tearDown() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void testCompleteIntegrationFlow() {
        // 1. Crear y persistir un curso
        em.getTransaction().begin();
        
        Curso curso = new Curso("1º D.A.M.", "Primer curso de Desarrollo de Aplicaciones Multiplataforma");
        em.persist(curso);

        // 2. Crear y persistir módulos
        Modulo programacion = new Modulo("Programación", "Fundamentos de programación");
        Modulo baseDatos = new Modulo("Bases de Datos", "Diseño y gestión de bases de datos");
        em.persist(programacion);
        em.persist(baseDatos);

        // 3. Crear alumnos y asignarlos al curso
        Alumno alumno1 = new Alumno("Dan", "Smith", "11122233A");
        alumno1.setFechaNacimiento(LocalDate.of(1999, 3, 15));
        alumno1.setEmail("k7@ejemplo.com");
        alumno1.setNumeroExpediente("EXP2025-001");
        curso.addAlumno(alumno1);

        Alumno alumno2 = new Alumno("Sumio", "Kodai", "44455566B");
        alumno2.setFechaNacimiento(LocalDate.of(1999, 7, 22));
        alumno2.setEmail("hcu1@24thward.com");
        alumno2.setNumeroExpediente("EXP2025-002");
        curso.addAlumno(alumno2);

        // 4. Asignar módulos a los alumnos
        alumno1.addModulo(programacion);
        alumno1.addModulo(baseDatos);
        alumno2.addModulo(programacion);

        // Persistir todo
        em.persist(alumno1);
        em.persist(alumno2);
        
        em.getTransaction().commit();

        // 5. Verificar las relaciones y consultas
        
        // Verificar alumnos en el curso
        TypedQuery<Alumno> queryAlumnosCurso = em.createQuery(
            "SELECT a FROM Alumno a WHERE a.curso = :curso", Alumno.class);
        queryAlumnosCurso.setParameter("curso", curso);
        List<Alumno> alumnosEnCurso = queryAlumnosCurso.getResultList();
        assertEquals(2, alumnosEnCurso.size());

        // Verificar módulos de un alumno
        em.refresh(alumno1);
        assertEquals(2, alumno1.getModulos().size());
        assertTrue(alumno1.getModulos().contains(programacion));
        assertTrue(alumno1.getModulos().contains(baseDatos));

        // Verificar alumnos en un módulo
        em.refresh(programacion);
        assertEquals(2, programacion.getAlumnos().size());
        assertTrue(programacion.getAlumnos().contains(alumno1));
        assertTrue(programacion.getAlumnos().contains(alumno2));

        // 6. Probar funcionalidad del repositorio
        Optional<Alumno> foundAlumno = alumnoRepository.findByDni("11122233A");
        assertTrue(foundAlumno.isPresent());
        assertEquals("Dan", foundAlumno.get().getNombre());

        // 7. Probar actualización de datos
        em.getTransaction().begin();
        alumno1.setEmail("fsr@ejemplo.com");
        Alumno updatedAlumno = alumnoRepository.update(alumno1);
        em.getTransaction().commit();

        assertEquals("fsr@ejemplo.com", updatedAlumno.getEmail());

        // 8. Verificar búsqueda por nombre completo
        List<Alumno> foundByNombre = alumnoRepository.findByNombreCompleto("Dan Smith");
        assertFalse(foundByNombre.isEmpty());
        assertEquals("11122233A", foundByNombre.get(0).getDni());

        // 9. Probar eliminación de un alumno
        em.getTransaction().begin();
        alumnoRepository.delete(alumno2);
        em.getTransaction().commit();

        // Verificar que el alumno fue eliminado
        Optional<Alumno> deletedAlumno = alumnoRepository.findByDni("44455566B");
        assertFalse(deletedAlumno.isPresent());

        // Verificar que el módulo ya no tiene al alumno eliminado
        em.refresh(programacion);
        assertFalse(programacion.getAlumnos().contains(alumno2));
    }

    @Test
    void testCascadeOperations() {
        em.getTransaction().begin();

        // Crear estructura completa
        Curso curso = new Curso("2º D.A.M.", "Segundo curso de DAM");
        Modulo modulo = new Modulo("Acceso a Datos", "Persistencia y bases de datos");
        Alumno alumno = new Alumno("Vivi", "Ornitier", "77788899C");
        
        curso.addAlumno(alumno);
        alumno.addModulo(modulo);
        
        // Solo persistir el curso - debería cascadear
        em.persist(curso);
        em.getTransaction().commit();
        
        // Limpiar el contexto de persistencia
        em.clear();
        
        // Verificar que todo se persistió correctamente
        Curso foundCurso = em.find(Curso.class, curso.getId());
        assertNotNull(foundCurso);
        
        assertFalse(foundCurso.getAlumnos().isEmpty());
        Alumno foundAlumno = foundCurso.getAlumnos().iterator().next();
        assertEquals("Vivi", foundAlumno.getNombre());
        
        assertFalse(foundAlumno.getModulos().isEmpty());
        assertEquals("Acceso a Datos", foundAlumno.getModulos().iterator().next().getNombre());
    }

    @Test
    void testTransactionalIntegrity() {
        em.getTransaction().begin();
        
        Curso curso = new Curso("3º D.A.M.", "Tercer curso (inválido)");
        em.persist(curso);
        
        try {
            // Intentar crear un alumno con DNI duplicado
            Alumno alumno1 = new Alumno("Repetido1", "Test", "99999999X");
            Alumno alumno2 = new Alumno("Repetido2", "Test", "99999999X"); // Mismo DNI
            
            curso.addAlumno(alumno1);
            curso.addAlumno(alumno2);
            
            em.persist(alumno1);
            em.persist(alumno2);
            
            em.getTransaction().commit();
            fail("Debería haber lanzado una excepción por DNI duplicado");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
        
        // Verificar que no se persistió nada debido al rollback
        TypedQuery<Curso> query = em.createQuery("SELECT c FROM Curso c WHERE c.nombre = :nombre", Curso.class);
        query.setParameter("nombre", "3º D.A.M.");
        assertTrue(query.getResultList().isEmpty());
    }
}