package wjy.yo.ereader.remotevo;

import lombok.Data;

@Data
public class OpResult {

    private int ok;

    private String message;

    public boolean isOk() {
        return this.ok == 1;
    }

}
