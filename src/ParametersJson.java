import java.util.Map;

public class ParametersJson {
    private String MODULE;
    private String OPERATION;
    private String SESSION;
    private Response RESPONSE;

    // Getters and setters
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

    public String getSESSION() {
        return SESSION;
    }

    public void setSESSION(String SESSION) {
        this.SESSION = SESSION;
    }

    public Response getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(Response RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    @Override
    public String toString() {
        return "MyJsonData{" +
                "MODULE='" + MODULE + '\'' +
                ", OPERATION='" + OPERATION + '\'' +
                ", SESSION='" + SESSION + '\'' +
                ", RESPONSE=" + RESPONSE +
                '}';
    }

    // Clase interna Response
    public static class Response {
        private String ERRORCAUSE;
        private String ERRORCODE;
        private String CURT;
        private String Z;

        // Getters and setters
        public String getERRORCAUSE() {
            return ERRORCAUSE;
        }

        public void setERRORCAUSE(String ERRORCAUSE) {
            this.ERRORCAUSE = ERRORCAUSE;
        }

        public String getERRORCODE() {
            return ERRORCODE;
        }

        public void setERRORCODE(String ERRORCODE) {
            this.ERRORCODE = ERRORCODE;
        }

        public String getCURT() {
            return CURT;
        }

        public void setCURT(String CURT) {
            this.CURT = CURT;
        }

        public String getZ() {
            return Z;
        }

        public void setZ(String Z) {
            this.Z = Z;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "ERRORCAUSE='" + ERRORCAUSE + '\'' +
                    ", ERRORCODE=" + ERRORCODE +
                    ", CURT=" + CURT +
                    ", Z='" + Z + '\'' +
                    '}';
        }
    }
}

