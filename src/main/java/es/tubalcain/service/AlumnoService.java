package es.tubalcain.service;

import es.tubalcain.domain.Alumno;
import es.tubalcain.domain.User;
import es.tubalcain.exception.OwnershipException;
import es.tubalcain.repository.AlumnoSpringRepository;
import es.tubalcain.security.UserContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlumnoService {

    private final AlumnoSpringRepository alumnoRepository;
    private final UserContext userContext;

    public AlumnoService(AlumnoSpringRepository alumnoRepository, UserContext userContext) {
        this.alumnoRepository = alumnoRepository;
        this.userContext = userContext;
    }

    @Transactional
    public Alumno crearAlumno(Alumno alumno) {
        User currentUser = userContext.getCurrentUser();
        Long userId = currentUser.getId();
        
        alumno.setUser(currentUser);

        if (alumno.getDni() != null && !alumno.getDni().isEmpty()) {
            alumnoRepository.findByDniAndUserId(alumno.getDni(), userId)
                    .ifPresent(a -> {
                        throw new RuntimeException("Ya existe un alumno con ese DNI para tu usuario");
                    });
        }
        
        return alumnoRepository.save(alumno);
    }


    public List<Alumno> listarTodos() {
        Long userId = userContext.getCurrentUserId();
        return alumnoRepository.findByUserId(userId);
    }

 
    public Alumno buscarPorId(Long id) {
        Long userId = userContext.getCurrentUserId();
        
        // Solo encontrar si pertenece al usuario actual
        return alumnoRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new OwnershipException(
                    "Alumno no encontrado o no tienes permiso para acceder a él"));
    }

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


    @Transactional
    public void eliminarAlumno(Long id) {
        Alumno alumno = buscarPorId(id);
        
        alumnoRepository.delete(alumno);
    }

    // Borrado suave
    @Transactional
    public void desactivarAlumno(Long id) {
        Alumno alumno = buscarPorId(id);
        
        // Pasar a inactivo
        alumno.setActivo(false);
        alumnoRepository.save(alumno);
    }

    public List<Alumno> buscarPorNombre(String nombre) {
        Long userId = userContext.getCurrentUserId();
        return alumnoRepository.findByUserIdAndNombreContainingIgnoreCase(userId, nombre);
    }

    public List<Alumno> buscarPorApellidos(String apellidos) {
        Long userId = userContext.getCurrentUserId();
        return alumnoRepository.findByUserIdAndApellidosContainingIgnoreCase(userId, apellidos);
    }

    public List<Alumno> listarActivos() {
        Long userId = userContext.getCurrentUserId();
        return alumnoRepository.findByUserIdAndActivoTrue(userId);
    }

    public Page<Alumno> listarPaginado(Pageable pageable) {
        Long userId = userContext.getCurrentUserId();
        return alumnoRepository.findByUserId(userId, pageable);
    }
}
