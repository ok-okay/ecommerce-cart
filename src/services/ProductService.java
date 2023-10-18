package services;

import models.Pojo_Product;
import org.jetbrains.annotations.Nullable;
import repositories.ProductRepository;

import java.util.ArrayList;
import java.util.Scanner;

public class ProductService {
    private static Scanner scanner = new Scanner(System.in);
    private static void displayProducts(ArrayList<Pojo_Product> productsList){
        for(int i = 0; i<productsList.size(); i++){
            Pojo_Product product = productsList.get(i);
            System.out.printf("%-15s %-15s %-20s %-15.2f %-15.2f %-10d", product.getProductId(),
                    product.getProductName(), product.getInventoryStatus().toString(),
                    product.getMrp(), product.getDiscount(), product.getMaxQuantity());
            System.out.println();
        }
    }
    public static void filterProducts(){
        System.out.println("Enter searchTerm");
        String searchTerm = scanner.nextLine();

        if(!ProductRepository.isResourceLoadStatus()){
            System.out.println("ProductsDB is still loading, try after a while");
            System.exit(0);
        }

        ArrayList<Pojo_Product> searchResults = ProductRepository.filterProductsByName(searchTerm);

        if(searchResults.isEmpty()){
            System.out.println("No products matching the given searchTerm: " + searchTerm);
            System.out.println("Try a different search");
        }
        else{
            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.printf("%-15s %-15s %-20s %-15s %-15s %-10s", "Product_Id", "Product_Name", "Inventory_Status", "MRP", "Discount(%)", "Max_Quantity");
            System.out.println();
            System.out.println("--------------------------------------------------------------------------------------------------");
            displayProducts(searchResults);
            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.println();
        }
    }
    @Nullable
    public static Integer getProductQuantity(String productId){
        if(!ProductRepository.isResourceLoadStatus()){
            System.out.println("ProductsDB is still loading, try after a while");
            System.exit(0);
        }
        return ProductRepository.getProductQuantity(productId);
    }
    @Nullable
    public static Pojo_Product getProductFromProductId(String productId){
        if(!ProductRepository.isResourceLoadStatus()){
            System.out.println("ProductsDB is still loading, try after a while");
            System.exit(0);
        }
        return ProductRepository.getProductFromProductId(productId);
    }
}
