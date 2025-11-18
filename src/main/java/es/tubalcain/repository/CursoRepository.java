package es.tubalcain.repository;

import java.util.List;
import java.util.Optional;

import es.tubalcain.domain.Curso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

public class CursoRepository {

    private final EntityManager em;

    public CursoRepository(EntityManager em) {
        this.em = em;
    }

    /** Persiste un nuevo curso en la base de datos */
    @Transactional
    public void save(Curso curso) {
        em.persist(curso);
    }

    /** Actualiza un curso existente */
    @Transactional
    public Curso update(Curso curso) {
        return em.merge(curso);
    }

    /** Elimina un curso por instancia */
    @Transactional
    public void delete(Curso curso) {
        if (!em.contains(curso)) {
            curso = em.merge(curso);
        }
        em.remove(curso);
    }

    /** Elimina un curso por su ID */
    @Transactional
    public void deleteById(Long id) {
        findById(id).ifPresent(this::delete);
    }

    /** Busca un curso por ID */
    public Optional<Curso> findById(Long id) {
        return Optional.ofNullable(em.find(Curso.class, id));
    }

    /** Busca un curso por nombre exacto */
    public Optional<Curso> findByNombre(String nombre) {
        TypedQuery<Curso> query = em.createQuery("SELECT c FROM Curso c WHERE c.nombre = :nombre", Curso.class);
        query.setParameter("nombre", nombre);
        return query.getResultStream().findFirst();
    }

    /** Busca cursos cuyo nombre contiene el término (case-insensitive) */
    public List<Curso> findByNombreLike(String nombre) {
        TypedQuery<Curso> query = em.createQuery(
                "SELECT c FROM Curso c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))",
                Curso.class);
        query.setParameter("nombre", nombre);
        return query.getResultList();
    }

    /** Devuelve todos los cursos */
    public List<Curso> findAll() {
        TypedQuery<Curso> query = em.createQuery("SELECT c FROM Curso c", Curso.class);
        return query.getResultList();
    }

    /** Devuelve el número total de cursos */
    public long countAll() {
        Query query = em.createQuery("SELECT COUNT(c) FROM Curso c");
        return (long) query.getSingleResult();
    }

    /** Devuelve una lista paginada de cursos */
    public List<Curso> findAllPaginated(int offset, int limit) {
        TypedQuery<Curso> query = em.createQuery("SELECT c FROM Curso c ORDER BY c.nombre", Curso.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

}
