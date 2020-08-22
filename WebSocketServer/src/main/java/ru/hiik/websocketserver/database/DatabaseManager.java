/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.hiik.websocketserver.database;

import com.google.gson.Gson;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;
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
    
    
    // Настройка подключения к серверу через строку 
    // persistence-unit  и параметр Name 
    // 
    @PersistenceContext(unitName = "STUDENT_DATABASE_PU")
    private EntityManager em;          //  Менеджер сущностей
    private Gson gson = new Gson();
    
    
    @Inject
    private WebSocketServer webSocketServer;
    
    
    /**
     * Обработчик пакетов, предназначенных для БД
     * 
     * @param student
     * @param command
     * @return  экземпляр класса {Packet}
     */
    @Transactional(value = TxType.REQUIRED)
    public Packet processStudent(Student student, String command)
    {
       // Форрмирование пакета для клиента  
       Packet responcePacket = new Packet();
       LOG.log(Level.INFO, "Обработка команды в БД: {"+command+"}");
       
       switch (command)
       {
           case "Удаление из БД":       
           {   
               Student delStudent = deleteStudent(student);
               if (delStudent != null)
               {
                   responcePacket.setCommand(command);
                   responcePacket.setType(Student.class.getCanonicalName());
                   String json = gson.toJson(delStudent);
                   responcePacket.setBody(json);
               }   
           }
           break;
           case "Добавление студента в БД": 
           {
               // Вызов метода сохранения студента 
               Student foundStudent  = saveStudent(student);
               // Возврат сохранненого экземпляра Student на клиент
               if (foundStudent != null)
               {
                   responcePacket.setCommand(command);
                   responcePacket.setType(Student.class.getCanonicalName());
                   String json = gson.toJson(foundStudent);
                   responcePacket.setBody(json);
               }    
               // Возврат на клиент ошибки сохранения
               else
               {
                   responcePacket.setCommand("Ошибка сохранения в БД");
                   responcePacket.setBody("Экземпляр {Student} не сохранен в БД");
               }    
           }break;
           
           default: 
           {
               LOG.log(Level.SEVERE, "Неопознанная команда: {"+command+"}");
           }
       }    
        
       return responcePacket;
    }        
    
    /**
     * Получение полного списка студентов из БД 
     * 
     * @return 
     */
    public List<Student> getAllStudents()
    {
        // Список студентов из базы данных  (подгтовка пустого списка)
        List<Student>  foundStudent  = new ArrayList<>();
        
        // Запрос JPA                   выбор    экземпляр   "из таблицы"  название таблицы класс Student
        //                               |       /            /            /                / 
        foundStudent = em.createQuery("Select student       from       Student student", Student.class)
                      .getResultList();
        
        if (foundStudent.size()>0)
        {
           LOG.log(Level.INFO, "Размер БД студентов {"+foundStudent.size()+"} записей");
        }   
        else
        {
           LOG.log(Level.WARNING, "БД студентов пустая");
        }
        
        return foundStudent;
    }        
    
    
    
    
    /**
     * Сохранение экземпляра класса Student в базе данных 
     * @param student  готовый (не нулевой) экземпляр класса Student
     * @return 
     */
    @Transactional(value = TxType.REQUIRED)
    public Student saveStudent(Student student)
    {
        // Обнуление экземпляра
        Student foundStudent = null;

        LOG.log(Level.INFO, "Сохранение студента {"+student.toString()+"}");
        if (student != null)
        {
            try
            {
                em.persist(student);    // Сохраняет экземпляр {} в базе данных
                                        // экземляр student становится привязанным к БД     
                LOG.log(Level.INFO, "Сохранен студент, идентификатор в БД {"+student.getId()+"}");
                // После операции persist экземляр Student становится "привязанным" к таблице 
                //базы данных, что означает - любое изменение свойств экземпляра сразу 
                // приводят к измнению соответсвующих столбцов в таблице
                em.flush();             // Сохраняет изменения экземпляра на диске 
                if (student.getId() != null)
                {
                    // Поиск студента, сохраненного в базе данных
                    foundStudent = em.find(Student.class, student.getId());
                    LOG.log(Level.INFO, "Сохранен студент {" + foundStudent.toString() + "}");
                }
            } catch (EntityExistsException ex)
            {
                LOG.log(Level.SEVERE, "Ошибка: {" + ex.toString() + "}");
            } catch (IllegalArgumentException ex)
            {
                LOG.log(Level.SEVERE, "Ошибка: {" + ex.toString() + "}");
            } catch (TransactionRequiredException ex)
            {
                LOG.log(Level.SEVERE, "Ошибка: {" + ex.toString() + "}");
            }
            catch (PersistenceException ex)
            {
                LOG.log(Level.SEVERE, "Ошибка: {" + ex.toString() + "}");
            }
           
        } else
        {
            LOG.log(Level.SEVERE, "Ошибка сохранения студента - экземпляр класса пустой");
        }

        return foundStudent;
    }
   
    
        /**
     * Обновление экземпляра студента в БД
     *
     * @param student студен, обновляемый в БД
     * @return
     */
    @Transactional(value = TxType.REQUIRES_NEW)
    public Student updateStudent(Student student)
    {

        Student foundStudent = null;
        try
        {
            //
            //                        название класса        уникальный идентификатор
            //                                   |            /
            foundStudent = em.find(Student.class, student.getId());
            // !!! экземпляр foundStudent статовится привязанным к таблице 

            // 
            // BeanUtils меняет свойства у foundStudent и поскольку foundStudent
            // привязан к БД, то любое изменение свойств сразу меняет записи в 
            // таблице Student 
            //
            
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
    public Student deleteStudent(Student student)
    {
        Student removedStudent = null;
        LOG.log(Level.INFO, "Удаление студента из БД {" + student.toString() + "}...");
        try
        {
            Student foundStudent = em.find(Student.class, student.getId());
            if (foundStudent != null)
            {
                em.remove(foundStudent); // Удаление студента
                LOG.log(Level.INFO, "Удален студент: " + student);
                removedStudent = student;
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
            webSocketServer.sendPacket(packet);
            LOG.log(Level.WARNING, "Ошибка удаления - неверные агрументы: " + ex.toString());
        }
        return removedStudent;
    }
    
    
}
