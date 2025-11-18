package es.tubalcain.repository;

import java.util.List;
import java.util.Optional;

import es.tubalcain.domain.Curso;

/**
 * Contrato de repositorio para la entidad Curso.
 * Implementaciones pueden usar JPA estándar o APIs específicas de Hibernate.
 */
public interface ICursoRepository {

    void save(Curso curso);

    Curso update(Curso curso);

    void delete(Curso curso);

    void deleteById(Long id);

    Optional<Curso> findById(Long id);

    Optional<Curso> findByNombre(String nombre);

    List<Curso> findByNombreLike(String nombre);

    List<Curso> findAll();

    long countAll();

    List<Curso> findAllPaginated(int offset, int limit);

}
