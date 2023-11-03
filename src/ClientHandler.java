
import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.google.gson.Gson;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientInfo;
    private Gson gson = new Gson();
    private LoginRequest lr = null;
    private  String clientSessionID;
    private boolean isFirstConnection = true;
    int  numberBytes;

    private boolean boolConnection= true;
    Header header = new Header();
    public ClientHandler(Socket socket) throws IOException {

        try {
            this.socket = socket;

            InputStream stream = socket.getInputStream();
            byte[] dataIni = new byte[474];
            int count = stream.read(dataIni);
            byte[] data = new byte[count];
            data = dataIni;
            header = new Header();
            header.setV(data[0]);
            header.setP(data[1]);
            header.setM(data[2]);
            header.setCSRC_COUNT(data[3]);
            header.setPLAYLOAD_TYPE(data[4]);
            header.setSSRC(data[5]);
            header.setPLAYLOAD_LEN(Arrays.copyOfRange(data, 6, 8));
            header.setRESERVE(Arrays.copyOfRange(data, 8, 10));
            header.setCSRC(Arrays.copyOfRange(data, 10, 12));

            //    byte[] jsonBytes = (Arrays.copyOfRange(data, 12, data.length - 1)); // Convertir bytes a arreglo de bytes

            String dataString = new String(data, StandardCharsets.UTF_8); // UTF-8 es un conjunto de caracteres comúnmente utilizado
            System.out.println(dataString);

            // Encuentra la posición del primer '{' para determinar el inicio del JSON
            int startIndex = dataString.indexOf('{');       //problema si es null??

            // Extrae la parte del JSON a partir del primer '{' hasta el final de la cadena
            String jsonWithoutPrefix;
            /*    if(finalIndex==-1||finalIndex==-0)
                     jsonWithoutPrefix = dataString.substring(startIndex);
                else
                     jsonWithoutPrefix = dataString.substring(startIndex,finalIndex);*/
            jsonWithoutPrefix = dataString.substring(startIndex);
            String jsonPrefix = dataString.substring(0, startIndex);


            clientHandlers.add(this);
            lr = gson.fromJson(jsonWithoutPrefix, LoginRequest.class);

            //Mostrar que recibe del json el sessionID - Si lo hace
            clientSessionID = lr.getSESSION();
            System.out.println(clientSessionID);

            byte[] encapHeader = new byte[12];

            encapHeader[0] = header.getV();
            encapHeader[1] = header.getP();
            encapHeader[2] = header.getM();
            encapHeader[3] = header.getCSRC_COUNT();
            encapHeader[4] = header.getPLAYLOAD_TYPE();
            encapHeader[5] = header.getSSRC();
            byte[] ef = new byte[2];
            ef = header.getPLAYLOAD_LEN();
            encapHeader[6] = ef[0];
            encapHeader[7] = ef[1];
            byte[] gh = new byte[2];

            byte[] ij = new byte[2];
            gh = header.getRESERVE();
            ij = header.getCSRC();
            encapHeader[8] = gh[0];
            encapHeader[9] = gh[1];
            encapHeader[10] = ij[0];
            encapHeader[11] = ij[1];
            String message = Response.response(lr);


            //poner el payload len
            byte[] messageBytes = message.getBytes();
            numberBytes = messageBytes.length;
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putInt(numberBytes);

            // Obtén el array de bytes resultante
            ef = buffer.array();
            header.setPLAYLOAD_LEN(ef);
            encapHeader[6] = ef[2];
            encapHeader[7] = ef[3];
            sendMessageToClient(encapHeader, messageBytes);

            System.out.println("Ya paso el connect");
            //! ----------------------------------------------------------------------------------------------------
            /*Se empieza a hacer la parte de la respuesta*/

            byte[] dataIniRes = new byte[1000];
            int countRes = stream.read(dataIniRes);
            byte[] dataRes = new byte[countRes];
            dataRes = dataIniRes;
            //  byte[] data2 = new byte[count2];
            //System.out.println(Arrays.toString(dataRes));
            Header headerRes = new Header();
            header.setV(dataRes[0]);
            header.setP(dataRes[1]);
            header.setM(dataRes[2]);
            header.setCSRC_COUNT(dataRes[3]);
            header.setPLAYLOAD_TYPE(dataRes[4]);
            header.setSSRC(dataRes[5]);
            header.setPLAYLOAD_LEN(Arrays.copyOfRange(dataRes, 6, 8));
            header.setRESERVE(Arrays.copyOfRange(dataRes, 8, 10));
            header.setCSRC(Arrays.copyOfRange(dataRes, 10, 12));

            dataString = new String(dataRes, StandardCharsets.UTF_8); // UTF-8 es un conjunto de caracteres comúnmente utilizado
            System.out.println(dataString);

            // Encuentra la posición del primer '{' para determinar el inicio del JSON
            startIndex = dataString.indexOf('{');       //problema si es null??
            int finalIndex = dataString.indexOf("}}");
            finalIndex = finalIndex + 2;
            // Extrae la parte del JSON a partir del primer '{' hasta el final de la cadena
            //  String jsonWithoutPrefix;
            /*    if(finalIndex==-1||finalIndex==-0)
                     jsonWithoutPrefix = dataString.substring(startIndex);
                else
                     jsonWithoutPrefix = dataString.substring(startIndex,finalIndex);*/
            jsonWithoutPrefix = dataString.substring(startIndex, finalIndex);
            jsonPrefix = dataString.substring(0, startIndex);

            //clientHandlers.
            clientHandlers.add(this);
            lr = gson.fromJson(jsonWithoutPrefix, LoginRequest.class);

            //Mostrar que recibe del json el sessionID - Si lo hace
            //    clientSessionID = lr.getSESSION();
            System.out.println(clientSessionID);

            encapHeader = new byte[12];

            encapHeader[0] = header.getV();
            encapHeader[1] = header.getP();
            encapHeader[2] = header.getM();
            encapHeader[3] = header.getCSRC_COUNT();
            encapHeader[4] = header.getPLAYLOAD_TYPE();
            encapHeader[5] = header.getSSRC();
            ef = new byte[2];
            ef = header.getPLAYLOAD_LEN();
            encapHeader[6] = ef[0];
            encapHeader[7] = ef[1];
            gh = new byte[2];

            ij = new byte[2];
            gh = header.getRESERVE();
            ij = header.getCSRC();
            encapHeader[8] = gh[0];
            encapHeader[9] = gh[1];
            encapHeader[10] = ij[0];
            encapHeader[11] = ij[1];
            message = Response.response(lr);


            //poner el payload len
            messageBytes = message.getBytes();
            numberBytes = messageBytes.length;
            buffer = ByteBuffer.allocate(4);
            buffer.putInt(numberBytes);

            // Obtén el array de bytes resultante
            ef = buffer.array();
            header.setPLAYLOAD_LEN(ef);
            encapHeader[6] = ef[2];
            encapHeader[7] = ef[3];
            sendMessageToClient(encapHeader, messageBytes);

            System.out.println("Ya paso el getsupportservice");


            String respuesta = "{\"MODULE\":\"DEVEMM\",\"OPERATION\":\"GETDEVVERSIONINFO\",\"PARAMETER\":{\"MODE\":1},\"SESSION\":\"" + clientSessionID + "\"}";
            messageBytes = respuesta.getBytes();
            numberBytes = messageBytes.length;
            sendMessageToClient(encapHeader, messageBytes);
            dataIniRes = new byte[1000];
            countRes = stream.read(dataIniRes);
            dataRes = new byte[countRes];
            dataRes = dataIniRes;
            dataString = new String(dataRes, StandardCharsets.UTF_8); // UTF-8 es un conjunto de caracteres comúnmente utilizado
            System.out.println(dataString);
            System.out.println("final?");

                /* respuesta = "{\"MODULE\":\"DEVEMM\",\"OPERATION\":\"GETDEVVERSIONINFO\",\"PARAMETER\":{\"MODE\":1},\"SESSION\":\""+clientSessionID+"\"}";
                messageBytes = respuesta.getBytes();
                numberBytes = messageBytes.length;
                sendMessageToClient(encapHeader, messageBytes);
                dataIniRes = new byte[1000];
                countRes =stream.read(dataIniRes);
                dataRes = new byte[countRes];
                dataRes = dataIniRes;
                dataString = new String(dataRes, StandardCharsets.UTF_8); // UTF-8 es un conjunto de caracteres comúnmente utilizado
                System.out.println(dataString);
                System.out.println("final?");
                */
        } catch (IOException e) {
            //        closeEverything(socket, bufferedReader, bufferedWriter);
        }
        while (boolConnection) {
            OutputRequest.GetDevVersionInfo(lr);
            byte[] encapHeader = new byte[12];
            //  Header header = new Header();
            encapHeader[0] = header.getV();
            encapHeader[1] = header.getP();
            encapHeader[2] = header.getM();
            encapHeader[3] = header.getCSRC_COUNT();
            encapHeader[4] = header.getPLAYLOAD_TYPE();
            encapHeader[5] = header.getSSRC();
            byte[] ef = new byte[2];
            ef = header.getPLAYLOAD_LEN();
            encapHeader[6] = ef[0];
            encapHeader[7] = ef[1];
            byte[] gh = new byte[2];

            byte[] ij = new byte[2];
            gh = header.getRESERVE();
            ij = header.getCSRC();
            encapHeader[8] = gh[0];
            encapHeader[9] = gh[1];
            encapHeader[10] = ij[0];
            encapHeader[11] = ij[1];
            String message = OutputRequest.GetDevVersionInfo(lr);


            //poner el payload len
            byte[] messageBytes = message.getBytes();
            numberBytes = messageBytes.length;
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putInt(numberBytes);

            // Obtén el array de bytes resultante
            ef = buffer.array();
            header.setPLAYLOAD_LEN(ef);
            encapHeader[6] = ef[2];
            encapHeader[7] = ef[3];
            sendMessageToClient(encapHeader, messageBytes);
            wait(1000);
        }
    }

    public void sendMessageToClient(byte[] byteArray,byte[] messageBytes) throws IOException {

        byte[] messageCompleto = new byte[byteArray.length + messageBytes.length];
        System.arraycopy(byteArray, 0, messageCompleto, 0, byteArray.length);
        System.arraycopy(messageBytes, 0, messageCompleto, byteArray.length, messageBytes.length);

        OutputStream outputStream = socket.getOutputStream();

        outputStream.write(messageCompleto);
    }
    @Override
    public void run() {

    }

 /*   public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (clientHandler != this) {
                    // Envía el mensaje a todos los clientes excepto al cliente actual.
              //      clientHandler.sendMessageToClient(clientSessionID + ": " + messageToSend);
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }
*/
    public void removeClientHandler() {
        clientHandlers.remove(this);
       // broadcastMessage("Server: " + clientSessionID + " has left the chat");
    }



    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*Funciones de utilidad*/
    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
}

