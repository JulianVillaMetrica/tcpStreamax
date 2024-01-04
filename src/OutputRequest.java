public class OutputRequest {
    private static String request(LoginRequest ir, String request, String MODULE, String OPERATION){
        return Response.response(ir, MODULE, OPERATION,request);
    }
    private static String request(LoginRequest ir, String MODULE, String OPERATION){
        return Response.response(ir, MODULE, OPERATION);
    }

    public static String GeneralDeviceStatusQuery(LoginRequest ir, int QMASK, int SERIAL){
        String request = "\"QMASK\":"+QMASK+",\"SERIAL\":"+SERIAL;

        return request(ir, request, "DEVEMM", "QUERYDEVGENERALSTATUS");
    }


    public static String GetDevVersionInfo(LoginRequest ir){
        String request = "\"MODE\":"+0;

        return request(ir,request,"DEVEMM", "GETDEVVERSIONINFO");
    }
    //Obtener la hora "UTC" - Acquire UTC of the current equipment
    public static String GetCTRLUTC(LoginRequest ir){
        return request(ir,"DEVEMM", "GETCTRLUTC");
    }

    //Establecer la hora "UTC" en el dispositivo - Set the current UTC
    public static String SetCTRLUTC(LoginRequest ir, int CURT, String Z){
        String request = "\"CURT\":"+CURT+","+"\"Z\":"+Z;

        return request(ir, request, "DEVEMM", "SETCTRLUTC");
    }

    //The Equipment Actively Requests for Timing
    public static String CheckTime(LoginRequest ir, int HANDLE, int CURT, String Z){
        String request = "\"HANDLE\":"+HANDLE+",\"CURT\":"+CURT+",\"Z\":"+Z;

        return request(ir, request, "DEVEMM", "CHECKTIME");
    }
}
