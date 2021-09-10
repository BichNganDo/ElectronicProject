package main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.API.APICategoryNews;
import servlets.API.APICategoryServlet;
import servlets.API.APIProductServlet;
import servlets.API.APISupplierServlet;
import servlets.cate_product.AddCategoryServlet;
import servlets.cate_product.EditCategoryServlet;
import servlets.cate_product.ManageCategoryProductServlet;
import servlets.PartialServlet;
import servlets.cate_news.AddCategoryNewsServlet;
import servlets.cate_news.EditCategoryNewsServlet;
import servlets.cate_news.ManageCategoryNewsServlet;
import servlets.product.AddProductServlet;
import servlets.product.EditProductServlet;
import servlets.product.ManageProductServlet;
import servlets.supplier.AddSupplierServlet;
import servlets.supplier.EditSupplierServlet;
import servlets.supplier.ManageSupplierServlet;

public class Main {

    public static void main(String[] args) throws Exception {

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new ManageCategoryProductServlet()), "/admin/cate_product");
        context.addServlet(new ServletHolder(new ManageSupplierServlet()), "/admin/supplier");
        context.addServlet(new ServletHolder(new ManageProductServlet()), "/admin/product");
        context.addServlet(new ServletHolder(new ManageCategoryNewsServlet()), "/admin/cate_news");

        context.addServlet(new ServletHolder(new AddCategoryServlet()), "/admin/cate_product/add");
        context.addServlet(new ServletHolder(new AddSupplierServlet()), "/admin/supplier/add");
        context.addServlet(new ServletHolder(new AddProductServlet()), "/admin/product/add");
        context.addServlet(new ServletHolder(new AddCategoryNewsServlet()), "/admin/cate_news/add");

        context.addServlet(new ServletHolder(new EditCategoryServlet()), "/admin/cate_product/edit");
        context.addServlet(new ServletHolder(new EditSupplierServlet()), "/admin/supplier/edit");
        context.addServlet(new ServletHolder(new EditProductServlet()), "/admin/product/edit");
        context.addServlet(new ServletHolder(new EditCategoryNewsServlet()), "/admin/cate_news/edit");

        context.addServlet(new ServletHolder(new APICategoryServlet()), "/admin/api/cate_product");
        context.addServlet(new ServletHolder(new APISupplierServlet()), "/admin/api/supplier");
        context.addServlet(new ServletHolder(new APIProductServlet()), "/admin/api/product");
        context.addServlet(new ServletHolder(new APICategoryNews()), "/admin/api/cate_news");

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
