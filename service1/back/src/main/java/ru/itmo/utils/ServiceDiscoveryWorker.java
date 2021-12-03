package ru.itmo.utils;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import lombok.SneakyThrows;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import java.util.Collections;

@Singleton
/*
  Registers the app in consul, also performs check in to consul every 15 seconds
 */
public class ServiceDiscoveryWorker {
    private Consul client = null;
    private static final String serviceId = "1";

    {
        try {
            client = Consul.builder().build();
            AgentClient agentClient = client.agentClient();
            Registration service = ImmutableRegistration.builder()
                    .id(serviceId)
                    .name("service1-app")
                    .port(50432)
                    .check(Registration.RegCheck.ttl(60L)) // registers with a TTL of 3 seconds
                    .meta(Collections.singletonMap("app", "lab2/api/labworks"))
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