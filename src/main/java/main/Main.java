package main;

import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.admin.api.APIAdminServlet;
import servlets.admin.api.APICategoryNewsServlet;
import servlets.admin.api.APICategoryServlet;
import servlets.admin.api.APINewsServlet;
import servlets.admin.api.APIProductServlet;
import servlets.admin.api.APISlidesServlet;
import servlets.admin.api.APISupplierServlet;
import servlets.admin.cate_product.AddCategoryServlet;
import servlets.admin.cate_product.EditCategoryServlet;
import servlets.admin.cate_product.ManageCategoryProductServlet;
import servlets.admin.PartialServlet;
import servlets.admin.mnadmin.AddAdminServlet;
import servlets.admin.mnadmin.EditAdminServlet;
import servlets.admin.mnadmin.LoginServlet;
import servlets.admin.mnadmin.LogoutServlet;
import servlets.admin.mnadmin.ManageAdminServlet;
import servlets.admin.cate_news.AddCategoryNewsServlet;
import servlets.admin.cate_news.EditCategoryNewsServlet;
import servlets.admin.cate_news.ManageCategoryNewsServlet;
import servlets.client.ManageIndexServlet;
import servlets.admin.filter.AuthenFilter;
import servlets.admin.news.AddNewsServlet;
import servlets.admin.news.EditNewsServlet;
import servlets.admin.news.ManageNewsServlet;
import servlets.admin.product.AddProductServlet;
import servlets.admin.product.EditProductServlet;
import servlets.admin.product.ManageProductServlet;
import servlets.admin.slides.AddSlidesServlet;
import servlets.admin.slides.EditSlidesServlet;
import servlets.admin.slides.ManageSlidesServlet;
import servlets.admin.supplier.AddSupplierServlet;
import servlets.admin.supplier.EditSupplierServlet;
import servlets.admin.supplier.ManageSupplierServlet;

public class Main {

    public static void main(String[] args) throws Exception {

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new ManageCategoryProductServlet()), "/admin/cate_product");
        context.addServlet(new ServletHolder(new ManageSupplierServlet()), "/admin/supplier");
        context.addServlet(new ServletHolder(new ManageProductServlet()), "/admin/product");
        context.addServlet(new ServletHolder(new ManageCategoryNewsServlet()), "/admin/cate_news");
        context.addServlet(new ServletHolder(new ManageNewsServlet()), "/admin/news");
        context.addServlet(new ServletHolder(new ManageSlidesServlet()), "/admin/slides");
        context.addServlet(new ServletHolder(new ManageAdminServlet()), "/admin/mnadmin");
        context.addServlet(new ServletHolder(new LoginServlet()), "/admin/login");
        context.addServlet(new ServletHolder(new LogoutServlet()), "/admin/logout");

        context.addServlet(new ServletHolder(new ManageIndexServlet()), "/client/index");

        context.addServlet(new ServletHolder(new AddCategoryServlet()), "/admin/cate_product/add");
        context.addServlet(new ServletHolder(new AddSupplierServlet()), "/admin/supplier/add");
        context.addServlet(new ServletHolder(new AddProductServlet()), "/admin/product/add");
        context.addServlet(new ServletHolder(new AddCategoryNewsServlet()), "/admin/cate_news/add");
        context.addServlet(new ServletHolder(new AddNewsServlet()), "/admin/news/add");
        context.addServlet(new ServletHolder(new AddSlidesServlet()), "/admin/slides/add");
        context.addServlet(new ServletHolder(new AddAdminServlet()), "/admin/mnadmin/add");

        context.addServlet(new ServletHolder(new EditCategoryServlet()), "/admin/cate_product/edit");
        context.addServlet(new ServletHolder(new EditSupplierServlet()), "/admin/supplier/edit");
        context.addServlet(new ServletHolder(new EditProductServlet()), "/admin/product/edit");
        context.addServlet(new ServletHolder(new EditCategoryNewsServlet()), "/admin/cate_news/edit");
        context.addServlet(new ServletHolder(new EditNewsServlet()), "/admin/news/edit");
        context.addServlet(new ServletHolder(new EditSlidesServlet()), "/admin/slides/edit");
        context.addServlet(new ServletHolder(new EditAdminServlet()), "/admin/mnadmin/edit");

        context.addServlet(new ServletHolder(new APICategoryServlet()), "/admin/api/cate_product");
        context.addServlet(new ServletHolder(new APISupplierServlet()), "/admin/api/supplier");
        context.addServlet(new ServletHolder(new APIProductServlet()), "/admin/api/product");
        context.addServlet(new ServletHolder(new APICategoryNewsServlet()), "/admin/api/cate_news");
        context.addServlet(new ServletHolder(new APINewsServlet()), "/admin/api/news");
        context.addServlet(new ServletHolder(new APISlidesServlet()), "/admin/api/slides");
        context.addServlet(new ServletHolder(new APIAdminServlet()), "/admin/api/mnadmin");

        context.addServlet(new ServletHolder(new PartialServlet()), "/admin/partital/*");

        FilterHolder authenFilter = new FilterHolder(new AuthenFilter());
        authenFilter.setName("AuthenFilter");
        context.addFilter(authenFilter, "/admin/*", null);

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
