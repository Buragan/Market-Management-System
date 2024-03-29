public class HashEntry<Key,Value>{

    private Key key;
    private Value value;
    private States state;
    private enum States {CURRENT,REMOVED}

    HashEntry(Key search_key,Value data_value) {
        key=search_key;
        value=data_value;
        state=HashEntry.States.CURRENT;
    }

    public Key getKey() {
        return key;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value new_value){
        value = new_value;
    }

    public void setRemoved(){
        state = States.REMOVED;
    }

    public boolean isIn(){
        return (state == States.REMOVED);
    }
    public void setCurrent(){
        state = States.CURRENT;
    } //we may have to use that



}