package wss.websocketserver.security;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 *
 * @author vaganovdv
 */
@Named
@SessionScoped
public class LoginManager implements Serializable 
{

    

    private static final Logger LOG = Logger.getLogger(LoginManager.class.getSimpleName());
    
        /**
         *  Формирование и проверка поля username
         *  (имя для входа в систему)
         */
        @Size(min = 3, max = 30)
        @NotEmpty
        private String username;

        
         /**
         *  Формирование и проверка поля password
         * (пароль для входа в систему)
         */
        @Size(min = 3, max = 30)
        @NotEmpty
        private String password;
        
    
    
    public void checkLogin()
    {
        if (username != null && username.isEmpty() )
        {
            LOG.log(Level.INFO, "Вход пользователя "+ username);
        }  
    
    }        
            
        
        
        
        
        
        
        /**
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
        

}
