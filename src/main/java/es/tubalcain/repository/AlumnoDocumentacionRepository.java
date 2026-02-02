package es.tubalcain.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import es.tubalcain.domain.AlumnoDocumentacion;

public interface AlumnoDocumentacionRepository extends MongoRepository<AlumnoDocumentacion, String> {
    List<AlumnoDocumentacion> findByAlumnoId(Long alumnoId);
}
