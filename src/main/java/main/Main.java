package main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.API.APICategoryServlet;
import servlets.cate_product.AddCategoryServlet;
import servlets.cate_product.EditCategoryServlet;
import servlets.cate_product.ManageCategoryProductServlet;
import servlets.cate_product.PartialServlet;

public class Main {

    public static void main(String[] args) throws Exception {

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new ManageCategoryProductServlet()), "/admin/cate_product");
        context.addServlet(new ServletHolder(new AddCategoryServlet()), "/admin/cate_product/add");
        context.addServlet(new ServletHolder(new EditCategoryServlet()), "/admin/cate_product/edit");
        context.addServlet(new ServletHolder(new APICategoryServlet()), "/admin/api/cate_product");
        context.addServlet(new ServletHolder(new PartialServlet()), "/admin/partital/*");

        ContextHandler resourceHandler = new ContextHandler("/static");
        String resource = "./public";
        if (!resource.isEmpty()) {
            resourceHandler.setResourceBase(resource);
            resourceHandler.setHandler(new ResourceHandler());
        }

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});

        Server server = new Server(8080);

        server.setHandler(handlers);

        server.start();

        System.out.println("Server started");

        server.join();
    }
}
