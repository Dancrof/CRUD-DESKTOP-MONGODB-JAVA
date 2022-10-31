/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Interfaces;

import com.mongodb.client.MongoClient;

/**
 *
 * @author Bryan
 */
public interface IConnectionDb {
    
    /**
     * Realiza la conexion a la base de datos
     *
     * @return MongoClient
     */
    MongoClient connect();
}
