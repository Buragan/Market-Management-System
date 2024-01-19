

public interface DictionaryInterface<Key, Value> {

    public Value get(Key key);

    public void put(Key key, Value value);

    public int locate(int index, Key key);
}