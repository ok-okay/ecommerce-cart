import controllers.CartController;
import controllers.ProductController;
import controllers.UserController;
import repositories.CartRepository;
import repositories.ProductRepository;
import repositories.UserRepository;
import services.OnboardingService;

import java.io.File;

public class Main {
    static class LoadProducts extends Thread {
        public void run() {
            ProductRepository.loadProducts();
            ProductRepository.setResourceLoadStatus(true);
        }
    }
    static class LoadUsers extends Thread {
        public void run() {
            UserRepository.loadUsers();
            UserRepository.setResourceLoadStatus(true);
        }
    }
    static class LoadCart extends Thread {
        public void run() {
            CartRepository.loadCart();
            CartRepository.setResourceLoadStatus(true);
        }
    }
    public static void main(String[] args) {
        Thread t1 = new LoadProducts();
        Thread t2 = new LoadUsers();
        Thread t3 = new LoadCart();

        t1.setPriority(Thread.MAX_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);
        t3.setPriority(Thread.MAX_PRIORITY);

        t1.start();
        t2.start();
        t3.start();

        OnboardingService.userOnboarding();
        int userInput = OnboardingService.getUserflowRequest();
        if(userInput==1 || userInput==2){
            UserController.handleUserInput(userInput);
        }
        else{
            if(userInput==3){
                ProductController.handleUserInput(userInput);
            }
            else{
                CartController.handleUserInput(userInput);
            }
        }
    }
}