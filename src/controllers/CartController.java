package controllers;

import services.CartService;

public class CartController {
    public static void handleUserInput(int userInput){
        try{
            if(userInput==4){
                System.out.println("Attempting add item to cart");
                CartService.addToCart();
                System.out.println("Successfully added item to cart");
            }
            else if (userInput==5){
                System.out.println("Attempting list items from cart");
                CartService.listCartItems();
            }
            else if (userInput==6){
                System.out.println("Attempting update cart");
                CartService.updateCart();
                System.out.println("Successfully updated cart");
            }
            else if(userInput==7){
                System.out.println("Attempting remove item from cart");
                CartService.removeItemFromCart();
                System.out.println("Successfully removed item from cart");
            }
            else{
                System.out.println("Enter an integer between 4 and 7 to interact with cart");
            }
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }
}
