package controllers;

import models.Pojo_Product;
import org.jetbrains.annotations.Nullable;
import services.ProductService;

public class ProductController {
    public static void handleUserInput(int userInput){
        if(userInput==3){
            System.out.println("Attempting list products from catalogue");
            ProductService.filterProducts();
        }
        else{
            System.out.println("Invalid input, enter 3 to search products");
            System.exit(0);
        }
    }
    @Nullable
    public static Integer getProductQuantity(String productId){
        return ProductService.getProductQuantity(productId);
    }
    @Nullable
    public static Pojo_Product getProductFromProductId(String productId){
        return ProductService.getProductFromProductId(productId);
    }
}
