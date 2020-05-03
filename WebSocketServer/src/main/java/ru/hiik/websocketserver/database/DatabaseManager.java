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
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import org.apache.commons.beanutils.BeanUtils;
import ru.hiik.entitycore.entity.student.Student;
import ru.hiik.entitycore.packet.Packet;
import ru.hiik.websocketserver.WebSocketServer;

/**
 *
 * @author vaganovdv
 */

@ApplicationScoped
public class DatabaseManager
{

    private static final Logger LOG = Logger.getLogger(DatabaseManager.class.getSimpleName());
    
    @PersistenceContext(unitName = "STUDENT_DATABASE_PU")
    private EntityManager em;
    
    @Inject
    private WebSocketServer webSocketServer;
    
    
    /**
     * Обработчик пакетов, предназначенных для БД
     * 
     * @return 
     */
    public Packet processStudent(Student student, String command)
    {
       Packet responcePacket = new Packet();
       LOG.log(Level.SEVERE, "Обработка команды в БД: {"+command+"}");
       
       switch (command)
       {
           case "Удаление из БД": deleteStudent(student); break;
           
           default: 
           {
               LOG.log(Level.SEVERE, "Неопознанная команда: {"+command+"}");
           }
       }    
        
       return responcePacket;
    }        
    
    
    
    
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

    
    
    @Transactional(value = TxType.REQUIRED)
    public void deleteStudent(Student student)
    {
        LOG.log(Level.INFO, "Удаление студента из БД {" + student.toString() + "}...");
        try
        {
            Student foundStudent = em.find(Student.class, student.getId());
            if (foundStudent != null)
            {
                em.remove(foundStudent);
                LOG.log(Level.INFO, "Удален студент: " + student);
            } else
            {
                LOG.log(Level.WARNING, "Удаление не возможно - в БД не обнаружен cтудент: " + student.toString());
            }

        } catch (IllegalArgumentException ex)
        {
            Packet packet = new Packet();
            packet.setType(String.class.getCanonicalName());
            packet.setCommand("Ошибка");
            packet.setBody("Ошибка удаления студента {"+student.toString()+"} , описание ошибки: {"+ex.toString()+"}");
            this.webSocketServer.sendPacket(packet);
            
            LOG.log(Level.WARNING, "Ошибка удаления - неверные агрументы: " + ex.toString());
        }
    }
    
    
}
