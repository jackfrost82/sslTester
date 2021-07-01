package com.beggi;
 
import java.io.IOException;
import java.io.PrintWriter;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@WebServlet("/testConnection")
public class testConnection extends HttpServlet {
 
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
         
        // read form fields
        String url = request.getParameter("url");
        String port = request.getParameter("port");

        System.out.println("url: " + url + "port" + port);
 
        // do some processing here...
        try {
			SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(url, Integer.parseInt(port));
            
            sslsocket.startHandshake();

            PrintWriter out = new PrintWriter(
                                  new BufferedWriter(
                                  new OutputStreamWriter(
                                  sslsocket.getOutputStream())));

            System.out.println("Connecting to " + url + " " + port);
            out.print("GET / HTTP/1.1\r\n");
            out.print("Host: " + url + "\r\n");
            out.print("\r\n");
            out.flush();
            /*
             * Make sure there were no surprises
             */
            if (out.checkError())
                System.out.println(
                    "SSLSocketClient:  java.io.PrintWriter error");

            /* read response */
            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(
                                    sslsocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
{
                System.out.println(inputLine);
                break;
}
            in.close();
            out.close();
            sslsocket.close();

		} catch (Exception e) {
			e.printStackTrace();
            ServletException se = new ServletException(e.getMessage(), e);
            se.initCause(e);
            throw se;
            
		} 
        // get response writer
        PrintWriter writer = response.getWriter();
         
        // build HTML code
        String htmlRespone = "<html>";
        htmlRespone += "<h2>Connessione effettuata a " + url + " \n\rVerificare i log";      

        // return response
        writer.println(htmlRespone);
         
    }
 
}