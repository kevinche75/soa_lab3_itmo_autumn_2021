package ru.itmo.service;

import ru.itmo.utils.ResponseWrapper;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Properties;

public class RemoteBeanLookup {

    public static LabWorkI lookupRemoteStatelessBean() {
        Properties jndiProperties = new Properties();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
        try {
            Context env = (Context)new InitialContext().lookup("java:comp/env");
            final javax.naming.Context context = new InitialContext(jndiProperties);
            final String appName = (String) env.lookup("appName");
            final String moduleName = (String) env.lookup("moduleName");
            final String beanName = (String) env.lookup("beanName");
            final String viewClassName = LabWorkI.class.getName();
            final String scope = (String) env.lookup("scope");
            String lookupName = scope + ":" + appName + "/" + moduleName + "/" + beanName + "!" + viewClassName;

            return (LabWorkI) context.lookup(lookupName);
        } catch (NamingException e) {
            System.out.println("не получилось (");
            e.printStackTrace();
            return new LabWorkI() {

                @Override
                public ResponseWrapper getAllLabWorks(HashMap<String, String> map) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper getLabWork(String str_id) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper getMinName() {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper countPersonalQualitiesMaximum(String str_pqm) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper createLabWork(String xmlStr) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper updateLabWork(String str_id, String xmlStr) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper deleteLabWork(String str_id) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper test() {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper getLessMaximumPoint(HashMap<String, String> map, String maximum_point) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }
            };
        }
    }
}
