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

    
    @Inject
    private DatabaseManager databaseManager;
    
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
           
        LOG.log(Level.INFO, "Старт сервера... ");
  
        /*
        
        Student st = new Student();
        st.setFirstName("Олег");
        st.setLastName("Иванов");
        st.setYearOfstudy(1);
        
        databaseManager.saveStudent(st);
        */
        
        LOG.log(Level.INFO, "Сервер стартовал");
        
    }
  
     @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        LOG.log(Level.INFO, "Остановка сервера... ");
  
        LOG.log(Level.INFO, "Сервер остановлен");
    }
    
}
