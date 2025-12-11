package es.tubalcain.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import es.tubalcain.domain.Alumno;
import es.tubalcain.repository.AlumnoSpringRepository;

@Route("alumnos")
public class AlumnoView extends VerticalLayout {

    private AlumnoSpringRepository repository;
    
    private Grid<Alumno> grid = new Grid<>(Alumno.class);

    private TextField nombreField = new TextField("Nombre");
    private TextField apellidosField = new TextField("Apellidos");
    private TextField dniField = new TextField("DNI");
    private TextField emailField = new TextField("Email");
    private Button addButton = new Button("Agregar");
    private Button deleteButton = new Button("Eliminar seleccionado");
    

    @Autowired
    public AlumnoView(AlumnoSpringRepository repository) {
        this.repository = repository;

        // Configure grid
        grid.setColumns("id", "nombre", "apellidos", "dni", "email");
        refreshGrid();

        // Add button listeners
        addButton.addClickListener(e -> addAlumno());
        deleteButton.addClickListener(e -> deleteAlumno());

        add(nombreField, apellidosField, dniField, emailField, addButton, deleteButton, grid);
    }

    private void addAlumno() {
        String nombre = nombreField.getValue();
        String apellidos = apellidosField.getValue();
        String dni = dniField.getValue();
        String email = emailField.getValue();
        if (!nombre.isEmpty() && !apellidos.isEmpty() && !dni.isEmpty() && !email.isEmpty()) {
            repository.save(new Alumno(nombre, apellidos, dni, email));
            nombreField.clear();
            apellidosField.clear();
            dniField.clear();
            emailField.clear();
            refreshGrid();
        }
    }

    private void deleteAlumno() {
        Alumno selected = grid.asSingleSelect().getValue();
        if (selected != null) {
            repository.delete(selected);
            refreshGrid();
        }
    }

    private void refreshGrid() {
        grid.setItems(repository.findAll());
    }
}
