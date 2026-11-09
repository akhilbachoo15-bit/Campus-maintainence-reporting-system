package com.berea.ipa302;

import com.berea.ipa302.repository.ReportRepository;
import com.berea.ipa302.repository.UserRepository;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.File;

/**
 * Runs once when the application starts up.
 * Creates the two "repositories" (our in-memory data stores) and puts
 * them in the ServletContext so every servlet can share the SAME
 * instance instead of each one making its own (which would mean data
 * disappearing between requests).
 *
 * It also makes sure a folder exists outside the web app for storing
 * uploaded evidence files, so they cannot be opened directly through a
 * browser URL.
 */
@WebListener
public class AppInitListener implements ServletContextListener {

    public static final String USER_REPO_KEY = "userRepository";
    public static final String REPORT_REPO_KEY = "reportRepository";
    public static final String UPLOAD_DIR_KEY = "uploadDir";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(USER_REPO_KEY, new UserRepository());
        sce.getServletContext().setAttribute(REPORT_REPO_KEY, new ReportRepository());

        String uploadPath = System.getProperty("java.io.tmpdir") + File.separator + "campus-maintenance-uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        sce.getServletContext().setAttribute(UPLOAD_DIR_KEY, uploadPath);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // nothing to clean up for the in-memory prototype
    }
}
