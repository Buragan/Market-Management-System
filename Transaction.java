
public class Transaction {
    private String date;
    private String productName;

    public Transaction(String date, String productName) {
        this.date = date;
        this.productName = productName;
    }


    public String getDate() {
        return date;
    }


    public String getProductName() {
        return productName;
    }

}