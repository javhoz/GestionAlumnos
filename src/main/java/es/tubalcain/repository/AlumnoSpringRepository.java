package es.tubalcain.repository;

import es.tubalcain.domain.Alumno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlumnoSpringRepository extends JpaRepository<Alumno, Long> {

    // Buscar por DNI
    Optional<Alumno> findByDni(String dni);

    // Buscar por número de expediente
    Optional<Alumno> findByNumeroExpediente(String numeroExpediente);

    // Buscar alumnos activos
    List<Alumno> findByActivoTrue();

    // Buscar por curso actual
    List<Alumno> findByCursoActual(Integer cursoActual);

    // Buscar por nombre
    List<Alumno> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por apellidos
    List<Alumno> findByApellidosContainingIgnoreCase(String apellidos);

    // Buscar por curso (relación ManyToOne)
    List<Alumno> findByCursoId(Long cursoId);

    // Buscar por módulo (relación ManyToMany)
    List<Alumno> findByModulosId(Long moduloId);

    // Buscar por userId (propietario) - TODOS los métodos deben incluir userId para seguridad
    List<Alumno> findByUserId(Long userId);
    Page<Alumno> findByUserId(Long userId, Pageable pageable);
    Optional<Alumno> findByIdAndUserId(Long id, Long userId);
    Optional<Alumno> findByDniAndUserId(String dni, Long userId); // DNI filtrado por usuario
    List<Alumno> findByUserIdAndActivoTrue(Long userId);
    List<Alumno> findByUserIdAndNombreContainingIgnoreCase(Long userId, String nombre);
    List<Alumno> findByUserIdAndApellidosContainingIgnoreCase(Long userId, String apellidos);

    // Paginación completa
    Page<Alumno> findAll(Pageable pageable);
}
