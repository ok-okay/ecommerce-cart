package services;

import java.util.InputMismatchException;
import java.util.Scanner;

public class OnboardingService {
    private static Scanner scanner = new Scanner(System.in);
    public static void userOnboarding(){
        System.out.println("Welcome to the ecommerce-cart");
        System.out.println("Enter 1 to sign in");
        System.out.println("Enter 2 to sign up");
        System.out.println("Enter 3 to search products by name");
        System.out.println("Enter 4 to add items to cart");
        System.out.println("Enter 5 to list cart items");
        System.out.println("Enter 6 to update items in cart");
        System.out.println("Enter 7 to remove items from cart");
    }

    public static int getUserflowRequest(){
        int userInput = 0;
        try{
            userInput = scanner.nextInt();
        }
        catch(InputMismatchException e){
            System.out.println("Input must be an integer, check value and try again");
            System.exit(0);
        }

        if(!(userInput>=1 && userInput<=7)){
            System.out.println("Enter a valid integer between 1 and 7");
            System.exit(0);
        }
        return userInput;
    }
}
