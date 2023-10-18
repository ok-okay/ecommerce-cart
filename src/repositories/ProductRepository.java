package repositories;

import models.InventoryStatus;
import models.Pojo_Product;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ProductRepository {
    private final static String filePath = new File("src/data/productData.csv").getAbsolutePath();
    private static ArrayList<Pojo_Product> productsList = new ArrayList<Pojo_Product>();
    private static boolean resourceLoadStatus = false;

    public static boolean isResourceLoadStatus() {
        return resourceLoadStatus;
    }

    public static void setResourceLoadStatus(boolean resourceLoadStatus) {
        ProductRepository.resourceLoadStatus = resourceLoadStatus;
    }

    public static ArrayList<Pojo_Product> filterProductsByName(String searchTerm){
        ArrayList<Pojo_Product> searchResults = new ArrayList<Pojo_Product>();
        for(int i = 0; i<productsList.size(); i++){
            if(productsList.get(i).getProductName().toLowerCase().contains(searchTerm)){
                searchResults.add(productsList.get(i));
            }
        }
        return searchResults;
    }
    @Nullable
    public static Pojo_Product getProductFromProductId(String productId){
        for(int i = 0; i<productsList.size(); i++){
            if(productsList.get(i).getProductId().equals(productId)){
                return productsList.get(i);
            }
        }
        return null;
    }
    @Nullable
    public static Integer getProductQuantity(String productId){
        for(int i = 0; i<productsList.size(); i++){
            if(productsList.get(i).getProductId().equals(productId)){
                return productsList.get(i).getMaxQuantity();
            }
        }
        return null;
    }
    public static void loadProducts(){
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
                String[] productInList = line.split(splitBy);
                try{
                    String productId = productInList[0];
                    String productName = productInList[1];
                    InventoryStatus inventoryStatus = InventoryStatus.valueOf(productInList[2]);
                    double mrp = Double.parseDouble(productInList[3]);
                    double discount = Double.parseDouble(productInList[4]);
                    int maxQuantity = 0;
                    if(productInList.length>5){
                        maxQuantity = Integer.parseInt(productInList[5]);
                    }
                    Pojo_Product product = new Pojo_Product(productId, productName, inventoryStatus, mrp, discount, maxQuantity);
                    productsList.add(product);
                }
                catch(Exception e){
                    try{
                        System.out.println("Error in schema of product: " + productInList[0]);
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
}