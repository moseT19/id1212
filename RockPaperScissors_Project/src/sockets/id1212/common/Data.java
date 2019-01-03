package src.sockets.id1212.common;

import java.io.Serializable;

public class Data implements Serializable {

    private DataType dt;
    private String dataBody;

    public Data(String incomingFromClient){

        parse(incomingFromClient);
    }

    private void parse(String s){

        String[] parts = s.split("-");
        dt = DataType.valueOf(parts[0]);
        dataBody = parts[1];

    }

    public DataType getDt() {
        return dt;
    }

    public String getDataBody() {
        return dataBody;
    }

    public void setDt(DataType dt) {
        this.dt = dt;
    }

    public void setDataBody(String dataBody) {
        this.dataBody = dataBody;
    }
}
