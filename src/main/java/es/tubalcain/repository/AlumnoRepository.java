package es.tubalcain.repository;

import java.util.List;
import java.util.Optional;

import es.tubalcain.domain.Alumno;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class AlumnoRepository { // En Spring sera una interface heredando de JPA, TODO de cara a futuro

	private final EntityManager em;

	public AlumnoRepository(EntityManager em) {
		this.em = em;
	}

	public void save(Alumno alumno) {
		em.persist(alumno);
	}

	public Optional<Alumno> findById(Long id) {
		return Optional.ofNullable(em.find(Alumno.class, id));
	}

	public Optional<Alumno> findByDni(String dni) {
		TypedQuery<Alumno> query = em.createQuery("SELECT a FROM Alumno a WHERE a.dni = :dni", Alumno.class);
		query.setParameter("dni", dni);
		return query.getResultStream().findFirst();
	}

	public Optional<Alumno> findByNumeroExpediente(String numExp) {
		TypedQuery<Alumno> query = em.createQuery("SELECT a FROM Alumno a WHERE a.numeroExpediente = :numExp",
				Alumno.class);
		query.setParameter("numExp", numExp);
		return query.getResultStream().findFirst();
	}

	public List<Alumno> findAll() {
        TypedQuery<Alumno> query = em.createQuery("SELECT a FROM Alumno a", Alumno.class);
        return query.getResultList();
    }

	public List<Alumno> findActivos() {
		TypedQuery<Alumno> query = em.createQuery("SELECT a FROM Alumno a WHERE a.activo = true", Alumno.class);
		return query.getResultList();
	}

	public List<Alumno> findByCursoActual(int curso) {
		TypedQuery<Alumno> query = em.createQuery("SELECT a FROM Alumno a WHERE a.cursoActual = :curso", Alumno.class);
		query.setParameter("curso", curso);
		return query.getResultList();
	}
}
