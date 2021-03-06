package main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
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
import servlets.admin.UploadFileServlet;
import servlets.admin.api.APIContact;
import servlets.admin.api.APIEmailRegisterServlet;
import servlets.admin.api.APIInfoBuyerServlet;
import servlets.admin.api.APISetiingServlet;
import servlets.admin.api.APIUserRegisterServlet;
import servlets.admin.mnadmin.AddAdminServlet;
import servlets.admin.mnadmin.EditAdminServlet;
import servlets.admin.mnadmin.LoginServlet;
import servlets.admin.mnadmin.LogoutServlet;
import servlets.admin.mnadmin.ManageAdminServlet;
import servlets.admin.cate_news.AddCategoryNewsServlet;
import servlets.admin.cate_news.EditCategoryNewsServlet;
import servlets.admin.cate_news.ManageCategoryNewsServlet;
import servlets.admin.contact.EditContactServlet;
import servlets.admin.contact.ManageContactServlet;
import servlets.admin.email_register.EditEmailServlet;
import servlets.admin.email_register.ManageEmailServlet;
import servlets.client.Home;
import servlets.admin.filter.AuthenFilter;
import servlets.admin.info_buyer.EditInfoBuyerServlet;
import servlets.admin.info_buyer.ManageInfoBuyerServlet;
import servlets.admin.news.AboutServlet;
import servlets.admin.news.AddNewsServlet;
import servlets.admin.news.EditNewsServlet;
import servlets.admin.news.ManageNewsServlet;
import servlets.admin.product.AddProductServlet;
import servlets.admin.product.EditProductServlet;
import servlets.admin.product.ManageProductServlet;
import servlets.admin.register.EditUserRegister;
import servlets.admin.register.ManageUserRegisterServlet;
import servlets.admin.settings.AddSettingServlet;
import servlets.admin.settings.EditSettingServlet;
import servlets.admin.settings.ManageSettingsServlet;
import servlets.admin.slides.AddSlidesServlet;
import servlets.admin.slides.EditSlidesServlet;
import servlets.admin.slides.ManageSlidesServlet;
import servlets.admin.supplier.AddSupplierServlet;
import servlets.admin.supplier.EditSupplierServlet;
import servlets.admin.supplier.ManageSupplierServlet;
import servlets.client.AboutBlog;
import servlets.client.Cart;
import servlets.client.CateProduct;
import servlets.client.Contact;
import servlets.client.EmailRegister;
import servlets.client.InfoBuyer;
import servlets.client.Login;
import servlets.client.NewsDetail;
import servlets.client.NewsServlet;
import servlets.client.Payment;
import servlets.client.ProductDetail;
import servlets.client.Register;
import servlets.client.SearchProduct;

public class MainApp {

