public class DoubleHashing<Key,Value> implements DictionaryInterface<Key,Value>{

    private HashEntry<Key,Value>[] table;
    private static double LOAD_FACTOR_THRESHOLD= 0.5;

    private int initial_size=16;

    public static int countOfCollision;

    public int size;

    public DoubleHashing(){
        @SuppressWarnings("unchecked")
        HashEntry<Key,Value>[] temp = (HashEntry<Key,Value>[])new HashEntry[NextPrime(initial_size)];
        table = temp;
        countOfCollision = 0;
        size = 0;
    }

    private int PAF(Key key){//Horner's rule algorithm
        int hash = 0;
        String[] temp = String.valueOf(key).replace("-", "").split("");
        int z = 33;

        for (int i = 0; i < temp.length; i++) {
            char next_char = temp[i].charAt(0);
            int value = next_char ;
            hash = (hash * z + value) % Integer.MAX_VALUE;
        }

        hash = hash % table.length;


        if (hash < 0) {
            hash = Math.abs(hash);
        }

        return hash % table.length;
    }
    private int SSF(Key key) {//SSF Algorithm
        return hashCode(String.valueOf(key)) % table.length;
    }
    public int hashCode(String input) {
        int hash = 0;
        for (char character : input.toCharArray()) {
            hash += (int) character;
        }
        return hash;
    }

    private int hash2(int hash1) {

        return 7 - (hash1 % 7);
    }


    public Value get(Key key){ //getting value
        Value result = null;
        int index;
        index = PAF(key);
        index = locate(index,key);

        if (index != -1){
            result =  table[index].getValue();
        }
        return result;
    }

    public int locate(int index,Key key){ //locate the index
        try {
            boolean found = false;
            int firstIndex = index;

            while (!found && (table[index] != null)) {

                if (!table[index].isIn() && (String.valueOf(key)).equals(String.valueOf(table[index].getKey()))) {
                    found = true;
                } else {
                    index = (index + hash2(firstIndex)) % table.length;
                }
            }
            int result = -1;
            if (found) {
                result = index;
            }
            return result;
        } catch (ArrayIndexOutOfBoundsException e) {

            System.err.println("ArrayIndexOutOfBoundsException: " + e.getMessage());
            return -1;
        }
    }

    private int probe(int index, Key key) { //probing
        try {
            int hash = hash2(index);

            while (table[index] != null && !table[index].isIn()) {
                if (String.valueOf(key).equals(String.valueOf(table[index].getKey()))) {
                    return index;
                }
                index = (index + hash) % table.length;
                countOfCollision += 1;
            }

            return index;
        } catch (Exception e) {

            System.err.println("Exception: " + e.getMessage());
            return -1;
        }
    }

    public void put(Key key,Value value){ //put the customer
        try {
            if ((key == null) || (value == null)) {
                throw new IllegalArgumentException("Cannot add null to a dictionary.");
            } else {
                Value oldValue;
                int index;

                index = PAF(key);
                index = probe(index, key);

                assert (index >= 0) && (index < table.length);

                if ((table[index] == null) || table[index].isIn()) {
                    table[index] = new HashEntry<>(key, value);
                    size++;
                } else {
                    oldValue = table[index].getValue();
                    ((Customer) oldValue).addTransaction(((Customer) value).getTransaction().get(0).getDate(), ((Customer) value).getTransaction().get(0).getProductName());
                    table[index].setValue(oldValue);
                }

                if ((double) size / (double) table.length > LOAD_FACTOR_THRESHOLD)
                    resize(table.length);
            }
        } catch (IllegalArgumentException e) {

            System.err.println("IllegalArgumentException: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    public void resize(int capacity){ //resizing the hashtable
        try {
            HashEntry<Key, Value>[] oldTable = table;
            int oldSize = capacity;
            int newSize = NextPrime(oldSize * 2);
            @SuppressWarnings("unchecked")
            HashEntry<Key, Value>[] temp = (HashEntry<Key, Value>[]) new HashEntry[newSize];
            table = temp;
            size = 0;
            for (int i = 0; i < oldSize; i++) {
                if ((oldTable[i] != null)) {
                    put(oldTable[i].getKey(), oldTable[i].getValue());
                }
            }
        } catch (Exception e) {
            // Genel bir exception durumu ile ilgili bir hata mesajı yazdırabilir veya uygun bir şekilde ele alabilirsiniz.
            System.err.println("Exception during resize: " + e.getMessage());
            // İsterseniz burada başka bir işlem yapabilir veya uygun bir şekilde devam edebilirsiniz.
        }
    }

    private static int NextPrime(int n) {//finding prime number
        if (n < 2) {
            return 2;
        }
        int nextNumber=0;
        if(n%2==0)
            nextNumber=n+1;
        else{
            nextNumber=n;
        }

        while (!isPrime(nextNumber)) {
            nextNumber += 2;
        }
        return nextNumber;
    }

    private static boolean isPrime(int number) { //is it prime?
        if (number < 2) {
            return false;
        }

        for (int i = 2; i <= number/2; i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }

    public void remove(Key key){ //removing
        int index;

        index = PAF(key);

        index = locate(index,key);


        if (index != -1){
            table[index].setRemoved();
            size--;
        }
    }

    public int getCountOfCollision(){
        return countOfCollision;
    }
}