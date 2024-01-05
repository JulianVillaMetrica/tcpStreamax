
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private String operation;
    private String response;
    private Socket socket;

    private Gson gson = new Gson();
    private LoginRequest lr = null;
    private  String clientSessionID;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    String jsonClean;



    Header header = new Header();
    public ClientHandler(Socket socket, String operation, String response) throws IOException {
        this.socket = socket;
        this.operation= operation;
        this.response=response;
    }


    @Override
    public void run() {
        try {

            while(true){
                /*---------------------------------------- Comunicacion Cliente - Servidor ----------------------------------------*/
                //this.socket = socket;

                /* LECTURA DE DATOS */

                InputStream stream = socket.getInputStream();
                byte[] dataIni = new byte[100000];
                int count = stream.read(dataIni);
                byte[] data;
                // Se inicializa el arreglo secundario solo con los datos de entrada (quitando el espacio restante)

                if (count>1) {
                    data = Arrays.copyOfRange(dataIni, 0, count);

                    /* RECOLECTAR LOS DATOS DEL HEADER ENTRANTE */

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

                    /* RECOLECTAR LOS DATOS DEL CUERPO DEL MENSAJE */

                    String dataString = new String(data, StandardCharsets.UTF_8);
                    /* BORRAR : Prueba de que se realizo correctamente la conversion de datos */
                    System.out.println("json inicial: " + dataString);

                    int startIndex = dataString.indexOf('{');
                    if (startIndex >= 0) {

                        int llavesAbiertas = 0;
                        int llavesCerradas = 0;

                        for (int i = 0; i < dataString.length(); i++) {
                            if (dataString.charAt(i) == '{') {
                                llavesAbiertas++;
                            } else if (dataString.charAt(i) == '}') {
                                llavesCerradas++;
                            }

                            if (llavesAbiertas == llavesCerradas && llavesAbiertas != 0) {
                                jsonClean = dataString.substring(startIndex, i + 1);
                                System.out.println("el json limpio?");
                                System.out.println(jsonClean);
                                System.out.println("Fin de Json");
                                break;
                            }
                        }

                        String operation = getOperation(jsonClean);
                        if (operation != null) {
                            System.out.println("Operacion: " + operation);

                            clientHandlers.add(this);
                            // Asignar a nuestro objeto de la clase lr mediante gson, los datos a sus respectivos cambios
                            lr = gson.fromJson(jsonClean, LoginRequest.class);
                            // Asignar a nuestra variable global el ID del cliente
                            clientSessionID = lr.getSESSION();

                            /* PREPARAR LA RESPUESTA */

                            // Crear un arreglo de byte para el header de la respuesta
                            byte[] encapHeader = new byte[12];

                            encapHeader[0] = header.getV();
                            encapHeader[1] = header.getP();
                            encapHeader[2] = header.getM();
                            encapHeader[3] = header.getCSRC_COUNT();
                            encapHeader[4] = header.getPLAYLOAD_TYPE();
                            encapHeader[5] = header.getSSRC();
                            byte[] playload_len;
                            playload_len = header.getPLAYLOAD_LEN();
                            encapHeader[6] = playload_len[0];
                            encapHeader[7] = playload_len[1];
                            byte[] reserve;
                            reserve = header.getRESERVE();
                            byte[] CSRC;
                            CSRC = header.getCSRC();
                            encapHeader[8] = reserve[0];
                            encapHeader[9] = reserve[1];
                            encapHeader[10] = CSRC[0];
                            encapHeader[11] = CSRC[1];

                            String message = Response.response(lr);

                            // Crear un arreglo de byte a partir de la cadena con el mensaje
                            byte[] messageBytes = message.getBytes();
                            // Guardamos espacio para manejar 4 bytes
                            ByteBuffer buffer = ByteBuffer.allocate(4);
                            // Ponemos la longitud del arreglo que contiene el mensaje que vamos a enviar
                            buffer.putInt(messageBytes.length);

                            // A nuestro arreglo que maneja la longitud del mensaje le asignamos lo que tiene el buffer
                            playload_len = buffer.array();
                            /* BORRAR (?) : Para que sobreescribir el header si no se vuelve a usar */
                            header.setPLAYLOAD_LEN(playload_len);

                            // Actualizar el header con el tamano correcto del playloadLenght
                            encapHeader[6] = playload_len[2];
                            encapHeader[7] = playload_len[3];
                            // Enviar el mensaje
                            sendMessageToClient(encapHeader, messageBytes);

                            /* BORRAR : Prueba de que se llego hasta el envio del mensaje de manera correcta */
                            System.out.println("Se realizo el envio del mensaje");
                            clientHandlers.add(this);
                            /*---------------------------------------- Comunicacion Cliente - Servidor ----------------------------------------*/

                        }//fin de chequeo de nulo
                    }//fin de chequeo si tiene o no un json, en caso de que no se hace el chequeo a ver si es un usuario
                    else {
                        switch (dataString.charAt(0)) {
                            case '$' -> //aqui se tiene que hacer una mejor verificacion de entrada, contrasena?
                                    configClient("esto es una persona configurando al dispositivo con sessionId:" + dataString);
                            case '+' -> {
                                clientHandlers.get(0).operation="GETDEVVERSIONINFO";
                            }
                            case '|' -> {
                                clientHandlers.get(0).operation="GETCTRLUTC";
                            }
                            case  '?' -> {;
                                clientHandlers.get(0).operation="SETCTRLUTC";
                            }
                            case  '(' -> {;
                                clientHandlers.get(0).operation="CHECKTIME";
                            }
                        }
                       // wait(200);
                        //sendSimpleMessage(clientHandlers.get(0).jsonClean);
                        //aqui hago las operaciones creo
                        if(clientHandlers.get(0).operation!=null) {
                            String getRes = "";
                            switch (clientHandlers.get(0).operation) {

                                case "GETDEVVERSIONINFO" -> {
                                    getRes = OutputRequest.GetDevVersionInfo(lr);
                                }
                                case "GETCTRLUTC" -> {
                                    getRes = OutputRequest.GetCTRLUTC(lr);
                                }
                                case "SETCTRLUTC" ->{
                                    getRes = OutputRequest.SetCTRLUTC((clientHandlers.get(0).lr),1570775036,"480A");
                                }
                                case "CHECKTIME"->{
                                    getRes = OutputRequest.CheckTime(lr,32,1570775036,"480A");
                                }

                            }
                            System.out.println("lo que se manda desde el cliente: " + getRes);
                            byte[] encapHeader = prepHeader(0, getRes);
                            sendMessageToClient(encapHeader, getRes.getBytes());

                            stream = socket.getInputStream();
                            dataIni = new byte[100000];
                            count = stream.read(dataIni);
                            // Se inicializa el arreglo secundario solo con los datos de entrada (quitando el espacio restante)
                            data = Arrays.copyOfRange(dataIni, 0, count);
                            String string = new String(data, StandardCharsets.UTF_8);
                            System.out.println("la respuesta del cliente:" + string);
                            String jsonResponse =cleanData(string);
                            clientHandlers.get(0).jsonClean=jsonResponse;
                        }
                    }
                }//checar que si se hayan leido datos
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSocket(socket);
        }
    }



    public void sendMessageToClient(byte[] byteArray,byte[] messageBytes) throws IOException {

        byte[] messageCompleto = new byte[byteArray.length + messageBytes.length];
        System.arraycopy(byteArray, 0, messageCompleto, 0, byteArray.length);
        System.arraycopy(messageBytes, 0, messageCompleto, byteArray.length, messageBytes.length);

        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(messageCompleto);

    }
    public void sendSimpleMessage(String message) throws IOException {
        OutputStream outputStream = socket.getOutputStream();

        outputStream.write(message.getBytes());
    }
    public void removeClientHandler() {
        clientHandlers.remove(this);
       // broadcastMessage("Server: " + clientSessionID + " has left the chat");
    }
    public void configClient(String message) {
        try {

            while(true)
            {
                OutputStream outputStream = socket.getOutputStream();
                //System.out.println(message);
                outputStream.write(message.getBytes());
                InputStream stream = socket.getInputStream();

                // Se crea un arreglo para meter los datos que se reciban, de momento de tamano bastante grande
                byte[] dataIni = new byte[100000];
                int count = stream.read(dataIni);


                System.out.println("Respuesta:");
                String string = new String(dataIni, StandardCharsets.UTF_8);
                System.out.println(string);


                ClientHandler clientHandlerConfiguring = clientHandlers.get(0);
                String sessionId = clientHandlerConfiguring.lr.getSESSION();
                String ultimoJson = clientHandlerConfiguring.jsonClean;//es como pedirle el ultimo json que recibio


                String messageLR = OutputRequest.GetDevVersionInfo(clientHandlerConfiguring.lr);


                //System.out.println("El session id a configurar: "+ sessionId);
                //System.out.println("El ultimo JSON: "+ultimoJson);
                sendSimpleMessage(sessionId+": "+ultimoJson);
            }

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
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

    public void closeSocket(Socket socket) {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public byte[] prepHeader(int clientNum,String message) {
        byte[] encapHeader = new byte[12];

        encapHeader[0] = header.getV();
        encapHeader[1] = header.getP();
        encapHeader[2] = header.getM();
        encapHeader[3] = header.getCSRC_COUNT();
        encapHeader[4] = header.getPLAYLOAD_TYPE();
        encapHeader[5] = header.getSSRC();
        byte[] payload_len;
        payload_len = header.getPLAYLOAD_LEN();
        encapHeader[6] = payload_len[0];
        encapHeader[7] = payload_len[1];
        byte[] reserve;
        reserve = clientHandlers.get(clientNum).header.getRESERVE();
        byte[] CSRC;
        CSRC = clientHandlers.get(clientNum).header.getCSRC();
        encapHeader[8] = reserve[0];
        encapHeader[9] = reserve[1];
        encapHeader[10] = CSRC[0];
        encapHeader[11] = CSRC[1];
       // message = Response.response(lr);

        // Crear un arreglo de byte a partir de la cadena con el mensaje
        byte[] messageBytes = message.getBytes();
        // Guardamos espacio para manejar 4 bytes
        ByteBuffer buffer = ByteBuffer.allocate(4);
        // Ponemos la longitud del arreglo que contiene el mensaje que vamos a enviar
        buffer.putInt(messageBytes.length);

        // A nuestro arreglo que maneja la longitud del mensaje le asignamos lo que tiene el buffer
        payload_len = buffer.array();
        /* BORRAR (?) : Para que sobreescribir el header si no se vuelve a usar */
        header.setPLAYLOAD_LEN(payload_len);

        // Actualizar el header con el tamano correcto del playloadLenght
        encapHeader[6] = payload_len[2];
        encapHeader[7] = payload_len[3];

        return encapHeader;
    }
    public String cleanData(byte[] data){
        String dataString = new String(data, StandardCharsets.UTF_8);
        int startIndex = dataString.indexOf('{');
        if (startIndex >= 0) {

            int llavesAbiertas = 0;
            int llavesCerradas = 0;

            for (int i = 0; i < dataString.length(); i++) {
                if (dataString.charAt(i) == '{') {
                    llavesAbiertas++;
                } else if (dataString.charAt(i) == '}') {
                    llavesCerradas++;
                }

                if (llavesAbiertas == llavesCerradas && llavesAbiertas != 0) {
                    jsonClean = dataString.substring(startIndex, i + 1);
                    System.out.println("el json limpio del metodo");
                    System.out.println(jsonClean);
                    System.out.println("fin del json limpio del metodo");
                    break;
                }
            }
        }
        return  jsonClean;
    }
    public String cleanData(String data){
        //String dataString = new String(data, StandardCharsets.UTF_8);

        int startIndex = data.indexOf('{');
        if (startIndex >= 0) {

            int llavesAbiertas = 0;
            int llavesCerradas = 0;

            for (int i = 0; i < data.length(); i++) {
                if (data.charAt(i) == '{') {
                    llavesAbiertas++;
                } else if (data.charAt(i) == '}') {
                    llavesCerradas++;
                }

                if (llavesAbiertas == llavesCerradas && llavesAbiertas != 0) {
                    jsonClean = data.substring(startIndex, i + 1);
                    System.out.println("el json limpio del metodo");
                    System.out.println(jsonClean);
                    System.out.println("fin del json limpio del metodo");
                    break;
                }
            }
        }
        return  jsonClean;
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
    public static String getOperation(String json) {
        Pattern pattern = Pattern.compile("\"OPERATION\":\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

}

