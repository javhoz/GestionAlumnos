package es.tubalcain.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;

@RedisHash("AlumnoDocumentacionCache")
public class AlumnoDocumentacionCache {

    @Id
    private Long alumnoId;
    private int documentCount;
    private Instant lastUploadedAt;

    public AlumnoDocumentacionCache() {}

    public AlumnoDocumentacionCache(Long alumnoId, int documentCount, Instant lastUploadedAt) {
        this.alumnoId = alumnoId;
        this.documentCount = documentCount;
        this.lastUploadedAt = lastUploadedAt;
    }

    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }

    public int getDocumentCount() { return documentCount; }
    public void setDocumentCount(int documentCount) { this.documentCount = documentCount; }

    public Instant getLastUploadedAt() { return lastUploadedAt; }
    public void setLastUploadedAt(Instant lastUploadedAt) { this.lastUploadedAt = lastUploadedAt; }
}
