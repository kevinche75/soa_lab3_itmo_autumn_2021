package ru.itmo.service;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Properties;

public class RemoteBeanLookup {

    public static LabWorkI lookupRemoteStatelessBean() {
        Properties jndiProperties = new Properties();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
//        jndiProperties.put(Context.PROVIDER_URL, "http-remoting://127.0.0.1:8080/");
        //This property is important for remote resolving
//        jndiProperties.put("jboss.naming.client.ejb.context", true);
        //This propert is not important for remote resolving
//        jndiProperties.put("org.jboss.ejb.client.scoped.context", "true");
        try {
            final javax.naming.Context context = new InitialContext(jndiProperties);
            final String appName = "global";
            final String moduleName = "ejb_back-snapshot";
            final String beanName = "LabWorksService";
            final String viewClassName = LabWorkI.class.getName();
            // let's do the lookup
            String lookupName = "java:" + appName + "/" + moduleName + "/" + beanName + "!" + viewClassName;
//            String lookupName = "/ejb_back-snapshot/LabWorksService!ru.itmo.service.LabWorkI";
            System.out.println(lookupName);
            System.out.println("===========\nSuccess");
            return (LabWorkI) context.lookup(lookupName);
        } catch (NamingException e) {
            System.out.println("не получилось (");
//            System.out.println(e.getMessage());
            e.printStackTrace();
            return new LabWorkI() {

                @Override
                public Response getAllLabWorks(MultivaluedMap<String, String> map) {
                    throw new EJBException("bean is not available");
                }

                @Override
                public Response getLabWork(String str_id) {
                    throw new EJBException("bean is not available");
                }

                @Override
                public Response getMinName() {
                    throw new EJBException("bean is not available");
                }

                @Override
                public Response countPersonalQualitiesMaximum(String str_pqm) {
                    throw new EJBException("bean is not available");
                }

                @Override
                public Response createLabWork(String xmlStr) {
                    throw new EJBException("bean is not available");
                }

                @Override
                public Response updateLabWork(String str_id, String xmlStr) {
                    throw new EJBException("bean is not available");
                }

                @Override
                public Response deleteLabWork(String str_id) {
                    throw new EJBException("bean is not available");
                }

                @Override
                public Response test() {
                    throw new EJBException("bean is not available");
                }

                @Override
                public Response getLessMaximumPoint(MultivaluedMap<String, String> map, String maximum_point) {
                    throw new EJBException("bean is not available");
                }
            };
        }
    }
}
