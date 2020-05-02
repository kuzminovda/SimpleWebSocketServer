/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.hiik.websocketserver.database;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import ru.hiik.entitycore.entity.student.Student;

/**
 *
 * @author vaganovdv
 */

@ApplicationScoped
public class DatabaseManager
{
    
    @PersistenceContext(unitName = "STUDENT_DATABASE_PU")
    private EntityManager em;
    
    @Transactional(value=TxType.REQUIRED)
    public void saveStudent(Student student)
    {
         em.persist(student);
    }     
   
    
}
