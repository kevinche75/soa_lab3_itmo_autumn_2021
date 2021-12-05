package ru.itmo.service;

import ru.itmo.utils.ResponseWrapper;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class RemoteBeanLookup {

    public static SecondServiceI lookupRemoteStatelessBean() {
        Properties jndiProperties = new Properties();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
        try {
            Context env = (Context)new InitialContext().lookup("java:comp/env");
            final Context context = new InitialContext(jndiProperties);
            final String appName = (String) env.lookup("appName");
            final String moduleName = (String) env.lookup("moduleName");
            final String beanName = (String) env.lookup("beanName");
            final String viewClassName = SecondServiceI.class.getName();
            final String scope = (String) env.lookup("scope");
            String lookupName = scope + ":" + appName + "/" + moduleName + "/" + beanName + "!" + viewClassName;
            System.out.println(lookupName);
            return (SecondServiceI) context.lookup(lookupName);
        } catch (NamingException e) {
            System.out.println("не получилось (");
            e.printStackTrace();
            return new SecondServiceI() {

                @Override
                public ResponseWrapper getDisciplines() {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper getDiscipline(String stringId) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper createDiscipline(String stringDiscipline) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper getDisciplineLabWorks(String stringDisciplineId) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper addLabWorkToDiscipline(String stringDisciplineId, String stringLabWorkId) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper removeLabWorkFromDiscipline(String stringDisciplineId, String stringLabWorkId) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper increaseLabWorkDifficulty(String id, String stringSteps) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper test() {
                    return new ResponseWrapper(500, "Server error, try again!");
                }
            };
        }
    }
}
