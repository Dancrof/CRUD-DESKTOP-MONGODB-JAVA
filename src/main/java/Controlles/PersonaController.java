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
import java.awt.HeadlessException;
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
            JOptionPane.showMessageDialog(null, "error general: " + e.getMessage());
            System.out.println(e);
        }
    }

    @Override
    public void findAllPersons() {
        try {
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
                model.addRow(this.setPersontoArrayObject(newPersona));
                this.loading(70);
                this.loading(100);
            });
            this.loading(100);

        } catch (MongoSecurityException e) {
            JOptionPane.showMessageDialog(null, "Credenciales de la BD incorrectas: " + e.getCredential());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error general: " + e.getMessage());
            System.out.println(e);
        }
    }

    /**
     * Ontengo una coleccion de la base de datos
     *
     * @return MongoCollection<Document>
     * @param String
     */
    private MongoCollection<Document> getCollection(String nameCollection) {
        try {
            MongoClient client = this._connet.connect();
            MongoDatabase bd = client.getDatabase("Personas");
            return bd.getCollection(nameCollection);

        } catch (MongoSecurityException e) {
            JOptionPane.showMessageDialog(null, "Credenciales de la BD incorrectas: " + e.getCredential());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error general: " + e.getMessage());
            System.out.println(e);
        }
        return null;
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
        try {
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

        } catch (MongoSecurityException e) {
            JOptionPane.showMessageDialog(null, "Credenciales de la BD incorrectas: " + e.getCredential());
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "error general: " + e.getMessage());
            System.out.println(e);
        }

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
        try {
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

        } catch (MongoSecurityException e) {
            JOptionPane.showMessageDialog(null, "Credenciales de la BD incorrectas: " + e.getCredential());
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "error general: " + e.getMessage());
            System.out.println(e);
        }

    }

    @Override
    public void findOnePerson() {
        try {           
            this.loading(0);
            this.loading(10);

            this.loading(20);
            MongoCollection<Document> collectionPersona = this.getCollection("persona");
            this.loading(30);
            Document filter = collectionPersona.find(new Document("nombre", txtNombre.getText().toLowerCase())).first();

            if (filter != null) {
                this.Limpiar();
                Persona getFilter = MapperPersonaJsonToObject(filter);
                model.addRow(this.setPersontoArrayObject(getFilter));
                JOptionPane.showMessageDialog(null, "Registro Encontrado con exito");
                this.loading(100);
                return;
            }
            JOptionPane.showMessageDialog(null, "Registro no existe");
            this.loading(0);
            
        } catch (MongoSecurityException e) {
            JOptionPane.showMessageDialog(null, "Credenciales de la BD incorrectas: " + e.getCredential());
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "error general: " + e.getMessage());
            System.out.println(e);
        }
    }

    /**
     * Asigna cada campo de persona a cada posiciom del arreglo de un objeto
     *
     * @return Object[]
     * @param Persona
     */
    private Object[] setPersontoArrayObject(Persona pers) {
        this.obj[0] = pers.getId();
        this.obj[1] = pers.getNombres();
        this.obj[2] = pers.getApellidos();
        this.obj[3] = pers.getTelefono();
        this.obj[4] = pers.getDireccion();
        this.obj[5] = pers.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm"));
        this.obj[6] = pers.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm"));
        return obj;
    }
}
