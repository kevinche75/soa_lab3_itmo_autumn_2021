package ru.itmo.service;

import ru.itmo.utils.ResponseWrapper;

import javax.ejb.EJBException;
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
            final javax.naming.Context context = new InitialContext(jndiProperties);
            final String appName = "global";
            final String moduleName = "ejb_back-snapshot";
            final String beanName = "LabWorksService";
            final String viewClassName = LabWorkI.class.getName();
            String lookupName = "java:" + appName + "/" + moduleName + "/" + beanName + "!" + viewClassName;

            return (LabWorkI) context.lookup(lookupName);
        } catch (NamingException e) {
            System.out.println("не получилось (");
            e.printStackTrace();
            return new LabWorkI() {

                @Override
                public ResponseWrapper getAllLabWorks(HashMap<String, String> map) {
                    throw new EJBException("bean is not available");
                }

                @Override
                public ResponseWrapper getLabWork(String str_id) {
                    throw new EJBException("bean is not available");
                }

                @Override
                public ResponseWrapper getMinName() {
                    throw new EJBException("bean is not available");
                }

                @Override
                public ResponseWrapper countPersonalQualitiesMaximum(String str_pqm) {
                    throw new EJBException("bean is not available");
                }

                @Override
                public ResponseWrapper createLabWork(String xmlStr) {
                    throw new EJBException("bean is not available");
                }

                @Override
                public ResponseWrapper updateLabWork(String str_id, String xmlStr) {
                    throw new EJBException("bean is not available");
                }

                @Override
                public ResponseWrapper deleteLabWork(String str_id) {
                    throw new EJBException("bean is not available");
                }

                @Override
                public ResponseWrapper test() {
                    throw new EJBException("bean is not available");
                }

                @Override
                public ResponseWrapper getLessMaximumPoint(HashMap<String, String> map, String maximum_point) {
                    throw new EJBException("bean is not available");
                }
            };
        }
    }
}
