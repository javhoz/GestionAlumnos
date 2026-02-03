package es.tubalcain.repository;

import es.tubalcain.domain.AlumnoDocumentacion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AlumnoDocumentacionRepository extends MongoRepository<AlumnoDocumentacion, String> {
    List<AlumnoDocumentacion> findByAlumnoId(Long alumnoId);
}
