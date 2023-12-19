
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
    private Socket socket;

    private String clientInfo;
    private Gson gson = new Gson();
    private LoginRequest lr = null;
    private  String clientSessionID;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    String jsonClean;


    Header header = new Header();
    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;

    }


    @Override
    public void run() {
        try {

            while(true){
                /*---------------------------------------- Comunicacion Cliente - Servidor ----------------------------------------*/
                //this.socket = socket;

                /* LECTURA DE DATOS */

                // Se crea un objeto InputStream para estar leyendo la entrada continua del socket
                InputStream stream = socket.getInputStream();
                // Se crea un arreglo para meter los datos que se reciban, de momento de tamaño bastante grande
                byte[] dataIni = new byte[100000];
            /* En un entero se almacena el tamaño de los datos que entraron
               y a su vez se le pasa a dataIni los datos de entrada de stream*/
                int count = stream.read(dataIni);
                // Se crea un arreglo secundario
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

                    // Crear un String haciendo la conversion del arreglo de bytes a UTF_8
                    String dataString = new String(data, StandardCharsets.UTF_8);
                    /* BORRAR : Prueba de que se realizó correctamente la conversion de datos */
                    System.out.println("json inicial: " + dataString);
                    // Guardar en un int la posicion de la primer {


                    int startIndex = dataString.indexOf('{');

                    if (startIndex >= 0) {
                        //    String jsonWithoutPrefix = dataString.substring(startIndex);

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



                            /* BORRAR (?) : El arraylist esta dentro de la clase por lo que no se puede ver desde afuera */
                            clientHandlers.add(this);
                            // Asignar a nuestro objeto de la clase lr mediante gson, los datos a sus respectivos cambios
                            lr = gson.fromJson(jsonClean, LoginRequest.class);
                            // Asignar a nuestra variable global el ID del cliente
                            clientSessionID = lr.getSESSION();
                            /* BORRAR : Prueba de que se realizó correctamente la asignacion del sessionId */
                            System.out.println("SessionID: " + clientSessionID);



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

                            // Actualizar el header con el tamaño correcto del playloadLenght
                            encapHeader[6] = playload_len[2];
                            encapHeader[7] = playload_len[3];
                            // Enviar el mensaje
                            sendMessageToClient(encapHeader, messageBytes);

                            /* BORRAR : Prueba de que se llego hasta el envio del mensaje de manera correcta */
                            System.out.println("Se realizó el envio del mensaje");
                           // clientHandlers.add(this);
                            /*---------------------------------------- Comunicacion Cliente - Servidor ----------------------------------------*/
                        }//fin de chequeo de nulo
                    }//fin de chequeo si tiene o no un json
                    else {
                        //System.out.println("Persona configurand:");

                        if(dataString.charAt(0)=='$')
                            configClient("esto es una persona configurando al dispoisitivo con sessionId:"+dataString);
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
                System.out.println(message);
                outputStream.write(message.getBytes());
                InputStream stream = socket.getInputStream();

                // Se crea un arreglo para meter los datos que se reciban, de momento de tamaño bastante grande
                byte[] dataIni = new byte[100000];
            /* En un entero se almacena el tamaño de los datos que entraron
               y a su vez se le pasa a dataIni los datos de entrada de stream*/
                int count = stream.read(dataIni);
                System.out.println("Respuesta:");
                String string = new String(dataIni, StandardCharsets.UTF_8);
                System.out.println(string);

                ClientHandler clientHandlerConfiguring = clientHandlers.get(0);
                String sessionId = clientHandlerConfiguring.lr.getSESSION();
                String ultimoJson = clientHandlerConfiguring.jsonClean;

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

