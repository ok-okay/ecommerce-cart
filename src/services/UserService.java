package services;

import models.Pojo_User;
import repositories.UserRepository;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserService {
    private static Scanner scanner = new Scanner(System.in);
    private static boolean emailValidityCheck(String email){
        if(!email.equals("")) {
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";

            Pattern pat = Pattern.compile(emailRegex);
            if(pat.matcher(email).matches()) {
                return true;
            }
        }
        return false;
    }
    private static boolean nameValidation(String name){
        if(!name.equals("")) {
            String nameRegex = "^[\\p{L} .'-]+$";

            Pattern pat = Pattern.compile(nameRegex);
            if(pat.matcher(name).matches()) {
                return true;
            }
        }
        return false;
    }
    private static boolean passwordValidation(String password){
        if(!password.equals("")){
            return true;
        }
        return false;
    }
    public static Long validateSession(String sessionId){
        Pojo_User user = UserRepository.findUserBySessionId(sessionId);
        if(user!=null){
            LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
            if(user.getExpiryTime().isAfter(currentTime)){
                return user.getUserId();
            }
        }
        return null;
    }
    private static String generateHash(String originalString){
        String hashedString = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(originalString.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            hashedString = hexString.toString();
            return hashedString;
        }catch(Exception e) {
            throw new RuntimeException("Error generating hash");
        }
    }
    public static void signInUser(){
        System.out.println("Enter email ID");
        String email = scanner.nextLine();
        if(!emailValidityCheck(email)){
            throw new IllegalArgumentException("Enter a valid email ID");
        }

        Pojo_User user = UserRepository.findUserByEmail(email);
        if(user==null){
            throw new IllegalArgumentException("No user found, use the signUp path instead");
        }
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime expiryTime = currentTime.plusMinutes(30);

        System.out.println("Enter password");
        String password = scanner.nextLine();
        String hashedPassword = generateHash(password);

        if(!user.getPassword().equals(hashedPassword)){
            throw new IllegalArgumentException("Incorrect password, try again");
        }

        String sessionId = generateHash(expiryTime.format(DateTimeFormatter.ISO_DATE_TIME));

        UserRepository.createSession(user.getUserId(), sessionId, expiryTime);
        System.out.println("SessionID: " + sessionId);
    }
    public static void signupUser(){
        System.out.println("Enter email ID");
        String email = scanner.nextLine();
        if(!emailValidityCheck(email)){
            throw new IllegalArgumentException("Enter a valid email ID");
        }

        if(UserRepository.findUserByEmail(email)==null){
            System.out.println("Enter first name");
            String firstName = scanner.nextLine();
            if(!nameValidation(firstName)){
                throw new IllegalArgumentException("Enter a valid first name");
            }

            System.out.println("Enter last name");
            String lastName = scanner.nextLine();
            if(!nameValidation(lastName)){
                throw new IllegalArgumentException("Enter a valid last name");
            }

            System.out.println("Enter password");
            String password = scanner.nextLine();
            if(!passwordValidation(password)){
                throw new IllegalArgumentException("Enter a valid password");
            }
            String hashedPassword = generateHash(password);
            UserRepository.addToUsersList(email, firstName, lastName, hashedPassword);
        }
        else{
            throw new IllegalArgumentException("Email already exists, use the signIn option");
        }


    }
}