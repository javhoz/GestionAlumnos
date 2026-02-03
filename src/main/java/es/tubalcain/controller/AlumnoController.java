package es.tubalcain.controller;

import es.tubalcain.assembler.AlumnoAssembler;
import es.tubalcain.dto.AlumnoDTO;
import es.tubalcain.domain.Alumno;
import es.tubalcain.service.AlumnoService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

@RestController("alumnoController") // changed: explicit unique bean name to avoid conflict
@RequestMapping("/api/alumnos")
@CrossOrigin(origins = "*")
public class AlumnoController {

    private final AlumnoService alumnoService;
    private final AlumnoAssembler alumnoAssembler;

    public AlumnoController(AlumnoService alumnoService, AlumnoAssembler alumnoAssembler) {
        this.alumnoService = alumnoService;
        this.alumnoAssembler = alumnoAssembler;
    }

    // CREAR
    @PostMapping
    @PreAuthorize("hasRole('PROFESOR')")
    public AlumnoDTO crear(@RequestBody AlumnoDTO alumnoDto) {

//        Curso curso = alumnoService.buscarCursoPorId(alumnoDto.getCursoId());
//
//        Set<Modulo> modulos = alumnoService.buscarModulosPorIds(
//                alumnoDto.getModuloIds()
//        );

        Alumno alumno = alumnoAssembler.toEntity(alumnoDto);
        Alumno persistedAlumno = alumnoService.crearAlumno(alumno);

        return alumnoAssembler.toDTO(persistedAlumno);
    }

    // LISTAR TODOS
    @GetMapping
    public List<AlumnoDTO> listar() {
        return alumnoService.listarTodos()
                .stream()
                .map(alumnoAssembler::toDTO)
                .collect(Collectors.toList());
    }

    // LISTAR PAGINADO
    @GetMapping("/paginado")
    public Page<AlumnoDTO> listarPaginado(Pageable pageable) {
        return alumnoService.listarPaginado(pageable)
                .map(alumnoAssembler::toDTO);
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public AlumnoDTO buscarPorId(@PathVariable Long id) {
    	
        Alumno alumno = alumnoService.buscarPorId(id);
        return alumnoAssembler.toDTO(alumno);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public AlumnoDTO actualizar(@PathVariable Long id, @RequestBody AlumnoDTO dto) {

        Alumno alumnoExistente = alumnoService.buscarPorId(id);

//        Curso curso = alumnoService.buscarCursoPorId(dto.getCursoId());
//
//        Set<Modulo> modulos = alumnoService.buscarModulosPorIds(
//                dto.getModuloIds()
//        );
//
//        alumnoAssembler.updateEntity(dto, alumnoExistente, curso, modulos);

        Alumno actualizado = alumnoService.actualizarAlumno(id, alumnoExistente);

        return alumnoAssembler.toDTO(actualizado);
    }

    // BORRADO REAL
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public void eliminar(@PathVariable Long id) {
        alumnoService.eliminarAlumno(id);
    }

    // BORRADO LÓGICO
    @PutMapping("/desactivar/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public void desactivar(@PathVariable Long id) {
        alumnoService.desactivarAlumno(id);
    }

    // BUSCAR POR NOMBRE
    @GetMapping("/buscar/nombre/{nombre}")
    public List<AlumnoDTO> buscarPorNombre(@PathVariable String nombre) {
        return alumnoService.buscarPorNombre(nombre)
                .stream()
                .map(alumnoAssembler::toDTO)
                .collect(Collectors.toList());
    }

    // BUSCAR POR APELLIDOS
    @GetMapping("/buscar/apellidos/{apellidos}")
    public List<AlumnoDTO> buscarPorApellidos(@PathVariable String apellidos) {
        return alumnoService.buscarPorApellidos(apellidos)
                .stream()
                .map(alumnoAssembler::toDTO)
                .collect(Collectors.toList());
    }

    // LISTAR SOLO ACTIVOS
    @GetMapping("/activos")
    public List<AlumnoDTO> listarActivos() {
        return alumnoService.listarActivos()
                .stream()
                .map(alumnoAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/{idAlumno}/documentacion")
    public ResponseEntity<Void> subirDocumentacion(@PathVariable Long idAlumno,
                                                   @RequestParam("file") MultipartFile file) throws IOException {
        alumnoService.subirDocumentacion(idAlumno, file.getOriginalFilename(), file.getBytes());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
