
import java.util.Map;

public class LoginRequest {
    private String MODULE;
    private String OPERATION;
    private Map<String, Object> PARAMETER;
    private String SESSION;
    private Header HEADER;

    public LoginRequest() {
    }

    public String getMODULE() {
        return MODULE;
    }

    public void setMODULE(String MODULE) {
        this.MODULE = MODULE;
    }

    public String getOPERATION() {
        return OPERATION;
    }

    public void setOPERATION(String OPERATION) {
        this.OPERATION = OPERATION;
    }

    public Map<String, Object> getPARAMETER() {
        return PARAMETER;
    }

    public Object getPARAMETERS(String key){
        if(PARAMETER.get(key)==null)
            return "\"\"";
        else
            return PARAMETER.get(key);
    }

    public void setPARAMETERS(String key, Object value){
        PARAMETER.put(key, value);
    }

    public void setPARAMETER(Map<String, Object> PARAMETER) {
        this.PARAMETER = PARAMETER;
    }

    public String getSESSION() {
        return SESSION;
    }

    public void setSESSION(String SESSION) {
        this.SESSION = SESSION;
    }

    public void setHEADER(Header header){
        this.HEADER = HEADER;
    }

    public Header getHEADER(){
        return HEADER;
    }
    /*
    public void HEADER(String bits, Header header){

        // V
        if(bits.substring(0, 2).equals("01"))
            header.setV(1);

        // P
        if(bits.substring(2, 3).equals("0"))
            header.setP(0);
        else
            header.setP(1);

        // M
        if(bits.substring(3,4).equals("0"))
            header.setM(0);
        else
            header.setM(1);

        // CSRC COUNT
        switch (bits.substring(4,8)) {
            case "0000" -> header.setCSRC_COUNT(0);
            case "0001" -> header.setCSRC_COUNT(1);
            case "0010" -> header.setCSRC_COUNT(2);
            case "0011" -> header.setCSRC_COUNT(3);
            case "0100" -> header.setCSRC_COUNT(4);
            case "0101" -> header.setCSRC_COUNT(5);
            case "0110" -> header.setCSRC_COUNT(6);
            case "0111" -> header.setCSRC_COUNT(7);
            case "1000" -> header.setCSRC_COUNT(8);
            default -> header.setCSRC_COUNT(8);
        }


        // PAYLOAD TYPE
        switch (bits.substring(8,16)) {
            case "00000000" -> header.setPLAYLOAD_TYPE(0);
            case "00000010" -> header.setPLAYLOAD_TYPE(2);
            case "00000011" -> header.setPLAYLOAD_TYPE(3);
            case "00000100" -> header.setPLAYLOAD_TYPE(4);
            case "00000110" -> header.setPLAYLOAD_TYPE(6);
            case "00001010" -> header.setPLAYLOAD_TYPE(10);
            case "00001011" -> header.setPLAYLOAD_TYPE(11);
            case "00001111" -> header.setPLAYLOAD_TYPE(15);
            case "00010000" -> header.setPLAYLOAD_TYPE(16);
            case "00010001" -> header.setPLAYLOAD_TYPE(17);
            default -> header.setPLAYLOAD_TYPE(0);
        }
    }*/

}

