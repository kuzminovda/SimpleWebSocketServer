/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.hiik.websocketserver.database;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import org.apache.commons.beanutils.BeanUtils;
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
    public Student saveStudent(Student student)
    {
        em.persist(student);
        em.flush();
        return student; 
         
    }     
   
    @Transactional(value=TxType.REQUIRED)
    public Student updateStudent(Student student)
    {
    
        Student  foundStudent = em.find(Student.class, student.getId());
        
        try
        {
            BeanUtils.copyProperties(foundStudent, student);
            em.flush();
            
        } catch (IllegalAccessException ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return foundStudent;
        
    }        
    
}
