/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlles;

import Models.ConfigDB.ConnectionDb;
import Models.Interfaces.IConnectionDb;
import Models.Persona;
import Models.Utils.ConverterDateToLocalDateTime;
import com.mongodb.MongoSecurityException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javax.swing.JOptionPane;
import org.bson.Document;
import Models.Interfaces.IPersonaController;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.bson.types.ObjectId;

/**
 *
 * @author Bryan
 */
public class PersonaController implements IPersonaController {

    JTextField txtNombre;
    JTextField txtApellido;
    JTextField txtTelefono;
    JTextField txtDireccion;

    JTable tabla;
    JProgressBar brProgres;

    public PersonaController(JTextField _txtNombre, JTextField _txtApellido,
            JTextField _txtTelefono,
            JTextField _txtDireccion,
            JTable _tabla,
            JProgressBar _brProgres) {
        this.txtNombre = _txtNombre;
        this.txtApellido = _txtApellido;
        this.txtTelefono = _txtTelefono;
        this.txtDireccion = _txtDireccion;
        this.tabla = _tabla;
        this.brProgres = _brProgres;
        model = (DefaultTableModel) this.tabla.getModel();
    }

    IConnectionDb _connet = new ConnectionDb();

    Object[] obj = new Object[7];
    DefaultTableModel model = new DefaultTableModel();

    @Override
    public void insertOnePersonDb(Persona persona) {
        try {
            this.loading(0);
            this.loading(10);
            MongoCollection<Document> collectionPersona = this.getCollection("persona");
            this.loading(30);
            
            if (validIfExistElement(persona)) {
                this.loading(70);
                collectionPersona.insertOne(new Document("nombre", persona.getNombres())
                        .append("apellido", persona.getApellidos())
                        .append("telefono", persona.getTelefono())
                        .append("direccion", persona.getDireccion())
                        .append("created_at", persona.getCreatedAt())
                        .append("update_at", persona.getUpdatedAt())
                );
                this.loading(100);
            }
        } catch (MongoSecurityException e) {
            JOptionPane.showMessageDialog(null, "Credenciales de la BD incorrectas: " + e.getCredential());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error al guardar un persona: " + e.getMessage());
            System.out.println(e);
        }
    }

    @Override
    public void findAllPersons() {
        this.Limpiar();
        this.loading(0);
        
        this.loading(10);
        MongoCollection<Document> collectionPersona = this.getCollection("persona");
        this.loading(20);
        
        FindIterable<Document> all = collectionPersona.find();
        this.loading(30);
        
        all.forEach(persona -> {
            Persona newPersona = MapperPersonaJsonToObject(persona);

            this.loading(40);

            this.obj[0] = newPersona.getId();
            this.obj[1] = newPersona.getNombres();
            this.obj[2] = newPersona.getApellidos();
            this.obj[3] = newPersona.getTelefono();
            this.obj[4] = newPersona.getDireccion();
            this.obj[5] = newPersona.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm"));
            this.obj[6] = newPersona.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm"));

            this.loading(50);

            model.addRow(obj);
            this.loading(70);
            this.loading(100);
        });
        this.loading(100);
    }

    /**
     * Ontengo una coleccion de la base de datos
     *
     * @return MongoCollection<Document>
     * @param String
     */
    private MongoCollection<Document> getCollection(String nameCollection) {
        MongoClient client = this._connet.connect();
        MongoDatabase bd = client.getDatabase("Personas");
        return bd.getCollection(nameCollection);
    }

    /**
     * Valida si la persona a guardar ya existe
     *
     * @return boolean
     * @param Persona
     */
    private boolean validIfExistElement(Persona persona) {
        MongoCollection<Document> collectionPersona = getCollection("persona");
        if (collectionPersona.find(new Document("nombre", persona.getNombres())).first() != null) {
            JOptionPane.showMessageDialog(null, "Esta persona ya existe");
            return false;
        }
        JOptionPane.showMessageDialog(null, "Registro Exitoso");
        return true;
    }

    /**
     * Desealizamos el Json de persana a un Objeto persona
     *
     * @return Persona
     * @param Document
     */
    private Persona MapperPersonaJsonToObject(Document persona) {
        return new Persona(persona.getObjectId("_id").toString(),
                persona.getString("nombre"),
                persona.getString("apellido"),
                persona.getString("telefono"),
                persona.getString("direccion"),
                ConverterDateToLocalDateTime.transforToLocalDateTime(persona.getDate("created_at")),
                ConverterDateToLocalDateTime.transforToLocalDateTime(persona.getDate("update_at"))
        );
    }

    /**
     * Limpia toda la tabla de personas
     *
     * @return void
     */
    private void Limpiar() {
        for (int i = 0; i < this.tabla.getRowCount(); i++) {
            model.removeRow(i);
            i -= 1;
        }
    }

    @Override
    public void deleteOnePerson() {
        ObjectId per;
        if (this.tabla.getSelectedRow() >= 0) {
            this.loading(0);
            this.loading(10);
            per = new ObjectId(this.tabla.getValueAt(tabla.getSelectedRow(), 0).toString());
            
            this.loading(20);
            MongoCollection<Document> collectionPersona = this.getCollection("persona");
            this.loading(30);
            
            if (collectionPersona.findOneAndDelete(new Document("_id", per)) != null) {
                JOptionPane.showMessageDialog(null, "Registro Eliminado con exito");
                this.loading(100);
                return;
            }
            JOptionPane.showMessageDialog(null, "Registro no encontado");
            this.loading(0);
            return;
        }
        JOptionPane.showMessageDialog(null, "Selecione un Registro a eliminar");
    }

    /**
     * Carga la barra de progreso
     *
     * @return void
     */
    private void loading(int porcentaje) {
        this.brProgres.setValue(porcentaje);
    }

    @Override
    public void updateOnePerson() {
        ObjectId per;
        if (this.tabla.getSelectedRow() >= 0) {
            this.loading(0);
            this.loading(10);
            
            per = new ObjectId(this.tabla.getValueAt(tabla.getSelectedRow(), 0).toString());

            Document dataUpdate = new Document("nombre", this.txtNombre.getText())
                    .append("apellido", this.txtApellido.getText())
                    .append("telefono", this.txtTelefono.getText())
                    .append("direccion", this.txtDireccion.getText())
                    .append("update_at", LocalDateTime.now());

            this.loading(20);
            MongoCollection<Document> collectionPersona = this.getCollection("persona");
            this.loading(30);
            
            if (collectionPersona.findOneAndUpdate(new Document("_id", per), new Document("$set", dataUpdate)) != null) {
                JOptionPane.showMessageDialog(null, "Registro actualizado con exito");
                this.loading(100);
                return;
            }
            JOptionPane.showMessageDialog(null, "Registro no encontado");
            this.loading(0);
            return;
        }
        JOptionPane.showMessageDialog(null, "Selecione un Registro a actualizar");
    }
}
