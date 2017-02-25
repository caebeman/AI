/**
 * Created by Keifer on 2/25/17.
 */
public class Move {
    private int binNo, value;

    public Move(int bin, int val){
        this.binNo = bin;
        this.value = val;
    }
    public Move(int val){
        this.value = val;
    }

    public int getBinNo() {
        return binNo;
    }

    public void setBinNo(int binNo) {
        this.binNo = binNo;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
