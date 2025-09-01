package wss.entitycore;


public class Packet
{

   
    private String command; // Поле команды
    private String type;    // Поле типа данных 
    private String body;    // Контейнер для размещения класса
    
    
     /**
     * @return the command
     */
    public String getCommand()
    {
        return command;
    }

    /**
     * @param command the command to set
     */
    public void setCommand(String command)
    {
        this.command = command;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * @return the body
     */
    public String getBody()
    {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body)
    {
        this.body = body;
    }
}
