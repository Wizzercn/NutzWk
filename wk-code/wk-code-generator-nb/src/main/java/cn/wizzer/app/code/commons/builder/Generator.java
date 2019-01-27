package cn.wizzer.app.code.commons.builder;

import org.apache.commons.cli.*;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.nutz.lang.*;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 入口类
 * Created by wizzer on 2017/1/23.
 */
public class Generator {
    private static final Log log = Logs.get();
    //private final Map<String, TableDescriptor> tables;
    private final TableDescriptor table;

    public Generator(Map<String, TableDescriptor> tables, TableDescriptor table) {
        //this.tables = tables;
        this.table = table;
    }

    public void generate(String packageName, String templatePath, File file, boolean force)
            throws IOException {
        if (file.exists() && !force) {
            log.debug("file " + file + " exists, skipped");
            return;
        }

        String code = generateCode(packageName, templatePath);
        file.getParentFile().mkdirs();
        Files.write(file, code.getBytes(Charset.forName("utf8")));

    }

    public String generateCode(String packageName, String templatePath) throws IOException {
        VelocityContext context = new VelocityContext();
        context.put("table", table);
        context.put("packageName", packageName);
        StringWriter writer = new StringWriter();
        // todo

        String template = new String(Streams.readBytes(ClassLoader.getSystemResourceAsStream(templatePath)),
                Charset.forName("utf8"));
        VelocityEngine engine = new VelocityEngine();
        engine.setProperty("runtime.references.strict", false);
        engine.init();
        engine.evaluate(context, writer, "generator", template);
        return writer.toString();

    }


    // -p export -c /generator.xml entity
    public static void main(String[] args) throws Exception {
        File programRootDir = new File("target/classes/");
        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        URL url = classLoader.getResource("/");
        Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
        add.setAccessible(true);
        add.invoke(classLoader, programRootDir.toURI().toURL());
        boolean vue = false;
        String basePath = "";
        String baseNbPath = "";
        String baseComPath = "";
        String projectPath = "";
        String configPath = "code/code.json";

        Pattern includePattern = Pattern.compile(".*");
        Pattern excludePattern = null;
        String basePackageName = "cn.wizzer.app";
        String controllerPackageName = "controllers";
        String servicePackageName = "services";
        String modelPackageName = "models";
        String _loader = "entity";

        String outputDir = "src/main";
        boolean force = false;
        String baseUri = "/";
        String types[] = {"all"};
        String pages[] = {"index", "add", "edit", "detail"};
        Options options = new Options();
        options.addOption("c", "config", true, "datasource config file(classpath)");
        options.addOption("i", "include", true, "include table pattern");
        options.addOption("x", "exclude", true, "exclude table pattern");
        options.addOption("p", "package", true, "base package name,default:cn.wizzer.app");
        options.addOption("ctr",
                "package",
                true,
                "controller base package name,default:controllers/${package}");
        options.addOption("mod",
                "package",
                true,
                "model base package name,default:models/${package}");
        options.addOption("sev",
                "package",
                true,
                "service base package name,default:services/${package}");
        options.addOption("v",
                "views",
                true,
                "for generator pages,default:all pages,eg: -v index_detail will generate index.html and detail.html");
        options.addOption("vue",
                "vue",
                true,
                "for generator vue");
        options.addOption("j", "project-path", true, "project directory, default is empty");
        options.addOption("b", "base-path", true, "base directory, default is empty");
        options.addOption("y", "common-path", true, "base common directory, default is empty");
        options.addOption("n", "basenb-path", true, "base nutzboot directory, default is empty");
        options.addOption("o", "output", true, "output directory, default is " + outputDir);
        options.addOption("u", "base-uri", true, "base uri prefix, default is /");
        options.addOption("f", "force", false, "force generate file even if file exists");
        options.addOption("loader", "loader", true, "entity or table");
        options.addOption("h", "help", false, "show help message");
        CommandLineParser parser = new GnuParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("vue")) {
                String _vue = commandLine.getOptionValue("vue", "false");
                if ("true".equals(_vue)) {
                    vue = true;
                }
            }
            if (commandLine.hasOption("c")) {
                configPath = commandLine.getOptionValue("c");
            }
            if (commandLine.hasOption("i")) {
                includePattern = Pattern.compile(commandLine.getOptionValue("i"),
                        Pattern.CASE_INSENSITIVE);
            }
            if (commandLine.hasOption("x")) {
                excludePattern = Pattern.compile(commandLine.getOptionValue("x"),
                        Pattern.CASE_INSENSITIVE);
            }
            if (commandLine.hasOption("p")) {
                basePackageName = commandLine.getOptionValue("p");
            }

