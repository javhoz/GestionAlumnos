package es.tubalcain.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "alumno_documentacion")
public class AlumnoDocumentacion {

    @Id
    private String id;

    // Store JPA Alumno id (keeps MySQL as primary for Alumno entity)
    private Long alumnoId;

    // Metadata
    private String tipo; // e.g. "DNI", "Contrato"
    private String nombreArchivo;
    private Instant fechaSubida;

    // File content; for large files prefer GridFS
    private byte[] contenido;

}
