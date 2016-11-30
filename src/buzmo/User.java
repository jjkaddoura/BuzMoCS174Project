/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buzmo;
import java.lang.String;
/**
 *
 * @author jacob
 */
public class User {
    String email;
    String phone_num;
    String password;
    String screenname;
    boolean is_manager;
    User(String email, String password, boolean is_manager){
        this.email = email;
        this.password = password;
        this.is_manager = is_manager;
    }
    User(String email, String password, boolean is_manager, String screenname){
        this.email = email;
        this.password = password;
        this.screenname = screenname;
        this.is_manager = is_manager;
    }
    
    
}
