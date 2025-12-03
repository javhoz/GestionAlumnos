package es.tubalcain.controller;

import es.tubalcain.domain.Alumno;
import es.tubalcain.service.AlumnoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumnos")
@CrossOrigin(origins = "*")
public class AlumnoController {

    private final AlumnoService alumnoService;

    public AlumnoController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    // CREAR
    @PostMapping
    public Alumno crear(@RequestBody Alumno alumno) {
        return alumnoService.crearAlumno(alumno);
    }

    // LISTAR TODOS
    @GetMapping
    public List<Alumno> listar() {
        return alumnoService.listarTodos();
    }

    // LISTAR PAGINADO
    @GetMapping("/paginado")
    public Page<Alumno> listarPaginado(Pageable pageable) {
        return alumnoService.listarPaginado(pageable);
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public Alumno buscarPorId(@PathVariable Long id) {
        return alumnoService.buscarPorId(id);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public Alumno actualizar(@PathVariable Long id, @RequestBody Alumno alumno) {
        return alumnoService.actualizarAlumno(id, alumno);
    }

    // BORRADO REAL
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        alumnoService.eliminarAlumno(id);
    }

    // BORRADO LÓGICO (DESACTIVAR, tengo que explicar el version lock)
    @PutMapping("/desactivar/{id}")
    public void desactivar(@PathVariable Long id) {
        alumnoService.desactivarAlumno(id);
    }

    // BUSCAR POR NOMBRE
    @GetMapping("/buscar/nombre/{nombre}")
    public List<Alumno> buscarPorNombre(@PathVariable String nombre) {
        return alumnoService.buscarPorNombre(nombre);
    }

    // BUSCAR POR APELLIDOS
    @GetMapping("/buscar/apellidos/{apellidos}")
    public List<Alumno> buscarPorApellidos(@PathVariable String apellidos) {
        return alumnoService.buscarPorApellidos(apellidos);
    }

    // LISTAR SOLO ACTIVOS
    @GetMapping("/activos")
    public List<Alumno> listarActivos() {
        return alumnoService.listarActivos();
    }
}
