/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Interfaces;

import Models.Persona;

/**
 *
 * @author Bryan
 */
public interface IPersonaController {
   /**
     * Realizo un registro de una persona a la base de datos
     *
     * @return void
     * @param Persona
     */
    void insertOnePersonDb(Persona persona);
    
    /**
     * Obtengo todos los registros de la base de datos y los mapeo a la entidad persona
     *
     * @return void
     */
    void findAllPersons();
    
    /**
     * Elimina un Registro persona de la base de datos
     *
     * @return void
     */
    void deleteOnePerson();
    
    /**
     * Actulizar un persona de la base de datos
     *
     * @return void
     */
    void updateOnePerson();
    
    /**
     * Busca un persona en especifica de l base de datos
     *
     * @return void
     */
    void findOnePerson();
}
