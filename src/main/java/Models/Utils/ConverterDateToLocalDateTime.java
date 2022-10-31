/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author Bryan
 */
public class ConverterDateToLocalDateTime {

    /**
     * Transformo un Date a un LocalDateTime
     *
     * @return LocalDateTime
     */
    public static LocalDateTime transforToLocalDateTime(Date date) {
        if (date != null) {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return null;
    }
    
    /**
     * Transformo un Date a un LocalDateTime
     *
     * @return LocalDateTime
     */
    public static LocalDateTime transforToLocalDateTimeV2(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