            if (commandLine.hasOption("ctr")) {
                controllerPackageName = commandLine.getOptionValue("ctr");
            }
            if (commandLine.hasOption("sev")) {
                servicePackageName = commandLine.getOptionValue("sev");
            }
            if (commandLine.hasOption("mod")) {
                modelPackageName = commandLine.getOptionValue("mod");
            }
            if (commandLine.hasOption("j")) {
                projectPath = commandLine.getOptionValue("j");
            }
            if (commandLine.hasOption("o")) {
                outputDir = commandLine.getOptionValue("o");
            }
            if (commandLine.hasOption("u")) {
                baseUri = commandLine.getOptionValue("u");
            }
            if (commandLine.hasOption("b")) {
                basePath = commandLine.getOptionValue("b");
            }
            if (commandLine.hasOption("n")) {
                baseNbPath = commandLine.getOptionValue("n");
            }
            if (commandLine.hasOption("y")) {
                baseComPath = commandLine.getOptionValue("y");
            }
            if (commandLine.hasOption("v")) {
                String pagestr = commandLine.getOptionValue("v");
                pages = pagestr.split("_");
            }
            force = commandLine.hasOption("f");
            if (commandLine.hasOption("h")) {
                usage(options);
            }
            String[] extraArgs = commandLine.getArgs();
            if (extraArgs.length > 0) {
                types = extraArgs;
            }
            if (commandLine.hasOption("loader"))
                _loader = commandLine.getOptionValue("loader");
        } catch (Exception e) {
            usage(options);
        }
        Ioc ioc = new NutIoc(new JsonLoader(configPath));
        Loader loader = (Loader) Mirror.me(Lang.loadClassQuite("cn.wizzer.app.code.commons.builder." + Strings.upperFirst(_loader) + "DescLoader")).born();
        Map<String, TableDescriptor> tables = loader.loadTables(ioc,
                basePackageName,
                basePath,
                baseUri,
                servicePackageName,
                modelPackageName);

        for (Map.Entry<String, TableDescriptor> entry : tables.entrySet()) {
            String tableName = entry.getKey();
            if (excludePattern != null) {
                if (excludePattern.matcher(tableName).find()) {
                    log.debug("skip " + tableName);
                    continue;
                }
            }
            if (includePattern != null) {
                if (!includePattern.matcher(tableName).find()) {
                    log.debug("skip " + tableName);
                    continue;
                }
            }

            TableDescriptor table = entry.getValue();

            log.debug("generate " + tableName + " ...");
            Generator generator = new Generator(tables, table);
            Map<String, String> typeMap = new HashMap<String, String>();
            typeMap.put("model", modelPackageName);
            typeMap.put("service", servicePackageName);
            typeMap.put("serviceImpl", servicePackageName);
            typeMap.put("controller", controllerPackageName);

            for (String type : new String[]{"model", "service", "serviceImpl", "controller", "view", "locale"}) {
                if (!isTypeMatch(types, type)) {
                    continue;
                }
                if (type.equals("view")) {
                    String viewpath = "view";
                    if (!hasLocale(types)) {
                        viewpath = "view_notlocale";
                    }
                    if (vue) {
                        viewpath = "vue";
                        if (!hasLocale(types)) {
                            viewpath = "vue_notlocale";
                        }
                        generateViewsVue(projectPath + "/" + outputDir + "/src/main", force, viewpath, table, generator, pages);
                    } else
                        generateViews(projectPath + "/" + outputDir + "/src/main", force, viewpath, table, generator, pages);
                } else if (type.equals("locale")) {
                    generateLocales(projectPath + "/" + outputDir + "/src/main", force, table, generator);
                } else {
                    if (loader instanceof EntityDescLoader && type.equals("model")) {
                        continue;
                    }
                    String packageName = basePackageName + "." + typeMap.get(type);
                    String path = projectPath + "/" + outputDir + "/src/main/java";
                    if ("service".equals(type)) {
                        path = projectPath + "/" + baseComPath + "/src/main/java";
                        packageName = servicePackageName;
                    }
                    if ("controller".equals(type)) {
                        packageName = controllerPackageName;
                    }
                    String templatePath = "templet/" + type.toLowerCase() + ".vm";

                    if ("controller".equals(type) && vue) {
                        templatePath = "templet/controller_vue.vm";
                    }
                    String packagePath = packageName.replace('.', '/');
                    String className = table.getEntityClassName();

                    if (!"model".equals(type)) {
                        className = Utils.UPPER_CAMEL(className) + Strings.upperFirst(type);
                    }
                    if ("serviceImpl".equals(type)) {
                        path = projectPath + "/" + baseNbPath + "/src/main/java";
                        packageName = servicePackageName;
                        packagePath = (packageName + ".impl").replace('.', '/');
                    }
                    log.debug("outputDir::" + outputDir);
                    File file = new File(path, packagePath + "/" + className + ".java");
                    log.debug("generate " + file.getName());
                    generator.generate(packageName, templatePath, file, force);
                }
            }
        }
        ioc.depose();
        log.debug("done!");
    }

    private static boolean hasLocale(String[] types) {
        for (String t : types) {
            if (t.equalsIgnoreCase("locale")) {
                return true;
            }
        }
        return false;
    }

    private static boolean isTypeMatch(String[] types, String type) {
        for (String t : types) {
            if (t.equalsIgnoreCase(type) || (t + "Impl").equalsIgnoreCase(type) || "all".equalsIgnoreCase(t)) {
                return true;
            }
        }
        return false;
    }

    private static void generateLocales(String outputDir, boolean force,
                                        TableDescriptor table,
                                        Generator generator)
            throws IOException {

        String templatePath = "templet/locale.vm";
        File file = new File(outputDir + "/resources/locales/zh_CN/"
                + table.getModelName()
                + "/"
                + table.getModelSubName()
                + ".properties");
        log.debug("generate html:" + file.getName());
        generator.generate(null, templatePath, file, force);

    }

    private static void generateViews(String outputDir, boolean force, String viewpath,
                                      TableDescriptor table,
                                      Generator generator,
                                      String[] pages)
            throws IOException {

        for (String view : pages) {
            String templatePath = "templet/" + viewpath + "/" + view + ".html.vm";
            File file = new File(outputDir + "/resources/views/"
                    + table.getViewBasePath()
                    + "/"
                    + view
                    + ".html");
            log.debug("generate html:" + file.getName());
            generator.generate(null, templatePath, file, force);
        }
    }

    private static void generateViewsVue(String outputDir, boolean force, String viewpath,
                                         TableDescriptor table,
                                         Generator generator,
                                         String[] pages)
            throws IOException {

        String templatePath = "templet/" + viewpath + "/index.html.vm";
        File file = new File(outputDir + "/resources/views/"
                + table.getViewBasePath()
                + "/index.html");
        log.debug("generate html:" + file.getName());
        generator.generate(null, templatePath, file, force);

    }

    private static void usage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Main [options] [all|entity|service|controller|view]", options);
        System.exit(1);
    }

}
