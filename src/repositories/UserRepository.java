package repositories;

import models.Pojo_User;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UserRepository {
    private static ArrayList<Pojo_User> usersList = new ArrayList<Pojo_User>();
    private final static String filePath = new File("src/data/userData.csv").getAbsolutePath();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final SerializeAndWrite writer = new SerializeAndWrite();
    public static void addToUsersList(String email, String firstName, String lastName, String password){
        Pojo_User user = new Pojo_User(usersList.size()+1, email, firstName, lastName, password, null, null);
        usersList.add(user);
        Thread t = new Thread(writer);
        t.start();
    }
    public static Pojo_User findUserByEmail(String email){
        for(int i = 0; i<usersList.size(); i++){
            Pojo_User user = usersList.get(i);
            if(user.getEmail().equals(email)){
                return user;
            }
        }
        return null;
    }
    public static void createSession(long userId, String sessionId, LocalDateTime expiryTime){
        for(int i = 0; i<usersList.size(); i++){
            Pojo_User user = usersList.get(i);
            if(user.getUserId() == userId){
                user.setSessionId(sessionId);
                user.setExpiryTime(expiryTime);
                usersList.set(i, user);
                break;
            }
        }
        Thread t = new Thread(writer);
        t.start();
    }
    public static Pojo_User findUserBySessionId(String sessionId){
        for(int i = 0; i<usersList.size(); i++){
            Pojo_User user = usersList.get(i);
            if(user.getSessionId()!=null && user.getSessionId().equals(sessionId)){
                return user;
            }
        }
        return null;
    }
    public static void loadUsers(){
        String line = "";
        String splitBy = ",";
        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            boolean check = true;
            while ((line = br.readLine()) != null) {
                if(check) {
                    check=false;
                    continue;
                }
                String[] userInList = line.split(splitBy);
                try{
                    long userId = Long.parseLong(userInList[0]);
                    String email = userInList[1];
                    String firstName = userInList[2];
                    String lastName = userInList[3];
                    String password = userInList[4];
                    String sessionId = null;
                    LocalDateTime expiryTime = null;
                    if(userInList.length>5){
                        sessionId = userInList[5];
                        expiryTime = LocalDateTime.parse(userInList[6], formatter);
                    }
                    Pojo_User user = new Pojo_User(userId, email, firstName, lastName, password, sessionId, expiryTime);
                    usersList.add(user);
                }
                catch(Exception e){
                    try{
                        System.out.println("Error in schema of product: " + userInList[0]);
                    }
                    catch(Exception ex){
                        System.out.println(ex);
                    }
                    continue;
                }
            }
            br.close();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
    private static synchronized void serializeAndWrite(){
        String outputString = "userId,email,firstName,lastName,password,sessionId,expiryTime\n";
        for(int i = 0; i<usersList.size(); i++){
            Pojo_User user = usersList.get(i);
            outputString += String.valueOf(user.getUserId()) + ','+ user.getEmail() + ',' + user.getFirstName() +
                    ',' + user.getLastName() + ',' + user.getPassword() + ',';
            if(user.getSessionId()!=null){
                outputString += user.getSessionId() + ',' + user.getExpiryTime().format(formatter);
            }
            outputString += '\n';
        }
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            bw.write(outputString);
            bw.flush();
            bw.close();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
    static class SerializeAndWrite implements Runnable{
        public void run(){
            serializeAndWrite();
        }
    }
}
