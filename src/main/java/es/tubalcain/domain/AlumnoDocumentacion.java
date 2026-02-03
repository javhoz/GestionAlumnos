package es.tubalcain.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "documentacion_alumnos")
public class AlumnoDocumentacion {

    @Id
    private String id;
    private Long alumnoId;
    private String filename;
    private byte[] content;
    private Long uploadedById;
    private Instant uploadedAt;

    public AlumnoDocumentacion() {}

    // getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public byte[] getContent() { return content; }
    public void setContent(byte[] content) { this.content = content; }

    public Long getUploadedById() { return uploadedById; }
    public void setUploadedById(Long uploadedById) { this.uploadedById = uploadedById; }

    public Instant getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Instant uploadedAt) { this.uploadedAt = uploadedAt; }
}
