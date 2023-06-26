package services;

import controllers.ProductController;
import controllers.UserController;
import models.Pojo_Cart;
import models.Pojo_Product;
import repositories.CartRepository;

import java.util.ArrayList;
import java.util.Scanner;

public class CartService {
    private static final Scanner scanner = new Scanner(System.in);
    private static long getUserIdFromSessionId(){
        System.out.println("Enter sessionId");
        String sessionId = scanner.nextLine();
        Long userId = UserController.validateSession(sessionId);
        if(userId == null){
            throw new IllegalArgumentException("User unauthorized, try signIn or signUp");
        }
        return userId;
    }
    private static int getMaxQuantityFromProductId(String productId){
        Integer maxQuantity = ProductController.getProductQuantity(productId);
        if(maxQuantity == null){
            throw new IllegalArgumentException("ProductID wrong, check products list and try again");
        }
        return maxQuantity;
    }
    private static int getQuantityFromUser(int maxQuantity){
        System.out.println("Enter quantity");
        int quantity;
        try {
            quantity = Integer.parseInt(scanner.nextLine());
        }
        catch(Exception e){
            throw new IllegalArgumentException("Quantity has to be an integer. Try again");
        }
        if(quantity<=0){
            throw new IllegalArgumentException("Quantity has to be a positive integer. Try again");
        }

        if(quantity>maxQuantity){
            throw new IllegalArgumentException("Quantity exceeds the maximum permissible amount of " + maxQuantity);
        }
        return quantity;
    }
    private static void displayCart(Pojo_Product[] productsList, ArrayList<Pojo_Cart> userCart){
        for(int i = 0; i<userCart.size(); i++){
            Pojo_Product product = productsList[i];
            Pojo_Cart cart = userCart.get(i);
            try{
                System.out.printf("%-15s %-15s %-20s %-10.2f %-15.2f %-20d %-10d", product.getProductId(),
                        product.getProductName(), product.getInventoryStatus().toString(),
                        product.getMrp(), product.getDiscount(), cart.getQuantity(), product.getMaxQuantity());
                System.out.println();
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    public static void addToCart(){
        long userId = getUserIdFromSessionId();

        System.out.println("Enter product ID");
        String productId = scanner.nextLine();

        if(!CartRepository.isResourceLoadStatus()){
            System.out.println("CartDB is still loading, try after a while");
            System.exit(0);
        }

        Pojo_Cart cartItem = CartRepository.getItemByUserIdProductId(userId, productId);
        if(cartItem!=null){
            throw new IllegalArgumentException("Product already exists in cart, try update or delete");
        }

        int maxQuantity = getMaxQuantityFromProductId(productId);
        int quantity = getQuantityFromUser(maxQuantity);

        CartRepository.addToCart(userId, productId, quantity);
    }
    public static void listCartItems(){
        long userId = getUserIdFromSessionId();
        if(!CartRepository.isResourceLoadStatus()){
            System.out.println("CartDB is still loading, try after a while");
            System.exit(0);
        }
        ArrayList<Pojo_Cart> userCart = CartRepository.filterCartByUserId(userId);
        if(userCart.size()==0){
            System.out.println("No items in cart, try adding using add to cart");
        }
        else{
            Pojo_Product [] products = new Pojo_Product[userCart.size()];
            for(int i = 0; i<userCart.size(); i++){
                String productId = userCart.get(i).getProductId();
                Pojo_Product product = ProductController.getProductFromProductId(productId);
                products[i] = product;
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-15s %-15s %-20s %-10s %-15s %-20s %-10s", "Product_Id", "Product_Name", "Inventory_Status", "MRP", "Discount(%)", "Quantity_In_Cart", "Max_Quantity");
            System.out.println();
            System.out.println("-------------------------------------------------------------------------------------------------------------------");
            displayCart(products, userCart);
            System.out.println("-------------------------------------------------------------------------------------------------------------------");
            System.out.println();
        }
    }
    public static void updateCart(){
        long userId = getUserIdFromSessionId();
        System.out.println("Enter product ID");
        String productId = scanner.nextLine();

        if(!CartRepository.isResourceLoadStatus()){
            System.out.println("CartDB is still loading, try after a while");
            System.exit(0);
        }

        Pojo_Cart cartItem = CartRepository.getItemByUserIdProductId(userId, productId);
        if(cartItem==null){
            throw new IllegalArgumentException("Product does not exist in cart, try add product");
        }

        int maxQuantity = getMaxQuantityFromProductId(productId);
        int quantity = getQuantityFromUser(maxQuantity);

        CartRepository.updateCart(userId, productId, quantity);
    }
    public static void removeItemFromCart(){
        long userId = getUserIdFromSessionId();
        System.out.println("Enter product ID");
        String productId = scanner.nextLine();

        if(!CartRepository.isResourceLoadStatus()){
            System.out.println("CartDB is still loading, try after a while");
            System.exit(0);
        }

        Pojo_Cart itemToRemove = CartRepository.getItemByUserIdProductId(userId, productId);
        if(itemToRemove==null){
            throw new IllegalArgumentException("Product not in cart");
        }
        CartRepository.deleteFromCart(userId, productId);
    }
}
