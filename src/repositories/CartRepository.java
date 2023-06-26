package repositories;

import models.Pojo_Cart;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;

public class CartRepository {
    private static ArrayList<Pojo_Cart> cartList = new ArrayList<Pojo_Cart>();
    private final static String filePath = new File("src/data/cartData.csv").getAbsolutePath();
    private static final SerializeAndWrite writer = new SerializeAndWrite();
    public static void addToCart(long userId, String productId, int quantity){
        Pojo_Cart cartItem = new Pojo_Cart(userId, productId, quantity);
        cartList.add(cartItem);
        Thread t = new Thread(writer);
        t.start();
    }
    public static void updateCart(long userId, String productId, int quantity){
        for(int i = 0; i<cartList.size(); i++){
            Pojo_Cart cartItem = cartList.get(i);
            if(cartItem.getUserId()==userId && cartItem.getProductId().equals(productId)){
                cartItem.setQuantity(quantity);
                cartList.set(i, cartItem);
                break;
            }
        }
        Thread t = new Thread(writer);
        t.start();
    }
    public static void deleteFromCart(long userId, String productId){
        for(int i = 0; i<cartList.size(); i++){
            Pojo_Cart cartItem = cartList.get(i);
            if(cartItem.getProductId().equals(productId) && cartItem.getUserId()==userId){
                cartList.remove(i);
                Thread t = new Thread(writer);
                t.start();
                break;
            }
        }
    }
    public static ArrayList<Pojo_Cart> filterCartByUserId(long userId){
        ArrayList<Pojo_Cart> userCart = new ArrayList<Pojo_Cart>();
        for(int i = 0; i<cartList.size(); i++){
            Pojo_Cart cartItem = cartList.get(i);
            if(cartItem.getUserId()==userId){
                userCart.add(cartItem);
            }
        }
        return userCart;
    }
    @Nullable
    public static Pojo_Cart getItemByUserIdProductId(long userId, String productId){
        for(int i = 0; i<cartList.size(); i++){
            Pojo_Cart cartItem = cartList.get(i);
            if(cartItem.getUserId()==userId && cartItem.getProductId().equals(productId)){
                return cartItem;
            }
        }
        return null;
    }
    public static void loadCart(){
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
                String[] cartItems = line.split(splitBy);
                try{
                    long userId = Long.parseLong(cartItems[0]);
                    String productId = cartItems[1];
                    int quantity = Integer.parseInt(cartItems[2]);
                    Pojo_Cart cart = new Pojo_Cart(userId, productId, quantity);
                    cartList.add(cart);
                }
                catch(Exception e){
                    try{
                        System.out.println("Error in schema of product: " + cartItems[0]);
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
        String outputString = "userId,productId,quantity\n";
        for(int i = 0; i<cartList.size(); i++){
            Pojo_Cart cartItem = cartList.get(i);
            outputString += String.valueOf(cartItem.getUserId()) + ',' + cartItem.getProductId() + ','
                + String.valueOf(cartItem.getQuantity());
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
