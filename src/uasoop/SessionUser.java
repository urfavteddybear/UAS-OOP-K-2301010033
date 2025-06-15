/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uasoop;

/**
 *
 * @author Wahyu
 */
public class SessionUser {
    public static int userId;
    public static String username;
    public static String role;
    public static boolean isLoggedIn = false;
    
    public static void setUser(int id, String user, String userRole) {
        userId = id;
        username = user;
        role = userRole;
        isLoggedIn = true;
    }
    
    public static void logout() {
        userId = 0;
        username = "";
        role = "";
        isLoggedIn = false;
    }
    
    public static boolean isAdmin() {
        return "admin".equals(role);
    }
}
