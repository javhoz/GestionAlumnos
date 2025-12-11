package es.tubalcain.service;

import es.tubalcain.domain.Alumno;
import es.tubalcain.repository.AlumnoSpringRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlumnoService {

    private final AlumnoSpringRepository alumnoRepository;

    public AlumnoService(AlumnoSpringRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    // Crear alumno (validando DNI, tengo que explicar LN)
    @Transactional
    public Alumno crearAlumno(Alumno alumno) {
        alumnoRepository.findByDni(alumno.getDni()).ifPresent(a -> {
            throw new RuntimeException("Ya existe un alumno con ese DNI");
        });
        return alumnoRepository.save(alumno);
    }

    // Listar todos
    public List<Alumno> listarTodos() {
        return alumnoRepository.findAll();
    }

    // Buscar por ID
    public Alumno buscarPorId(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
    }

    // Actualizar alumno
    @Transactional
    public Alumno actualizarAlumno(Long id, Alumno alumnoActualizado) {
        Alumno alumno = buscarPorId(id);

        alumno.setNombre(alumnoActualizado.getNombre());
        alumno.setApellidos(alumnoActualizado.getApellidos());
        alumno.setEmail(alumnoActualizado.getEmail());
        alumno.setTelefono(alumnoActualizado.getTelefono());
        alumno.setDireccion(alumnoActualizado.getDireccion());
        alumno.setCursoActual(alumnoActualizado.getCursoActual());
        alumno.setActivo(alumnoActualizado.isActivo());

        return alumnoRepository.save(alumno);
    }

    // Borrado real
    @Transactional
    public void eliminarAlumno(Long id) {
        alumnoRepository.deleteById(id);
    }

    // Desactivar alumno (borrado lógico)
    @Transactional
    public void desactivarAlumno(Long id) {
        Alumno alumno = buscarPorId(id);
        alumno.setActivo(false);
        alumnoRepository.save(alumno);
    }

    // Búsquedas
    public List<Alumno> buscarPorNombre(String nombre) {
        return alumnoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Alumno> buscarPorApellidos(String apellidos) {
        return alumnoRepository.findByApellidosContainingIgnoreCase(apellidos);
    }

    public List<Alumno> listarActivos() {
        return alumnoRepository.findByActivoTrue();
    }

    // Paginación
    public Page<Alumno> listarPaginado(Pageable pageable) {
        return alumnoRepository.findAll(pageable);
    }
}
