package nl.illuminatedgroup.bankapplicatie;

import java.net.URI;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Server {
    public Server() throws Exception
    {
        HttpServer server = initWebserver();
        server.start();
        
        while(true)
        {
            Thread.sleep(1000);
        }
    }
    
    public static void main(String[] args) throws Exception
    {
        new Server();
    }
    
    public HttpServer initWebserver()
    {
        ResourceConfig config = new ResourceConfig(BankEndPoint.class);
        config.register(JacksonJaxbJsonProvider.class);
        URI uri = URI.create("http://0.0.0.0:8080");
        return GrizzlyHttpServerFactory.createHttpServer(uri, config);
    }
}