    public static void main(String[] args) throws Exception {

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        //<editor-fold defaultstate="collapsed" desc="Admin Servlet">
        context.addServlet(new ServletHolder(new ManageCategoryProductServlet()), "/admin/cate_product");
        context.addServlet(new ServletHolder(new ManageSupplierServlet()), "/admin/supplier");
        context.addServlet(new ServletHolder(new ManageProductServlet()), "/admin/product");
        context.addServlet(new ServletHolder(new ManageCategoryNewsServlet()), "/admin/cate_news");
        context.addServlet(new ServletHolder(new ManageNewsServlet()), "/admin/news");
        context.addServlet(new ServletHolder(new ManageSlidesServlet()), "/admin/slides");
        context.addServlet(new ServletHolder(new ManageAdminServlet()), "/admin/mnadmin");
        context.addServlet(new ServletHolder(new ManageSettingsServlet()), "/admin/setting");
        context.addServlet(new ServletHolder(new LoginServlet()), "/admin/login");
        context.addServlet(new ServletHolder(new LogoutServlet()), "/admin/logout");
        context.addServlet(new ServletHolder(new ManageContactServlet()), "/admin/contact");
        context.addServlet(new ServletHolder(new ManageEmailServlet()), "/admin/email");
        context.addServlet(new ServletHolder(new ManageInfoBuyerServlet()), "/admin/info_buyer");
        context.addServlet(new ServletHolder(new ManageUserRegisterServlet()), "/admin/register");

        context.addServlet(new ServletHolder(new AddCategoryServlet()), "/admin/cate_product/add");
        context.addServlet(new ServletHolder(new AddSupplierServlet()), "/admin/supplier/add");
        context.addServlet(new ServletHolder(new AddProductServlet()), "/admin/product/add");
        context.addServlet(new ServletHolder(new AddCategoryNewsServlet()), "/admin/cate_news/add");
        context.addServlet(new ServletHolder(new AddNewsServlet()), "/admin/news/add");
        context.addServlet(new ServletHolder(new AddSlidesServlet()), "/admin/slides/add");
        context.addServlet(new ServletHolder(new AddAdminServlet()), "/admin/mnadmin/add");
        context.addServlet(new ServletHolder(new AddSettingServlet()), "/admin/setting/add");

        context.addServlet(new ServletHolder(new EditCategoryServlet()), "/admin/cate_product/edit");
        context.addServlet(new ServletHolder(new EditSupplierServlet()), "/admin/supplier/edit");
        context.addServlet(new ServletHolder(new EditProductServlet()), "/admin/product/edit");
        context.addServlet(new ServletHolder(new EditCategoryNewsServlet()), "/admin/cate_news/edit");
        context.addServlet(new ServletHolder(new EditNewsServlet()), "/admin/news/edit");
        context.addServlet(new ServletHolder(new EditSlidesServlet()), "/admin/slides/edit");
        context.addServlet(new ServletHolder(new EditAdminServlet()), "/admin/mnadmin/edit");
        context.addServlet(new ServletHolder(new EditSettingServlet()), "/admin/setting/edit");
        context.addServlet(new ServletHolder(new EditContactServlet()), "/admin/contact/edit");
        context.addServlet(new ServletHolder(new EditEmailServlet()), "/admin/email/edit");
        context.addServlet(new ServletHolder(new EditInfoBuyerServlet()), "/admin/info_buyer/edit");
        context.addServlet(new ServletHolder(new EditUserRegister()), "/admin/register/edit");

        context.addServlet(new ServletHolder(new APICategoryServlet()), "/admin/api/cate_product");
        context.addServlet(new ServletHolder(new APISupplierServlet()), "/admin/api/supplier");
        context.addServlet(new ServletHolder(new APIProductServlet()), "/admin/api/product");
        context.addServlet(new ServletHolder(new APICategoryNewsServlet()), "/admin/api/cate_news");
        context.addServlet(new ServletHolder(new APINewsServlet()), "/admin/api/news");
        context.addServlet(new ServletHolder(new APISlidesServlet()), "/admin/api/slides");
        context.addServlet(new ServletHolder(new APIAdminServlet()), "/admin/api/mnadmin");
        context.addServlet(new ServletHolder(new APISetiingServlet()), "/admin/api/setting");
        context.addServlet(new ServletHolder(new APIContact()), "/admin/api/contact");
        context.addServlet(new ServletHolder(new APIEmailRegisterServlet()), "/admin/api/email");
        context.addServlet(new ServletHolder(new APIInfoBuyerServlet()), "/admin/api/info_buyer");
        context.addServlet(new ServletHolder(new APIUserRegisterServlet()), "/admin/api/register");

        context.addServlet(new ServletHolder(new PartialServlet()), "/admin/partital/*");
        //</editor-fold>

        // Test UploadFile
        context.addServlet(new ServletHolder(new UploadFileServlet()), "/admin/upload-file");
        context.addServlet(new ServletHolder(new AboutServlet()), "/admin/about-blog");
        context.addServlet(new ServletHolder(new EmailRegister()), "/add/email");
        context.addServlet(new ServletHolder(new InfoBuyer()), "/add/info-buyer");

        context.addServlet(new ServletHolder(new Home()), "/");
        context.addServlet(new ServletHolder(new CateProduct()), "/danh-muc");
        context.addServlet(new ServletHolder(new ProductDetail()), "/chi-tiet-san-pham");
        context.addServlet(new ServletHolder(new NewsServlet()), "/tin-tuc");
        context.addServlet(new ServletHolder(new NewsDetail()), "/chi-tiet-tin-tuc");
        context.addServlet(new ServletHolder(new AboutBlog()), "/gioi-thieu");
        context.addServlet(new ServletHolder(new Contact()), "/lien-he");
        context.addServlet(new ServletHolder(new SearchProduct()), "/tim-kiem");
        context.addServlet(new ServletHolder(new Cart()), "/gio-hang");
        context.addServlet(new ServletHolder(new Payment()), "/thanh-toan");
        context.addServlet(new ServletHolder(new Register()), "/dang-ky");
        context.addServlet(new ServletHolder(new Login()), "/dang-nhap");

        context.setContextPath("/");
        context.setSessionHandler(new SessionHandler());

        FilterHolder authenFilter = new FilterHolder(new AuthenFilter());
        authenFilter.setName("AuthenFilter");
        context.addFilter(authenFilter, "/admin/*", null);

        ContextHandler resourceHandler = new ContextHandler("/static");
        String resource = "./public";
        resourceHandler.setResourceBase(resource);
        resourceHandler.setHandler(new ResourceHandler());

        ContextHandler uploadHandler = new ContextHandler("/upload");
        String resourceUpload = "./upload";
        uploadHandler.setResourceBase(resourceUpload);
        uploadHandler.setHandler(new ResourceHandler());

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, uploadHandler, context});

        Server server = new Server(8080);

        server.setHandler(handlers);

        server.start();

        System.out.println("Server started");

        server.join();

    }
}
