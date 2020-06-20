/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.hiik.websocketserver.startup;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import ru.hiik.entitycore.entity.student.Student;
import ru.hiik.websocketserver.database.DatabaseManager;

/**
 *
 * @author vaganovdv
 */
public class ContextStartUpListener  implements ServletContextListener
{

    private static final Logger LOG = Logger.getLogger(ContextStartUpListener.class.getName());

      // Создание пустых экземпляров
        Student st1 = new Student();
        Student st2 = new Student();
    
    
    @Inject
    private DatabaseManager databaseManager;
    
    
    /**
     * Метод, выполняющий действия при старте сервера
     * @param sce 
     */
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
           
        LOG.log(Level.INFO, "Старт сервера... ");
        
      
        
        // Заполнение полей 1 экземпляра
        st1.setFirstName("Олег");
        st1.setLastName("Иванов");
        st1.setMiddleName("Иванович");
        st1.setYearOfstudy(2);
  
        // Заполнение полей 2 экземпляра
        st2.setFirstName("Петр");
        st2.setLastName("Петровский");
        st2.setMiddleName("Петрович");
        st2.setYearOfstudy(2);
        
        
        // Сохранение сфорсмированных экземпляров в БД
        // Обращение к класу {databaseManager} для сохранения студентов в БД
        Student student1 = databaseManager.saveStudent(st1);
        Student student2 = databaseManager.saveStudent(st2);
        
        st1 = student1;
        st2 = student2;
        
        
        LOG.log(Level.INFO, "Сохранен студент в БД, идентификатор {"+student1.getId()+"}");
        LOG.log(Level.INFO, "Сохранен студент в БД, идентификатор {"+student2.getId()+"}");      
        
        LOG.log(Level.INFO, "Сервер стартовал");
        
    }
  
    
    
    
    /**
     * Метод, выполняющийся при остановке сервера
     * @param sce 
     */
     @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        LOG.log(Level.INFO, "Остановка сервера... ");
      
        // Обновление поля {FirstName} у экземпляра st1
        st1.setFirstName("Алексей");
        st1.setLastName("Иванов");
        st1.setYearOfstudy(1);
        
        databaseManager.updateStudent(st1);
        
        LOG.log(Level.INFO, "Сервер остановлен");
    }
    
}
