package es.tubalcain.dto;

import java.time.LocalDate;
import java.util.Set;

public class AlumnoDTO {

    private Long id;
    private String nombre;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    private String email;
    private String telefono;
    private String direccion;
    private String numeroExpediente;
    private Integer cursoActual;
    private boolean activo;

    // Relaciones por ID
    private Long cursoId;
    private Set<Long> moduloIds;

    // ========== Getters & Setters ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getNumeroExpediente() { return numeroExpediente; }
    public void setNumeroExpediente(String numeroExpediente) { this.numeroExpediente = numeroExpediente; }

    public Integer getCursoActual() { return cursoActual; }
    public void setCursoActual(Integer cursoActual) { this.cursoActual = cursoActual; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public Set<Long> getModuloIds() { return moduloIds; }
    public void setModuloIds(Set<Long> moduloIds) { this.moduloIds = moduloIds; }
}
