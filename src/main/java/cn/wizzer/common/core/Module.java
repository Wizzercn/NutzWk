package cn.wizzer.common.core;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.beetl.ext.nutz.BeetlViewMaker;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.nutz.integration.shiro.ShiroSessionProvider;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

import java.io.File;

/**
 * Created by wizzer on 2016/6/21.
 */
@Modules(scanPackage = true, packages = "cn.wizzer.modules")
@Ok("json:full")
@Fail("http:500")
@IocBy(type = ComboIocProvider.class, args = {"*json", "config/ioc/", "*anno", "cn.wizzer", "*tx", "*quartz", "*async"})
@Localization(value = "locales/", defaultLocalizationKey = "zh_CN")
@Encoding(input = "UTF-8", output = "UTF-8")
@Views({BeetlViewMaker.class})
@SetupBy(value = Setup.class)
@ChainBy(args = "config/chain/nutzwk-mvc-chain.json")
@SessionBy(ShiroSessionProvider.class)
public class Module {
    private static Server server;
    private static final int DEFAULT_HTTP_PORT = 8080;
    private static final int DEFAULT_THREAD_SIZE = 20;

    public static void main(String[] args) throws Exception {
        int port = DEFAULT_HTTP_PORT;
        int threadSize = DEFAULT_THREAD_SIZE;
        String war = "src/main/webapp";
        if ((new File("webapp")).exists()) {
            war = "webapp";
        }

        String contextPath = "/";
        int gracefulShutdownTimeout = 3000;
        Options options = new Options();
        options.addOption("p", "port", true, "server port, default is 8080");
        options.addOption("m", "max-threads", true, "max threads, default is 20");
        options.addOption("w", "war", true, "war directory");
        options.addOption("c", "context-path", true, "context path");
        options.addOption("g", "graceful-shutdown-timeout", true, "set graceful shutdown timeout(ms), default is 3000ms");
        options.addOption("h", "help", false, "show help message");
        GnuParser parser = new GnuParser();

        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("p")) {
                port = Integer.parseInt(commandLine.getOptionValue("p"));
            }

            if (commandLine.hasOption("m")) {
                threadSize = Integer.parseInt(commandLine.getOptionValue("m"));
            }

            if (commandLine.hasOption("w")) {
                war = commandLine.getOptionValue("w");
            }

            if (commandLine.hasOption("c")) {
                contextPath = commandLine.getOptionValue("c");
            }

            if (commandLine.hasOption("g")) {
                gracefulShutdownTimeout = Integer.parseInt(commandLine.getOptionValue("g"));
            }

            if (commandLine.hasOption("h")) {
                usage(options);
            }
        } catch (Exception var12) {
            usage(options);
        }

        server = new Server(new QueuedThreadPool(threadSize));
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath(contextPath);
        webapp.setWar(war);
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{new StatisticsHandler(), webapp});
        server.setHandler(handlers);
        server.setStopAtShutdown(true);
        server.setStopTimeout((long) gracefulShutdownTimeout);
        server.start();
        server.join();

    }

    public static void shutdown() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void usage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Main", options);
        System.exit(1);
    }
}
