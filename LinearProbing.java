public class LinearProbing<Key,Value> implements DictionaryInterface<Key,Value>{

    private HashEntry<Key,Value>[] table;
    private static double LOAD_FACTOR_THRESHOLD = 0.5;

    private int initial_size=16;

    private static int countOfCollision;
    public int size;

    public LinearProbing(){
        @SuppressWarnings("unchecked")
        HashEntry<Key,Value>[] temp = (HashEntry<Key,Value>[])new HashEntry[NextPrime(initial_size)];
        table = temp;
        countOfCollision = 0;
        size = 0;
    }

    private int PAF(Key key){  //Horner's rule algorithm
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

    private int SSF(Key key) {   //SSF Algorithm
        return hashCode(String.valueOf(key)) % table.length;
    }
    public int hashCode(String input) {
        int hash = 0;
        for (char character : input.toCharArray()) {
            hash += (int) character;
        }
        return hash;
    }

    public Value get(Key key){//get the value

        Value result = null;
        int hash = 0;

        hash = SSF(key);
        hash = locate( hash,key ) ;

        if (hash != -1){

            result = table[hash].getValue();
        }

        return result;
    }

    public void put(Key key,Value value){ //put the customer
        if ((key==null) || (value==null))
            throw new IllegalArgumentException("Cannot add null to a dictionary.");

        else {
            Value oldValue;
            int index;

            index = SSF(key);
            index = probe(index,key,value);

            assert (index >= 0) && (index < table.length);

            if ((table[index] == null) || table[index].isIn() ){
                table[index] = new HashEntry<>(key, value);
                size++;
            }
            else{
                oldValue = table[index].getValue();
                ((Customer)oldValue).addTransaction(((Customer) value).getTransaction().get(0).getDate(), ((Customer) value).getTransaction().get(0).getProductName());
                table[index].setValue(oldValue);
            }

            if ((double)size/ (double)table.length > LOAD_FACTOR_THRESHOLD) resize(table.length);
        }
    }

    public int locate(int index,Key key){ //locate the index

        try {
            boolean found = false;

            while (!found && (table[index] != null)) {
                if (!table[index].isIn() && (String.valueOf(key)).equals(String.valueOf(table[index].getKey()))) {
                    found = true;
                } else {
                    index = (index + 1) % table.length;
                }
            }

            int result = -1;

            if (found) {
                result = index;
            }

            return result;
        } catch (Exception e) {

            System.err.println("Exception during locate: " + e.getMessage());
            return -1;
        }
    }

    public int probe(int index, Key key, Value value){ //probing
        try {
            boolean found = false;
            int firstIndex = index;
            int removedStateIndex = -1;

            while (!found && (table[index] != null)) {
                if (!table[index].isIn()) {
                    if ((String.valueOf(key)).equals(String.valueOf(table[index].getKey()))) {
                        found = true;
                    } else {
                        countOfCollision++;
                        index = (index + 1) % table.length;
                    }
                } else {
                    if (removedStateIndex == -1) {
                        removedStateIndex = index;
                    }

                    index = (index + 1) % table.length;
                }
            }

            if (firstIndex != index) {
                countOfCollision += 1;
            }

            if (found || (removedStateIndex == -1)) {
                return index;
            } else {
                return removedStateIndex;
            }
        } catch (Exception e) {

            System.err.println("Exception during probe: " + e.getMessage());
            return -1;
        }
    }

    public void resize(int initialcap){ //resize the hashtable
        try {
            HashEntry<Key, Value>[] oldTable = table;
            int oldSize = initialcap;            int newSize = NextPrime(oldSize * 2);

            @SuppressWarnings("unchecked")
            HashEntry<Key, Value>[] temp = (HashEntry<Key, Value>[]) new HashEntry[newSize];
            table = temp;
            size = 0;

            for (int i = 0; i < oldSize; i++) {
                if (oldTable[i] != null) {
                    put(oldTable[i].getKey(), oldTable[i].getValue());
                }
            }
        } catch (RuntimeException e) {

            System.err.println("RuntimeException during resize: " + e.getMessage());
        }
    }

    public int getCountOfCollision(){
        return countOfCollision;
    }

    public void remove(Key key){ // removing
        int index;
        index = SSF(key);
        index = locate(index,key);

        if (index != -1){
            table[index].setRemoved();
            size--;
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
            nextNumber += 2; // Sadece tek sayıları kontrol etmek yeterlidir
        }

        return nextNumber;
    }

    private static boolean isPrime(int number) {//is it prime?
        if (number < 2) {
            return false;
        }

        for (int i = 2; i < number/2; i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }

}
