package controllers;

import services.UserService;

public class UserController {
    public static void handleUserInput(int userInput){
        try{
            if(userInput==1){
                System.out.println("Attempting user signIn");
                UserService.signInUser();
                System.out.println("User signed in successfully");
            }
            else if (userInput==2) {
                System.out.println("Attempting user signUp");
                UserService.signupUser();
                System.out.println("User created successfully");
            }
            else {
                System.out.println("Invalid input, enter 1 for signup and 2 for signIn");
                System.exit(0);
            }
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }
    public static Long validateSession(String sessionId){
        return UserService.validateSession(sessionId);
    }
}
