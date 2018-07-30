package johnny.portfolio.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This web server can create HttpWorker to handle the http requests from the
 * client(Web browser). The server does nothing but dispatches requests to
 * workers. Each worker starts a new thread to handle the request. So the web
 * server can handle multiple requests simultaneously. The main functions
 * provided by this web server include: explore files/directories in the root
 * folder of the webserver; display content of a single file from server;
 * handle fake-cgi request, eg. add numbers.
 */
public class WebServer {
    /**
     * Start a Server Socket to monitor client requests and dispatches the http
     * request to HttpWorkers.
     */
    public static void main(String args[]){
        // The maximum queue length for incoming connection
        int queue_len = 6;
        // Port number for http request
        int port = 2540;
        // A reference of the client socket
        Socket socket;

        try{
            // Setup the server socket
            ServerSocket servsocket = new ServerSocket(port, queue_len);
            System.out.println("Web Server is starting up, listening at port " + port + ".");
            System.out.println("You can access http://localhost:2540 now.");
            
            while(true){
                // Make the server socket wait for the next client request
                socket = servsocket.accept();
                // Local reader from the client
                BufferedReader reader =new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Assign http requests to HttpWorker
                String req = "";
                String clientRequest = "";
                while ((clientRequest = reader.readLine()) != null) {
                    if (req.equals("")) {
                        req  = clientRequest;
                    }
                    if (clientRequest.equals("")) { // If the end of the http request, stop
                        break;
                    }
                }

                if (req != null && !req.equals("")) {
                    new HttpWorker(req, socket).start();
                }
            }
        }
        catch(IOException ex){
            //Handle the exception
            System.out.println(ex);
        }
        finally {
            System.out.println("Server has been shutdown!");
        }
    }
}
