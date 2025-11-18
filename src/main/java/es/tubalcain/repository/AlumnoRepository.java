package es.tubalcain.repository;

import java.util.List;
import java.util.Optional;

import es.tubalcain.domain.Alumno;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

public class AlumnoRepository { // En Spring será una interface heredando de JPA, TODO de cara a futuro

    private final EntityManager em;

    public AlumnoRepository(EntityManager em) {
        this.em = em;
    }

    /** Persiste un nuevo alumno en la base de datos */
    @Transactional
    public void save(Alumno alumno) {
        em.persist(alumno);
    }

    /** Actualiza un alumno existente */
    @Transactional
    public Alumno update(Alumno alumno) {
        return em.merge(alumno);
    }

    /** Elimina un alumno por instancia */
    @Transactional
    public void delete(Alumno alumno) {
        if (!em.contains(alumno)) {
            alumno = em.merge(alumno);
        }
        em.remove(alumno);
    }

    /** Elimina un alumno por su ID */
    @Transactional
    public void deleteById(Long id) {
        findById(id).ifPresent(this::delete);
    }

    /** Busca un alumno por ID */
    public Optional<Alumno> findById(Long id) {
        return Optional.ofNullable(em.find(Alumno.class, id));
    }

    /**
     * Busca un alumno por ID y carga también su `curso` asociado en la misma consulta.
     * Usa `JOIN FETCH` para evitar LazyInitializationException fuera de la transacción
     * y para curarnos en salud del problema N+1 si vamos a necesitar el curso inmediatamente.
     */
    public Optional<Alumno> findByIdWithCurso(Long id) {
        TypedQuery<Alumno> query = em.createQuery(
                "SELECT a FROM Alumno a LEFT JOIN FETCH a.curso WHERE a.id = :id",
                Alumno.class);
        query.setParameter("id", id);
        return query.getResultStream().findFirst();
    }

    /** Busca un alumno por DNI */
    public Optional<Alumno> findByDni(String dni) {
        TypedQuery<Alumno> query = em.createQuery("SELECT a FROM Alumno a WHERE a.dni = :dni", Alumno.class);
        query.setParameter("dni", dni);
        return query.getResultStream().findFirst();
    }

    /** Busca un alumno por número de expediente */
    public Optional<Alumno> findByNumeroExpediente(String numExp) {
        TypedQuery<Alumno> query = em.createQuery(
                "SELECT a FROM Alumno a WHERE a.numeroExpediente = :numExp",
                Alumno.class);
        query.setParameter("numExp", numExp);
        return query.getResultStream().findFirst();
    }

    /** Devuelve todos los alumnos */
    public List<Alumno> findAll() {
        TypedQuery<Alumno> query = em.createQuery("SELECT a FROM Alumno a", Alumno.class);
        return query.getResultList();
    }

    /** Devuelve los alumnos activos */
    public List<Alumno> findActivos() {
        TypedQuery<Alumno> query = em.createQuery(
                "SELECT a FROM Alumno a WHERE a.activo = true",
                Alumno.class);
        return query.getResultList();
    }

    /** Busca alumnos por curso actual */
    public List<Alumno> findByCursoActual(int curso) {
        TypedQuery<Alumno> query = em.createQuery(
                "SELECT a FROM Alumno a WHERE a.cursoActual = :curso",
                Alumno.class);
        query.setParameter("curso", curso);
        return query.getResultList();
    }

    /** Busca alumnos por nombre o apellidos (parcial o completo, insensible a mayúsculas) */
    public List<Alumno> findByNombreCompleto(String nombre) {
        TypedQuery<Alumno> query = em.createQuery(
                "SELECT a FROM Alumno a WHERE LOWER(CONCAT(a.nombre, ' ', a.apellidos)) LIKE LOWER(CONCAT('%', :nombre, '%'))",
                Alumno.class);
        query.setParameter("nombre", nombre);
        return query.getResultList();
    }

    /** Busca alumnos por grupo (por ejemplo, 1A, 2B, etc.) */
    public List<Alumno> findByGrupo(String grupo) {
        TypedQuery<Alumno> query = em.createQuery(
                "SELECT a FROM Alumno a WHERE a.grupo = :grupo",
                Alumno.class);
        query.setParameter("grupo", grupo);
        return query.getResultList();
    }

    /** Devuelve el número total de alumnos */
    public long countAll() {
        Query query = em.createQuery("SELECT COUNT(a) FROM Alumno a");
        return (long) query.getSingleResult();
    }

    /** Devuelve el número de alumnos activos */
    public long countActivos() {
        Query query = em.createQuery("SELECT COUNT(a) FROM Alumno a WHERE a.activo = true");
        return (long) query.getSingleResult();
    }

    /** Devuelve una lista paginada de alumnos (útil para mostrar resultados en páginas) */
    public List<Alumno> findAllPaginated(int offset, int limit) {
        TypedQuery<Alumno> query = em.createQuery("SELECT a FROM Alumno a ORDER BY a.apellidos, a.nombre", Alumno.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    /** Marca un alumno como inactivo */
    @Transactional
    public void desactivarAlumno(Long id) {
        findById(id).ifPresent(a -> {
            a.setActivo(false);
            em.merge(a);
        });
    }

    /** Reactiva un alumno */
    @Transactional
    public void activarAlumno(Long id) {
        findById(id).ifPresent(a -> {
            a.setActivo(true);
            em.merge(a);
        });
    }

}
