package es.tubalcain.assembler;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import es.tubalcain.dto.AlumnoDTO;
import es.tubalcain.domain.Alumno;
import es.tubalcain.domain.Curso;
import es.tubalcain.domain.Modulo;

@Component
public class AlumnoAssembler {

    public AlumnoDTO toDTO(Alumno alumno) {
        if (alumno == null) return null;

        AlumnoDTO dto = new AlumnoDTO();

        dto.setId(alumno.getId());
        dto.setNombre(alumno.getNombre());
        dto.setApellidos(alumno.getApellidos());
        dto.setDni(alumno.getDni());
        dto.setFechaNacimiento(alumno.getFechaNacimiento());
        dto.setEmail(alumno.getEmail());
        dto.setTelefono(alumno.getTelefono());
        dto.setDireccion(alumno.getDireccion());
        dto.setNumeroExpediente(alumno.getNumeroExpediente());
        dto.setCursoActual(alumno.getCursoActual());
        dto.setActivo(alumno.isActivo());

        if (alumno.getCurso() != null) {
            dto.setCursoId(alumno.getCurso().getId());
        }

        if (alumno.getModulos() != null) {
            dto.setModuloIds(
                alumno.getModulos()
                      .stream()
                      .map(Modulo::getId)
                      .collect(Collectors.toSet())
            );
        }

        return dto;
    }

    public Alumno toEntity(AlumnoDTO dto) {
        if (dto == null) return null;

        Alumno alumno = new Alumno();
        mapCommonFields(dto, alumno);

        return alumno;
    }
    
    public Alumno toEntity(AlumnoDTO dto, Curso curso, Set<Modulo> modulos) {
        if (dto == null) return null;

        Alumno alumno = new Alumno();
        mapCommonFields(dto, alumno);

        alumno.setCurso(curso);
        alumno.setModulos(modulos);

        return alumno;
    }

    public void updateEntity(AlumnoDTO dto, Alumno alumno, Curso curso, Set<Modulo> modulos) {
        if (dto == null || alumno == null) return;

        mapCommonFields(dto, alumno);

        alumno.setCurso(curso);
        alumno.setModulos(modulos);
    }

    private void mapCommonFields(AlumnoDTO dto, Alumno alumno) {
        alumno.setNombre(dto.getNombre());
        alumno.setApellidos(dto.getApellidos());
        alumno.setDni(dto.getDni());
        alumno.setFechaNacimiento(dto.getFechaNacimiento());
        alumno.setEmail(dto.getEmail());
        alumno.setTelefono(dto.getTelefono());
        alumno.setDireccion(dto.getDireccion());
        alumno.setNumeroExpediente(dto.getNumeroExpediente());
        alumno.setCursoActual(dto.getCursoActual());
        alumno.setActivo(dto.isActivo());
    }
}
