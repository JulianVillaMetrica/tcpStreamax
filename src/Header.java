public class Header {
    private byte V;
    private byte P;
    private byte M;
    private byte CSRC_COUNT;
    private byte PLAYLOAD_TYPE;
    private byte SSRC;
    private byte[] PLAYLOAD_LEN;
    private byte[] RESERVE;
    private byte[] CSRC;


    public Header() {
        this.V = -1;
        this.P = -1;
        this.M = -1;
        this.CSRC_COUNT = -1;
        this.PLAYLOAD_TYPE = -1;
        this.SSRC = 0;
    }

    public Header(byte V, byte P, byte M, byte CSRC_COUNT, byte PLAYLOAD_TYPE, byte SSRC) {
        this.V = V;
        this.P = P;
        this.M = M;
        this.CSRC_COUNT = CSRC_COUNT;
        this.PLAYLOAD_TYPE = PLAYLOAD_TYPE;
        this.SSRC = SSRC;
    }

    public byte getV() {
        return V;
    }

    public void setV(byte V) {
        this.V = V;
    }

    public byte getP() {
        return P;
    }

    public void setP(byte P) {
        this.P = P;
    }

    public byte getM() {
        return M;
    }

    public void setM(byte M) {
        this.M = M;
    }

    public byte getCSRC_COUNT() {
        return CSRC_COUNT;
    }

    public void setCSRC_COUNT(byte CSRC_COUNT) {
        this.CSRC_COUNT = CSRC_COUNT;
    }

    public byte getPLAYLOAD_TYPE() {
        return PLAYLOAD_TYPE;
    }

    public void setPLAYLOAD_TYPE(byte PLAYLOAD_TYPE) {
        this.PLAYLOAD_TYPE = PLAYLOAD_TYPE;
    }

    public byte getSSRC() {
        return SSRC;
    }

    public void setSSRC(byte SSRC) {
        this.SSRC = SSRC;
    }

    public void setPLAYLOAD_LEN(byte[] PLAYLOAD_LEN){this.PLAYLOAD_LEN = PLAYLOAD_LEN;}

    public byte[] getPLAYLOAD_LEN() {
        return PLAYLOAD_LEN;
    }

    public void setRESERVE(byte[] RESERVE){this.RESERVE = RESERVE;}

    public byte[] getRESERVE() {
        return RESERVE;
    }

    public void setCSRC(byte[] CSRC){this.CSRC = CSRC;}

    public byte[] getCSRC() {
        return CSRC;
    }
}

