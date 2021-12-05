package ru.itmo.utils;

import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.health.ServiceHealth;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;

public class ServiceDiscovery {

    public static Consul client = null;

    static  {
        try {
            client = Consul.builder().build();
        } catch (Exception e) {
            System.err.println("Consul is unavailable");
        }
    }

    public static String getUriFromConsul() throws NamingException {
        Context env = (Context)new InitialContext().lookup("java:comp/env");
        String serviceName = (String) env.lookup("serviceName");
        String serviceMetaKey = (String) env.lookup("serviceMetaUriKey");
        if (client != null) {
            HealthClient healthClient = client.healthClient();
            List<ServiceHealth> nodes = healthClient.getHealthyServiceInstances(serviceName).getResponse();
            if (nodes.size() > 0) {
                ServiceHealth service = nodes.get(0);
                System.out.println("Got service's 2 (Payara) uri from consul");
                String address = service.getNode().getAddress();
                int port = service.getService().getPort();
                String app = service.getService().getMeta().get(serviceMetaKey);
                return String.format("https://%s:%d/%s", address, port, app);
            }
        }
        System.err.println("Service 2 (Payara) not available from consul - using default resource");
        return (String) env.lookup("uri");
    }
}
