import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Main {

    private static DoubleHashing<String,Customer> AllCustomer;


    public static void main(String[] args) throws IOException {
        long startTimeMain = System.currentTimeMillis();
        int count =0;
        boolean flag = false;
        String csvDosya = "C:\\Users\\excalıbur\\OneDrive\\Desktop\\supermarket_dataset_50K.csv";

        AllCustomer = new DoubleHashing<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvDosya))) {// read the file
            String satir;
            while ((satir = br.readLine()) != null) {
                String[] parcalar = satir.split(",");
                if (parcalar.length == 4) {
                    if (flag) {
                        String id = parcalar[0];
                        String name = parcalar[1];
                        String date = parcalar[2];
                        String productName = parcalar[3];
                        Customer customer = new Customer(id, name);
                        customer.addTransaction(date, productName);
                        AllCustomer.put(id, customer);
                        count++;
                        System.out.println(count);
                    }
                    if (!flag)
                        flag = true;
                }
            }
        }
        catch (FileNotFoundException e) {
            // If you can't find the file
            System.err.println("Dosya bulunamadı: " + e.getMessage());

        }

        System.out.println("Customer count: " + AllCustomer.size + " \nPurchase count: " + count + "\nCollisions: " + Math.abs(AllCustomer.getCountOfCollision()));
        long endTimeMain = System.currentTimeMillis();
        long elapsedTimeMain = endTimeMain - startTimeMain;
        System.out.println("Time Elapsed: " + elapsedTimeMain); //counting the time
        while (true) {


            csvDosya = "C:\\Users\\excalıbur\\OneDrive\\Desktop\\keys.txt";
            long startTotalTime = System.nanoTime();
            long startTime = System.nanoTime();
            boolean found = false;
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            long minTime = -1;
            long maxTime = -1;
            try (BufferedReader br = new BufferedReader(new FileReader(csvDosya))) { // read the file
                String satir;
                while ((satir = br.readLine()) != null) {
                    startTime = System.nanoTime();
                    found = false;
                    if (AllCustomer.get(satir) != null) {
                        flag = true;
                    }
                    endTime = System.nanoTime();
                    elapsedTime = endTime - startTime;
                    if (minTime == -1 || elapsedTime < minTime)
                        minTime = elapsedTime;
                    if (maxTime == -1 || elapsedTime > maxTime) {
                        maxTime = elapsedTime;
                    }

                }
            }
            catch (FileNotFoundException e) {
                // if you can't find the file.
                System.err.println("Dosya bulunamadı: " + e.getMessage());

            }
            long endTotalTime = System.nanoTime();
            long elapsedTotalTime = endTotalTime - startTotalTime;
            elapsedTotalTime /= 1000;
            System.out.println("Min search Time: " + minTime);
            System.out.println("Max search Time: " + maxTime);
            System.out.println("Average search Time: " + elapsedTotalTime);
            /*boolean flag2 = true;
            while (true) {
                System.out.print("\nContinue Yes(Press y) / No(Press n): ");

                String choice = scan.next().toLowerCase();

                if (choice.equals("y"))
                    break;
                else if (choice.equals("n")) {
                    flag2 = false;
                    break;
                }
            }
            if (!flag2)
                break;*/
            break;
        }
        Scanner scan = new Scanner(System.in);

        while (true) {//loop for the code

            System.out.println("\n Enter the ID: ");
            String CustomerID = scan.next();
            boolean found = true;
            if (AllCustomer.get(CustomerID) != null) {
                AllCustomer.get(CustomerID).display_transaction();
            } else {
                System.out.println("There is no ID in this Excel.");
                found = false;
            }
            while (found) {
                System.out.print("\nDelete Yes(Press y) / No(Press n): ");

                String choice = scan.next().toLowerCase();

                if (choice.equals("y")) {
                    AllCustomer.remove(CustomerID); //removing
                    found = false;
                }
                else if (choice.equals("n")) {
                    break;
                }
            }
            boolean flag2 = true;
            while (true) {
                System.out.print("\nContinue Searching Yes(Press y) / No(Press n): ");//loop

                String choice = scan.next().toLowerCase();

                if (choice.equals("y"))
                    break;
                else if (choice.equals("n")) {
                    flag2 = false;
                    break;
                }
            }
            if (!flag2)
                break;
        }


    }
    public static void Search(String[] input) { // we don't use that but we keep it because we may need that later.
        long startTotalTime = System.nanoTime();
        long startTime = System.nanoTime();
        boolean found = false;
        if (AllCustomer.get(input[0]) != null) {
            found = true;
        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        long minTime = elapsedTime;
        long maxTime = elapsedTime;
        for (int i = 1; i < input.length; i++) {
            startTime = System.nanoTime();
            found = false;
            if (AllCustomer.get(input[i]) != null) {
                AllCustomer.get(input[i]).display_transaction();

            }
            endTime = System.nanoTime();
            elapsedTime = endTime - startTime;
            if (elapsedTime < minTime)
                minTime = elapsedTime;
            else if (elapsedTime > maxTime) {
                maxTime = elapsedTime;
            }
        }

        long endTotalTime = System.nanoTime();
        long elapsedTotalTime = endTotalTime - startTotalTime;
        elapsedTotalTime /= input.length;
        System.out.println("Min search Time: " + minTime);
        System.out.println("Max search Time: " + maxTime);
        System.out.println("Average search Time: " + elapsedTotalTime);
    }

}