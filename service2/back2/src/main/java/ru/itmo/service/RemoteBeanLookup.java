package ru.itmo.service;

import ru.itmo.utils.ResponseWrapper;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class RemoteBeanLookup {

    public static SecondServiceI lookupRemoteStatelessBean() {
        Properties jndiProperties = new Properties();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
        try {
            final Context context = new InitialContext(jndiProperties);
            final String appName = "global";
            final String moduleName = "ejb_back2-snapshot";
            final String beanName = "SecondService";
            final String viewClassName = SecondServiceI.class.getName();
            String lookupName = "java:" + appName + "/" + moduleName + "/" + beanName + "!" + viewClassName;
            System.out.println(lookupName);
            return (SecondServiceI) context.lookup(lookupName);
        } catch (NamingException e) {
            System.out.println("не получилось (");
            e.printStackTrace();
            return new SecondServiceI() {

                @Override
                public ResponseWrapper getDisciplines() {
                    throw new EJBException("bean is not available");
                }

                @Override
                public ResponseWrapper getDiscipline(String stringId) {
                    throw new EJBException("bean is not available");                }

                @Override
                public ResponseWrapper createDiscipline(String stringDiscipline) {
                    throw new EJBException("bean is not available");                }

                @Override
                public ResponseWrapper getDisciplineLabWorks(String stringDisciplineId) {
                    throw new EJBException("bean is not available");                }

                @Override
                public ResponseWrapper addLabWorkToDiscipline(String stringDisciplineId, String stringLabWorkId) {
                    throw new EJBException("bean is not available");                }

                @Override
                public ResponseWrapper removeLabWorkFromDiscipline(String stringDisciplineId, String stringLabWorkId) {
                    throw new EJBException("bean is not available");                }

                @Override
                public ResponseWrapper increaseLabWorkDifficulty(String id, String stringSteps) {
                    throw new EJBException("bean is not available");                }

                @Override
                public ResponseWrapper test() {
                    throw new EJBException("bean is not available");
                }
            };
        }
    }
}
