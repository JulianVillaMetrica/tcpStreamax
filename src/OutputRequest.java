public class OutputRequest {
    private static String request(LoginRequest ir, String request, String MODULE, String OPERATION){
        return Response.response(ir, MODULE, OPERATION,request);
    }


    public static String GeneralDeviceStatusQuery(LoginRequest ir, int QMASK, int SERIAL){
        String request = "\"QMASK\":"+QMASK+",\"SERIAL\":"+SERIAL;

        return request(ir, request, "DEVEMM", "QUERYDEVGENERALSTATUS");
    }


    public static String GetDevVersionInfo(LoginRequest ir){
        String request = "\"MODE\":"+0;

        return request(ir,request,"DEVEMM", "GETDEVVERSIONINFO");
    }
}
