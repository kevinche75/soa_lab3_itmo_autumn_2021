package ru.itmo.utils;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import lombok.SneakyThrows;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Collections;

@Singleton
/*
  Registers the app in consul, also performs check in to consul every 15 seconds
 */
public class ServiceDiscoveryWorker {
    private Consul client = null;
    private static String serviceId = "1";

    {
        try {
            Context env = (Context)new InitialContext().lookup("java:comp/env");
            serviceId = (String) env.lookup("serviceId");
            client = Consul.builder().build();
            AgentClient agentClient = client.agentClient();
            String serviceName = (String) env.lookup("serviceName");
            int port = (Integer) env.lookup("servicePort");
            System.out.println(serviceName);
            System.out.println(port);
            Registration service = ImmutableRegistration.builder()
                    .id(serviceId)
                    .name(serviceName)
                    .port(port)
                    .check(Registration.RegCheck.ttl(60L)) // registers with a TTL of 3 seconds
                    .meta(Collections.singletonMap(
                            (String) env.lookup("serviceMetaUriKey"),
                            (String) env.lookup("serviceMetaUri"))
                    )
                    .build();

            agentClient.register(service);
            System.out.println("Service registered");
        } catch (Exception e) {
            System.err.println("Consul is unavailable");
        }
    }

    @SneakyThrows
    @Schedule(hour = "*", minute = "*", second = "*/15")
    public void checkIn() {
        AgentClient agentClient = client.agentClient();
        agentClient.pass(serviceId);
    }

}