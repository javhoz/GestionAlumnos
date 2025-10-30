package es.tubalcain.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToMany;

@Entity
@Table(name = "Modulo")
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    // Relación bidireccional ManyToMany con Alumno
    @ManyToMany(mappedBy = "modulos")
    private Set<Alumno> alumnos = new HashSet<>();

    public Modulo() {}

    public Modulo(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Set<Alumno> getAlumnos() { return alumnos; }
    public void setAlumnos(Set<Alumno> alumnos) { this.alumnos = alumnos; }

    public void addAlumno(Alumno alumno) {
        alumnos.add(alumno);
        alumno.getModulos().add(this);
    }

    public void removeAlumno(Alumno alumno) {
        alumnos.remove(alumno);
        alumno.getModulos().remove(this);
    }

    @Override
    public String toString() {
        return "Modulo{id=" + id + ", nombre='" + nombre + "'}";
    }
}
