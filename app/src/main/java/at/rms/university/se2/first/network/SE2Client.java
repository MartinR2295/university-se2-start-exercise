package at.rms.university.se2.first.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SE2Client extends Thread {

    public interface Response {
        // on successful response
        void onSE2ServerResponse(String response);
        // on error
        void onSE2ServerError(IOException e);
        // on finished (error or success)
        void onSE2ServerFinished();
    }

    private final String host = "se2-isys.aau.at";
    private final int port = 53212;
    private Response responseHandler;
    private String inputString;

    public SE2Client(Response responseHandler, String inputString) {
        this.responseHandler = responseHandler;
        this.inputString = inputString;
    }

    @Override
    public void run() {
        super.run();
        try {
            // create socket
            Socket socket = new Socket(this.host, this.port);

            // create output stream to write to the server
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            // create inputReader with inputStream to read response
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // send data to the server
            outputStream.writeBytes(inputString + '\n');

            // receive the response, and send it to the handler
            this.responseHandler.onSE2ServerResponse(inputReader.readLine());

            // close socket
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            this.responseHandler.onSE2ServerError(e);
        }

        // notify about the finished process
        this.responseHandler.onSE2ServerFinished();
    }
}
