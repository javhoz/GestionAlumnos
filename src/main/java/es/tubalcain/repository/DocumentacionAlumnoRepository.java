package es.tubalcain.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import es.tubalcain.domain.DocumentacionAlumno;

public interface DocumentacionAlumnoRepository extends MongoRepository<DocumentacionAlumno, String> {
    List<DocumentacionAlumno> findByAlumnoId(Long alumnoId);
}
