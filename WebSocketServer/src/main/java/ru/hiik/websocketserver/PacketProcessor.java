/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.hiik.websocketserver;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import ru.hiik.entitycore.packet.Packet;
import ru.hiik.entitycore.entity.student.Student;
import ru.hiik.websocketserver.database.DatabaseManager;

/**
 *
 * @author vaganovdv
 */
@ApplicationScoped
public class PacketProcessor
{

    private static final Logger LOG = Logger.getLogger(PacketProcessor.class.getSimpleName());
    private Gson gson = new Gson();

    @Inject
    private DatabaseManager databaseManager;
    @Inject
    private WebSocketServer webSocketServer;
    

    /**
     * Функция обработки пакетов, поступивших от клиентов
     *
     * @param inputJson
     * @return
     */    
    //                               строка, полученная от  WebSocketServer              
    //                                    |
    public Packet processPacket(String inputJson)
    {
        Packet responcePacket = new Packet();  // Создание пустого ответного пакета
        Packet inputPacket = null;             // Создание входного пусто пакета
        try
        {
            
            // Восстановливает класс Packet из строки
            inputPacket = gson.fromJson(inputJson, Packet.class);
            if (inputPacket != null)
            {
                // Извлекаем класс Student из поля body Packet
                if (extractCommand(inputPacket).isPresent())
                {
                    Optional<Packet> opt = extractClass(inputPacket);
                    if (opt.isPresent())
                    {
                        // Передача экземпляра класса Packet на обработку 
                        //
                        routePacket(opt.get());
                    }
                }
            } else
            {

            }
        } catch (JsonParseException ex)
        {

            LOG.log(Level.SEVERE, "Ошибка извлечения пакета {Packet} - json не содержит экземпляра класса {Packet}");
        }
        return responcePacket;
    }

    /**
     * Фукнция извлечения пакетов
     *
     * @param packet
     * @return
     */
    private Optional<String> extractCommand(Packet packet)
    {
        Optional<String> opt = Optional.empty();
        if (packet.getCommand() != null && !packet.getCommand().isEmpty())
        {
            opt = Optional.of(packet.getCommand());
            LOG.log(Level.INFO, "Извлечен пакет c командой: {" + packet.getCommand() + "}");
        } else
        {
            LOG.log(Level.SEVERE, "Ошибка извлечения команды пакета {Packet}: получен пакет с пустой командой");
        }
        return opt;
    }

    /**
     * Функция извлечения класса
     *
     * @param packet
     */
    public  Optional<Packet> extractClass(Packet packet)
    {
        Optional<Packet> opt = Optional.empty();
        if (packet.getType() != null && !packet.getType().isEmpty())
        {
            if (packet.getType() != null && !packet.getType().isEmpty())
            {
                try
                {
                    Class cls = Class.forName(packet.getType());
                    if (cls != null)
                    {
                        if (packet.getBody() != null && !packet.getBody().isEmpty())
                        {
                            opt = Optional.of(packet);
                        } else
                        {
                            LOG.log(Level.SEVERE, "Ошибка извлечения класса {"+packet.getType()+"}");
                        }
                    }
                } catch (ClassNotFoundException ex)
                {
                    System.out.println(ex.toString());
                }
            }
        } else
        {
            LOG.log(Level.SEVERE, "Ошибка извлечения команды: получен пакет с пустой командой");
        }
        return opt;
    }
    
    
    
    

    /**
     * Функция обработки пакетов (маршрутизация пакета)
     *
     * @param obj
     * @param className
     */
    public void routePacket(Packet packet)
    {
        LOG.log(Level.INFO, "Маршрутизация пакета ...");
        try
        {
            switch (packet.getType())
            {
                case "ru.hiik.entitycore.entity.student.Student":
                {
                    
                    // Из поля Body извлекается экземпляр класса Student
                    Student student = gson.fromJson(packet.getBody(), Student.class);
                    if (student != null)
                    {
                        LOG.log(Level.INFO, "Извлечен экземпляр {Student}: " + student.toString());
                        //      экземпляр класса  {databaseManager}   передача команды менеждеру databaseManager 
                        //                               |              |
                        Packet packetToClient = databaseManager.processStudent(student, packet.getCommand());
                        // Отправка результата обработки на клиент
                        this.webSocketServer.sendPacket(packetToClient);
                    }
                }
                break;

                default:
                {
                    LOG.log(Level.SEVERE, "Неизвестный класс для обработки: " + packet.getType());
                }
            }

        } catch (JsonParseException ex)
        {
            LOG.log(Level.SEVERE, "Ошибка GSON извлечения класса из пакета {Packet} : {" + ex.toString() + "}");
        }

    }

   
   
   
    
}
