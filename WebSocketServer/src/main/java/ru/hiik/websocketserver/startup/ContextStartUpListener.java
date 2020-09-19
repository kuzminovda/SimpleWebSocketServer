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
    
    
    /**
     * Метод, выполняющий действия при старте сервера
     * @param sce 
     */
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
           
        LOG.log(Level.INFO, "Старт сервера... ");
        
        // Получение количества студентов в базе данных 
        int studentCount  = databaseManager.getAllStudents().size();
        
        LOG.log(Level.INFO, "В базе данных {"+studentCount+"} студетов");
        
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
      
              
        LOG.log(Level.INFO, "Сервер остановлен");
    }
    
}
