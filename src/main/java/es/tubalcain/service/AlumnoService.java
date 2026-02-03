package es.tubalcain.service;

import es.tubalcain.domain.Alumno;
import es.tubalcain.domain.User;
import es.tubalcain.exception.OwnershipException;
import es.tubalcain.repository.AlumnoSpringRepository;
import es.tubalcain.repository.AlumnoDocumentacionRepository;
import es.tubalcain.security.UserContext;
import es.tubalcain.domain.AlumnoDocumentacion;
import es.tubalcain.domain.AlumnoDocumentacionCache;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AlumnoService {

    private static final Logger log = LoggerFactory.getLogger(AlumnoService.class);

    private final AlumnoSpringRepository alumnoRepository;
        private final AlumnoDocumentacionRepository alumnoDocumentacionRepository;
        //private final AlumnoDocumentacionCacheRepository alumnoDocumentacionRepository;

    private final UserContext userContext;

    public AlumnoService(AlumnoSpringRepository alumnoRepository, AlumnoDocumentacionRepository alumnoDocumentacionRepository, UserContext userContext) {
        this.alumnoRepository = alumnoRepository;
        this.alumnoDocumentacionRepository = alumnoDocumentacionRepository;
        //this.cacheRepository = cacheRepository;
        this.userContext = userContext;
    }
    
    /**
     * Verifica si el usuario actual tiene permisos de profesor (PROFESOR)
     */
    private boolean hasProfesorPermissions(Set<User.Role> roles) {
        return roles.contains(User.Role.PROFESOR);
    }

    @Transactional
    public Alumno crearAlumno(Alumno alumno) {
        User currentUser = userContext.getCurrentUser();
        Set<User.Role> roles = currentUser.getRoles();
        
        // PROFESOR can create alumnos for any user, but if not specified, default to current user
        // ALUMNO can only create for themselves (though they shouldn't be able to create due to controller restrictions)
        if (alumno.getUser() == null) {
            alumno.setUser(currentUser);
        } else if (!hasProfesorPermissions(roles)) {
            // If not PROFESOR, force user to be current user
            alumno.setUser(currentUser);
        }

        Long userId = alumno.getUser().getId();
        
        if (alumno.getDni() != null && !alumno.getDni().isEmpty()) {
            if (hasProfesorPermissions(roles)) {
                // PROFESOR: check DNI globally
                alumnoRepository.findByDni(alumno.getDni())
                        .ifPresent(a -> {
                            throw new RuntimeException("Ya existe un alumno con ese DNI");
                        });
            } else {
                // ALUMNO: check DNI only for their user
                alumnoRepository.findByDniAndUserId(alumno.getDni(), userId)
                        .ifPresent(a -> {
                            throw new RuntimeException("Ya existe un alumno con ese DNI para tu usuario");
                        });
            }
        }
        
        return alumnoRepository.save(alumno);
    }


    public List<Alumno> listarTodos() {
        User currentUser = userContext.getCurrentUser();
        Set<User.Role> roles = currentUser.getRoles();
        
        // PROFESOR can see all alumnos, ALUMNO only their own
        if (hasProfesorPermissions(roles)) {
            return alumnoRepository.findAll();
        } else {
            Long userId = currentUser.getId();
            return alumnoRepository.findByUserId(userId);
        }
    }

 
    public Alumno buscarPorId(Long id) {
        User currentUser = userContext.getCurrentUser();
        Set<User.Role> roles = currentUser.getRoles();
        
        // PROFESOR puede acceder a cualquier alumno, ALUMNO solo a su propia información
        if (hasProfesorPermissions(roles)) {
            return alumnoRepository.findById(id)
                    .orElseThrow(() -> new OwnershipException("Alumno no encontrado"));
        } else {
            // ALUMNO solo puede acceder a su propia información
            Long userId = currentUser.getId();
            return alumnoRepository.findByIdAndUserId(id, userId)
                    .orElseThrow(() -> new OwnershipException(
                        "Alumno no encontrado o no tienes permiso para acceder a él"));
        }
    }

    @Transactional
    public Alumno actualizarAlumno(Long id, Alumno alumnoActualizado) {
        // buscarPorId already checks permissions based on role
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
        // buscarPorId already checks permissions based on role
        Alumno alumno = buscarPorId(id);
        
        alumnoRepository.delete(alumno);
    }

    // Borrado suave
    @Transactional
    public void desactivarAlumno(Long id) {
        // buscarPorId already checks permissions based on role
        Alumno alumno = buscarPorId(id);
        
        // Pasar a inactivo
        alumno.setActivo(false);
        alumnoRepository.save(alumno);
    }

    public List<Alumno> buscarPorNombre(String nombre) {
        User currentUser = userContext.getCurrentUser();
        Set<User.Role> roles = currentUser.getRoles();
        
        if (hasProfesorPermissions(roles)) {
            return alumnoRepository.findByNombreContainingIgnoreCase(nombre);
        } else {
            Long userId = currentUser.getId();
            return alumnoRepository.findByUserIdAndNombreContainingIgnoreCase(userId, nombre);
        }
    }

    public List<Alumno> buscarPorApellidos(String apellidos) {
        User currentUser = userContext.getCurrentUser();
        Set<User.Role> roles = currentUser.getRoles();
        
        if (hasProfesorPermissions(roles)) {
            return alumnoRepository.findByApellidosContainingIgnoreCase(apellidos);
        } else {
            Long userId = currentUser.getId();
            return alumnoRepository.findByUserIdAndApellidosContainingIgnoreCase(userId, apellidos);
        }
    }

    public List<Alumno> listarActivos() {
        User currentUser = userContext.getCurrentUser();
        Set<User.Role> roles = currentUser.getRoles();
        
        if (hasProfesorPermissions(roles)) {
            return alumnoRepository.findByActivoTrue();
        } else {
            Long userId = currentUser.getId();
            return alumnoRepository.findByUserIdAndActivoTrue(userId);
        }
    }

    public Page<Alumno> listarPaginado(Pageable pageable) {
        User currentUser = userContext.getCurrentUser();
        Set<User.Role> roles = currentUser.getRoles();
        
        if (hasProfesorPermissions(roles)) {
            return alumnoRepository.findAll(pageable);
        } else {
            Long userId = currentUser.getId();
            return alumnoRepository.findByUserId(userId, pageable);
        }
    }

    @Transactional
    public void subirDocumentacion(Long idAlumno, String filename, byte[] content) {
        // buscarPorId already checks permissions based on role
        Alumno alumno = buscarPorId(idAlumno);

        AlumnoDocumentacion doc = new AlumnoDocumentacion();
        doc.setAlumnoId(alumno.getId());
        doc.setFilename(filename);
        doc.setContent(content);
        doc.setUploadedById(userContext.getCurrentUser().getId());
        doc.setUploadedAt(Instant.now());

        alumnoDocumentacionRepository.save(doc);

        // Update Redis cache if available; tolerate connection failures
        /**if (cacheRepository.isPresent()) {
            try {
                AlumnoDocumentacionCacheRepository repo = cacheRepository.get();
                Optional<AlumnoDocumentacionCache> maybe = repo.findById(alumno.getId());
                AlumnoDocumentacionCache cache = maybe.orElseGet(() -> new AlumnoDocumentacionCache(alumno.getId(), 0, null));
                cache.setDocumentCount(cache.getDocumentCount() + 1);
                cache.setLastUploadedAt(Instant.now());
                repo.save(cache);
            } catch (Exception e) {
                // likely Redis connection failure; log and continue
                log.warn("Could not update Redis cache for alumno {} — skipping cache update: {}", idAlumno, e.getMessage());
            }
        } else {
            log.debug("Redis cache repository not available; skipping cache update for alumno {}", idAlumno);
        }**/
    }
}
