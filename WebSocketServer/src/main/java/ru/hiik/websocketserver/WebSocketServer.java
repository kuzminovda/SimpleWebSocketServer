/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.hiik.websocketserver;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import ru.hiik.entitycore.packet.Packet;

/**
 *
 * @author vaganovdv
 */
//  Класс доступен на протяжении всего жизненного цикла приложения
//   |
// 
@ApplicationScoped
//
//    Сервер, реализующий протокол Websocket
//      |
@ServerEndpoint("/websocket")
public class WebSocketServer
{

    private static final Logger LOG = Logger.getLogger(WebSocketServer.class.getSimpleName());
    
    //   Структура для Map (карта) хранения сведений о подключенных пользователях (сесии пользователей)
    //   
    //   Пример карты (связанные пары):
    //        ключ                          значение (экземпляр класса Session)
    //         |                                            |
    //      EF91381F-B90C-4593-8886-C0F03C91AE24 - сессия пользователя 1 
    //      8632E55F-4BB8-468C-89AF-4886E597016B - сессия пользователя 2 
    //      6CBAB35C-A89E-4952-AB0A-5256ED0279E6 - сессия пользователя 3 
    //
    //                              ключ  значение   имя структуры   может использоваться в режиме работы нескольких нитей
    //                               |      |        |               /
    private static ConcurrentMap <String, Session> users = new ConcurrentHashMap<String, Session>();
    private Gson gson = new Gson();
    
    @Inject 
    private PacketProcessor packetProcessor;
    
    // Событие OnOpen возникает в случае подключения к серверу клиента
    // и создания новой  сессии с уникальным идентификатором
    @OnOpen
    //                          экземпляр сесси с клиентом Websocket
    //                            |
    public void onOpen(Session session)
    {
        // Печать идентификатора сесиии
        LOG.log(Level.INFO, "Подключено к серверу: " + session.getId().toUpperCase());
        //                     ключ        преобразовать в заглавные   экземпляр класса сессии
        //                      |           /                          /
        users.putIfAbsent(session.getId().toUpperCase(),             session);
    }

    
    
    @OnClose
    public void onClose(Session session)
    {
        users.remove(session.getId().toUpperCase());
    }

    @OnError
    public void onError(Throwable t)
    {

    }

    
    
    /*
     OnMessage - обработка события получения сервером сообщения от клиента
    */
    @OnMessage
    //                сообщение от клиента       экземпляр класса сессии с клиентом
    //                             |           /
    public void onMessage(String message, Session session)
    {
        LOG.log(Level.INFO,"Получено сообщение от клиента: " + message);
        if (message != null )
        {
            // Вызов функции обработки пакета 
            packetProcessor.processPacket(message);
        }   
    }
    
    /**
     * Посылка пакета 
     */
    public void sendPacket(Packet packet)
    {
        LOG.log(Level.INFO, "Отправка пакета...");
        if (packet != null)
        {
            String json = gson.toJson(packet);
            
            if (json != null && !json.isEmpty())
            {
                users.values().forEach(s ->
                {
                    try
                    {
                        s.getBasicRemote().sendText(json);
                        LOG.log(Level.INFO, "Отправлен пакет: {"+json+"}");
                    } catch (IOException ex)
                    {
                        LOG.log(Level.SEVERE, "Ошибка отправки пакета: {"+ex.toString()+"}");
                    }

                });
            }
        }
    }        
    
    
    
}
