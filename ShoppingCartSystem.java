package PROJECT;

import java.util.Scanner;

public class ShoppingCartSystem {
    static String[][] categories = {
        {"Apples", "Bananas", "Cherries", "Oranges", "Mangoes"},       // Fruits
        {"Bread", "Rice", "Pasta", "Oats", "Quinoa"},                 // Grains
        {"Milk", "Cheese", "Yogurt", "Butter", "Cream"},              // Dairy
        {"Carrots", "Potatoes", "Tomatoes", "Spinach", "Onions"},     // Vegetables
        {"Coca Cola", "Pepsi", "Orange Juice", "Water", "Fanta"}      // Beverages
    };
    static double[][] prices = {
        {150, 120, 200, 180, 250},       // Prices for Fruits (per kg in INR)
        {100, 80, 150, 120, 200},        // Prices for Grains (per kg in INR)
        {54, 55, 27, 20, 36},            // Prices for Dairy (Milk per litre, rest per 100g)
        {60, 40, 80, 100, 70},           // Prices for Vegetables (per kg in INR)
        {40, 35, 60, 20, 40}             // Prices for Beverages (per 250ml in INR)
    };
    static String[] cart = new String[100];
    static double[] cartPrices = new double[100];
    static double[] cartQuantities = new double[100]; // Stores quantities only (no units)
    static String[] cartUnits = new String[100];      // Stores units for each item
    static int cartCount = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\nShopping Cart System");
            System.out.println("1. Add Item to Cart");
            System.out.println("2. Remove Item from Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Generate Detailed Bill");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addItem(scanner);
                    break;
                case 2:
                    removeItem(scanner);
                    break;
                case 3:
                    viewCart();
                    break;
                case 4:
                    boolean continueShopping = generateDetailedBill(scanner);
                    if (!continueShopping) {
                        return;
                    }
                    break;
                case 5:
                    System.out.println("Thank you for shopping!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
        scanner.close();
    }

    public static void addItem(Scanner scanner) {
        System.out.println("\nAvailable Categories:");
        System.out.println("1. Fruits");
        System.out.println("2. Grains");
        System.out.println("3. Dairy");
        System.out.println("4. Vegetables");
        System.out.println("5. Beverages");
        System.out.print("Select a category (1-5): ");
        int categoryChoice = scanner.nextInt();
        if (categoryChoice < 1 || categoryChoice > categories.length) {
            System.out.println("Invalid category. Please try again.");
            return;
        }
        String[] selectedCategory = categories[categoryChoice - 1];
        double[] selectedPrices = prices[categoryChoice - 1];
        System.out.println("\nAvailable Items:");
        for (int i = 0; i < selectedCategory.length; i++) {
            String unit = (categoryChoice == 3 && i == 0) ? "per litre"
                    : (categoryChoice == 3) ? "per 100g"
                    : (categoryChoice == 5) ? "per 250ml"
                    : "per kg";
            System.out.println((i + 1) + ". " + selectedCategory[i] + " - INR " + selectedPrices[i] + " " + unit);
        }
        System.out.print("Select an item to add to your cart (1-" + selectedCategory.length + "): ");
        int itemChoice = scanner.nextInt();
        if (itemChoice < 1 || itemChoice > selectedCategory.length) {
            System.out.println("Invalid item. Please try again.");
            return;
        }
        String item = selectedCategory[itemChoice - 1];
        double unitPrice = selectedPrices[itemChoice - 1];
        double quantity = 1;
        String unit = "kg";
        if (categoryChoice == 3) {
            if (itemChoice == 1) {
                System.out.print("Enter the quantity in litres: ");
                quantity = scanner.nextDouble();
                unit = "litre";
            } else {
                System.out.print("Enter the quantity in grams: ");
                quantity = scanner.nextDouble();
                unitPrice /= 100;
                unit = "grams";
            }
        } else if (categoryChoice == 5) {
            System.out.print("Enter the quantity in 250ml units: ");
            quantity = scanner.nextDouble();
            unit = "250ml";
        } else {
            System.out.print("Enter the quantity in kg: ");
            quantity = scanner.nextDouble();
        }
        double totalPrice = unitPrice * quantity;
        cart[cartCount] = item;
        cartPrices[cartCount] = totalPrice;
        cartQuantities[cartCount] = quantity;
        cartUnits[cartCount] = unit;
        cartCount++;
        System.out.println(item + " (" + quantity + " " + unit + ") has been added to your cart for INR " + totalPrice);
    }

    public static void removeItem(Scanner scanner) {
        if (cartCount == 0) {
            System.out.println("\nYour cart is empty. Nothing to remove.");
            return;
        }
        System.out.println("\nItems in Your Cart:");
        for (int i = 0; i < cartCount; i++) {
            System.out.println((i + 1) + ". " + cart[i] + " - INR " + cartPrices[i]);
        }
        System.out.print("Select an item to remove (1-" + cartCount + "): ");
        int itemChoice = scanner.nextInt();
        if (itemChoice < 1 || itemChoice > cartCount) {
            System.out.println("Invalid item. Please try again.");
            return;
        }
        for (int i = itemChoice - 1; i < cartCount - 1; i++) {
            cart[i] = cart[i + 1];
            cartPrices[i] = cartPrices[i + 1];
            cartQuantities[i] = cartQuantities[i + 1];
            cartUnits[i] = cartUnits[i + 1];
        }
        cart[--cartCount] = null;
        cartPrices[cartCount] = 0;
        cartQuantities[cartCount] = 0;
        cartUnits[cartCount] = null;
        System.out.println("Item has been removed from your cart.");
    }

    public static void viewCart() {
        if (cartCount == 0) {
            System.out.println("\nYour cart is empty.");
        } else {
            System.out.println("\nItems in Your Cart:");
            for (int i = 0; i < cartCount; i++) {
                System.out.println((i + 1) + ". " + cart[i] + " (" + cartQuantities[i] + " " + cartUnits[i] + ") - INR " + cartPrices[i]);
            }
        }
    }

    public static boolean generateDetailedBill(Scanner scanner) {
        if (cartCount == 0) {
            System.out.println("\nYour cart is empty. No bill to generate.");
            return true;
        }
        System.out.println("\n--- Detailed Bill ---");
        System.out.println("--------------------------------------------------");
        System.out.println("Item No. | Item Name        | Quantity        | Total Price");
        System.out.println("--------------------------------------------------");
        double totalAmount = 0;
        for (int i = 0; i < cartCount; i++) {
            System.out.printf("%-8d | %-16s | %-10.2f (%s) | INR %-12.2f\n",
                    (i + 1), cart[i], cartQuantities[i], cartUnits[i], cartPrices[i]);
            totalAmount += cartPrices[i];
        }
        System.out.println("--------------------------------------------------");
        System.out.printf("Total Amount: INR %.2f\n", totalAmount);
        System.out.println("\nWhat would you like to do?");
        System.out.println("1. Pay and Exit");
        System.out.println("2. Cancel and Exit");
        System.out.println("3. Return to Main Menu");
        System.out.print("Enter your choice: ");
        int actionChoice = scanner.nextInt();
        switch (actionChoice) {
            case 1:
                System.out.println("Payment successful! Thank you for shopping.");
                return false;
            case 2:
                System.out.println("Order cancelled. Thank you for visiting.");
                return false;
            case 3:
                System.out.println("Returning to the main menu...");
                return true;
            default:
                System.out.println("Invalid choice. Returning to main menu...");
                return true;
        }
    }
}
