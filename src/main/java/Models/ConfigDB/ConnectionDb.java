/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.ConfigDB;

import Models.ConfigDB.Constantes.Constantes;
import Models.Interfaces.IConnectionDb;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;

/**
 *
 * @author Bryan
 */
public class ConnectionDb implements IConnectionDb {

    private final String URL_BD_CONNECTION = Constantes.URL_CONNET_DB;
    @Override
    public MongoClient connect() {
        try {
            return MongoClients.create(this.URL_BD_CONNECTION);
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "error al crear un conexion con la bd: " + e.getMessage());
        }
        return null;
    }
}
