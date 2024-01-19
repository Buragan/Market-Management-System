import java.time.LocalDate;
import java.util.ArrayList;

public class Customer {
    private String Id;
    private String Name;
    private ArrayList<Transaction> transaction;
    public Customer(String Id, String Name) {
        this.Id = Id;
        this.Name = Name;
        this.transaction = new ArrayList<>();
    }
    public String getId() {
        return Id;
    } // we may have to use that

    public String getName() {
        return Name;
    }

    public ArrayList<Transaction> getTransaction() {
        return transaction;
    }


    public void display_transaction(){ //display customer
        System.out.println("\nName Of Customer: " + getName());
        System.out.println("\nBuys : " + transaction.size() + " product from supermarket" );
        for (int i = 0; i < transaction.size(); i++){
            System.out.println((i+1)+ ") " + transaction.get(i).getProductName() + " " + transaction.get(i).getDate());
        }
    }


    public void addTransaction(String date, String productName) {  //adding and sorting the products date
        LocalDate purchaseDate = LocalDate.parse(date);
        int index = 0;

        for (int i = 0; i < transaction.size(); i++) {
            LocalDate currentPurchaseDate = LocalDate.parse(transaction.get(i).getDate());

            if (purchaseDate.isEqual(currentPurchaseDate) || purchaseDate.isAfter(currentPurchaseDate)) {
                index = i;
                break;
            } else {
                index = i + 1;
            }
        }
        transaction.add(index, new Transaction(date, productName));
    }
}