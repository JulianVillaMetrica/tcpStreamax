
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

}

