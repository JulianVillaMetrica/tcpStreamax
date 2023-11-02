public class Error {

    public static String ErrorCause(int error){
        String ErrC = "";

        ErrC = switch(error){
            case 0 -> "";
            case 1 -> "failed to acquire the device";
            case 2 -> "failed to verify the identity";
            case 3 -> "authentication timeout";
            case 4 -> "check S0 failed";
            case 5 -> "username or password error";
            case 6 -> "execution failed";
            case 7 -> "protocol analysis failed";
            case 8 -> "failed to connect to the media server";
            case 9 -> "failed to register the media link";
            case 10 -> "failed to create the media transmission thread";
            case 11 -> "Currently, the protocol doesn't support the function";
            case 12 -> "memory allocation failed";
            case 13 -> "no matching results were found";
            case 14 -> "network issue";
            case 15 -> "failed to connect to the EMAIL server";
            case 16 -> "timing failed";
            case 17 -> "failed to go offline forcibly";
            case 18 -> "no permissions";
            case 19 -> "forced to go offline due to low permissions";
            case 20 -> "failed to add username";
            case 21 -> "the same username";
            case 22 -> "failed to edit user";
            case 23 -> "failed to delete user";
            case 24 -> "too many online users";
            case 25 -> "task exists";
            case 26 -> "insufficient sources or task is full";
            case 27 -> "illegal channel";
            case 28 -> "uncoded";
            case 29 -> "main stream not supported";
            case 30 -> "sub-stream not supported";
            case 31 -> "mobile phone stream not supported";
            case 32 -> "failed to start live preview";
            case 33 -> "the file doesn't exist";
            case 34 -> "failed to acquire the upgrade file path";
            case 35 -> "failed to receive the upgrade file";
            case 36 -> "fail to check the upgrade file";
            case 37 -> "failed to open the upgrade file";
            case 38 -> "failed to capture photos";
            case 39 -> "failed to send data";
            case 40 -> "no such task";
            case 41 -> "unknown error";
            case 42 -> "parameter error";
            case 43 -> "time error";
            case 44 -> "server connection timeout";
            case 45 -> "other users are configuring";
            case 46 -> "too frequent operation";
            case 47 -> "testing";
            case 48 -> "sender address error";
            case 49 -> "recipient address error";
            case 50 -> "server fill-in error";
            case 51 -> "failed to read data from the device";
            case 52 -> "failed to start remote sync playback";
            case 53 -> "failed to end remote sync playback";
            case 54 -> "failed to drag";
            case 55 -> "too many upgrade users";
            case 56 -> "the user has no permissions";
            case 57 -> "the version number of upgrade file is the same";
            case 58 -> "the system is upgrading";
            case 59 -> "server port fill-in error";
            case 60 -> "WIFI module doesn’t exist";
            case 61 -> "wWIFI switch is off";
            case 62 -> "linking";
            case 63 -> "user's MAC address is illegal";
            case 64 -> "no permissions";
            case 65 -> "media link exists";
            case 66 -> "device serial number doesn't exist";
            case 67 -> "audio coding failed";
            case 68 -> "audio analysis failed";
            case 69 -> "failed to start talkback";
            case 70 -> "failed to download log";
            case 71 -> "verifying";
            case 72 -> "verification failed";
            case 73 -> "successful verification";
            case 74 -> "acquiring valid IP";
            case 75 -> "failed to export parameters";
            case 76 -> "failed to import parameters";
            case 77 -> "parameter check failed";
            case 78 -> "failed to configure network parameters";
            case 79 -> "re-report to the server";
            case 80 -> "failed to connect to the signaling server";
            case 81 -> "processing command";
            case 82 -> "no remote device";
            case 83 -> "failed to log in the FTP server";
            case 84 -> "upgrade package download is successful";
            case 85 -> "no storage space or storage medium";
            case 86 -> "disk is protected";
            case 87 -> "the file is in the backup";
            case 88 -> "task completed";
            case 89 -> "task rejected";
            case 90 -> "USB upgrade interrupted";
            case 91 -> "remote playback data end";
            case 92 -> "the line is busy";
            case 93 -> "dialing failed";
            case 94 -> "not supported by the current network";
            case 95 -> "3G/4G modules start to upgrade";
            case 96 -> "no deleted data";
            case 97 -> "deletion is ready";
            case 98 -> "deleting";
            case 99 -> ""; //Sin descripción
            case 100 -> "user expired";
            case 101 -> "time shift task has been deleted due to expiration";
            case 102 -> "failed to receive operation & maintenance records";
            case 103 -> "the file exists";
            case 104 -> "failed to create file";
            case 105 -> "USB undetected";
            case 106 -> "USB read/write error";
            case 107 -> "no corresponding file in the USB";
            case 108 -> "no hardware configuration table in the device";
            //case 109 -> "";   //No hay Error 109
            case 110 -> "failed to import hardware configuration table";
            case 111 -> "need to change the initial password";
            case 112 -> "failed to save the new password";
            case 113 -> "old and new passwords are the same";
            case 114 -> "new and initial passwords are the same";
            case 115 -> "the function is disabled";
            case 116 -> "the task is in progress";
            case 117 -> "download failed and no SD card in the passenger counter";
            case 118 -> "no corresponding time period data in the SD card of passenger counter";
            case 119 -> "failed to create routing";
            case 120 -> "canceled task causes execution termination";
            case 121 -> "cannot download video outside the Geo-fence";
            default -> "";
        };

        return ErrC;
    }




}
