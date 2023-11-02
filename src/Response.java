
public class Response {
    private static int error;
    private static int devType;
    private static int maskCMD;
    private static String pro;
    public static String response(LoginRequest lr){
        String json = "{\"MODULE\":\"" + lr.getMODULE() + "\","
                + "\"OPERATION\":\"" + lr.getOPERATION() + "\","
                + Parameters(lr)
                + "\"SESSION\":\"" + lr.getSESSION() + "\"}";

        return json;
    }
    public static String responseSupport(LoginRequest lr,String sessionId){
        String json = "{\"MODULE\":\"" + lr.getMODULE() + "\","
                + "\"OPERATION\":\"" + lr.getOPERATION() + "\","
                + Parameters(lr)
                + "\"SESSION\":\"" + sessionId + "\"}";

        return json;
    }
    public static String Parameters(LoginRequest lr){
        String param = "";

        if(!"KEEPALIVE".equals(lr.getOPERATION())){
            param = "\"RESPONSE\":{";

            param += switch(lr.getOPERATION()){
                case "CONNECT" -> CONNECT(lr);
                case "SENDALARMINFO" -> SENDALARMINFO(lr);
                default -> "";
            };

            param += "},";
        }

        return param;
    }

    public static String CONNECT(LoginRequest lr){
        devType=1;
        error = 0;
        maskCMD=0;
        pro= "1.0.6";
        String response = "";
        response +="\"DEVTYPE\":";
        response += devType;

        if(lr.getPARAMETER().equals("")) {
            error = 0;
        }
        response += ",\"ERRORCAUSE\":\"" + Error.ErrorCause(error) + "\"";
        response += ",\"ERRORCODE\":";
        response += error;

        response +=",\"MASKCMD\":";
        response += maskCMD;

        response +=",\"PRO\":";
        response += "\""+pro+"\"";
        response +=",\"VCODE\":";
        response += "\"\" ";




        return response;
        /*return "\"ERRORCODE\":" + lr.getPARAMETERS("ERRORCODE") + ","
                + "\"ERRORCAUSE\":" + lr.getPARAMETERS("ERRORCAUSE");*/
    }

    public static String getHeader(Header head){
        return toBinary(head.getV(),2) +
                toBinary(head.getP(),1) +
                toBinary(head.getM(), 1) +
                toBinary(head.getCSRC_COUNT(), 4) +
                toBinary(head.getPLAYLOAD_TYPE(),8) +
                toBinary(head.getSSRC(),16);
    }

    public static String SENDALARMINFO(LoginRequest lr){
        int ALARMTYPE = Integer.parseInt(lr.getPARAMETERS("ALARMTYPE")+"");
        String alarm = "";

        alarm = switch (ALARMTYPE) {
            case 0 -> Alarms.VideoLossAlarm();
            case 1 -> Alarms.CoveredVideoAlarm();
            case 2 -> Alarms.MotionDetectionAlarm();
            case 3 -> Alarms.MemoryExceptionAlarm();
            case 4 -> Alarms.CustomAlarm();
            case 5 -> Alarms.InspectionAlarm();
            case 6 -> Alarms.ViolationDetectionAlarm();
            case 7 -> Alarms.EmergencyAlarm();
            case 8 -> Alarms.SpeedingAlarm();
            case 9 -> Alarms.LowVoltageAlarm();
            case 17 -> Alarms.AlarmForEnteringAndExitingTheGeoFence();
            case 18 -> Alarms.ACC_Alarm();
            case 19 -> Alarms.LostPeripheralConnectionAlarm();
            case 20 -> Alarms.StopAnnouncementAlarm();
            case 21 -> Alarms.GPS_AntennaAlarm();
            case 22 -> Alarms.DayAndNightSwitchAlarm();
            case 23 -> Alarms.NoDrivingAlarm();
            case 32 -> Alarms.SerialPortAlarm();
            case 33 -> Alarms.FatiguedAlarm();
            case 34 -> Alarms.OvertimeParkingAlarm();
            case 35 -> Alarms.PostureAlarm();
            case 36 -> Alarms.EcoDrivingAlarm();
            case 37 -> Alarms.IllegalIgnitionAlarm();
            case 38 -> Alarms.IllegalShutdownAlarm();
            case 39 -> Alarms.CustomExternalInputAlarm();
            case 42 -> Alarms.OilAlarm();
            case 43 -> Alarms.RoadOccupationAlarm();
            case 44 -> Alarms.AntiforgettingAlarm();
            case 45 -> Alarms.FaultAlarmForSpecialCustomers();
            case 46 -> Alarms.TemperatureAnomalyAlarm();
            case 47 -> Alarms.AbnormalTemperatureChangeAlarm();
            case 48 -> Alarms.SmokeAlarm();
            case 49 -> Alarms.GBOX_Alarm();
            case 50 -> Alarms.LicensePlateRecognitionAlarm();
            case 51 -> Alarms.AlarmForDoorOpeningWhileDriving();
            case 52 -> Alarms.AbnormalWirelessSignalAlarm();
            case 54 -> Alarms.PhoneCallingAlarm();
            case 55 -> Alarms.GPS_FaultAlarm();
            case 56 -> Alarms.DSM_Alarm();
            case 57 -> Alarms.FireproofBoxAlarm();
            case 58 -> Alarms.OperationAlarm();
            case 60 -> Alarms.STOPAM_Alarm();
            case 61 -> Alarms.TemperatureThresholdAlarm();
            case 62 -> Alarms.StandardTemperatureRiseAlarm();
            case 64 -> Alarms.IntelligentVehicleAlgorithmAlarm();
            case 65 -> Alarms.AntidismantlingAlarm();
            case 67 -> Alarms.PolicemanLoginAndLogoutAlarm();
            case 68 -> Alarms.VoltageAlarm();
            case 69 -> Alarms.StopAlarm();
            case 70 -> Alarms.AntiforgettingInfraredMotionDetectionAlarm();
            case 72 -> Alarms.DSM_StatisticalDataAlarm();
            case 73 -> Alarms.IllegalDrivingAlarm();
            case 74 -> Alarms.AbnormalBootAlarm();
            case 75 -> Alarms.AlarmSentFromPT2();
            case 76 -> Alarms.ExternalAlarm();
            case 78 -> Alarms.DriverBehaviorAlarm();
            case 79 -> Alarms.BWC_Alarm();
            case 80 -> Alarms.OverloadAlarm();
            case 81 -> Alarms.PassengerCounterFaultAlarm();
            case 82 -> Alarms.CustomAlarm2();
            case 85 -> Alarms.SIM_CardAnomalyAlarm();
            case 86 -> Alarms.IllegalMoveAlarm();
            case 87 -> Alarms.RegionalVehicleDetectionAlarm();
            case 88 -> Alarms.AlarmForCoveredP2Video();
            case 89 -> Alarms.UncalibratedP2Alarm();
            case 90 -> Alarms.UnconnectedP2Alarm();
            case 91 -> Alarms.AbnormalDoorOpeningAndClosingAlarm();
            case 93 -> Alarms.SignalAlarm();
            case 95 -> Alarms.DriverEventAlarm();
            case 96 -> Alarms.FacialComparisonAlarm();
            case 97 -> Alarms.STOPARM_Alarm();
            case 98 -> Alarms.SafezoneAlarm();
            default -> "";
        };

        return alarm;
    }

    public static String toBinary(int n, int length){
        StringBuilder binary = new StringBuilder();
        for (long i = (1L << length - 1); i > 0; i = i / 2) {
            binary.append((n & i) != 0 ? "1" : "0");
        }
        return binary.toString();
    }


}


