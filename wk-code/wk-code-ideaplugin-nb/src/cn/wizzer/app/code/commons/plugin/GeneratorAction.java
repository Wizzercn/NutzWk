package cn.wizzer.app.code.commons.plugin;

import cn.wizzer.app.code.commons.plugin.ui.ConfigDialog;
import cn.wizzer.app.code.commons.plugin.ui.ErrorDialog;
import cn.wizzer.app.code.commons.plugin.utils.GenerateConfig;
import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.execution.runners.ExecutionUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * Created by wizzer on 2017/1/23.
 */
public class GeneratorAction extends AnAction {
    Project project = null;

    public void actionPerformed(AnActionEvent e) {
        project = e.getData(CommonDataKeys.PROJECT);
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiClass rootClass = getTargetClass(e, editor);
        if (null == rootClass) {
            new ErrorDialog(project).show();
            return;
        }
        ConfigDialog fieldDialog = null;
        try {
            fieldDialog = new ConfigDialog(rootClass);
        } catch (Exception ex) {
            new ErrorDialog(project, "error", "\npackage名成不规范").show();
            return;
        }
        fieldDialog.show();
        if (!fieldDialog.isOK()) {
            return;
        }

        generateCode(rootClass, fieldDialog.getGenerateConfig());
    }

    private void generateCode(final PsiClass rootClass,
                              final GenerateConfig generateConfig) {
        RunManager runManager = RunManager.getInstance(project);
        ApplicationConfiguration appConfig = new ApplicationConfiguration("generator", project, ApplicationConfigurationType.getInstance());
        appConfig.MAIN_CLASS_NAME = "cn.wizzer.app.code.commons.builder.Generator";
        String entityClassName = rootClass.getName();
        StringBuilder programArgs = new StringBuilder();
        programArgs.append("-i ").append(entityClassName).append(" -u ").append(generateConfig.getBaseUri())
                .append(" -b ").append(generateConfig.getBaseModelPath())
                .append(" -y ").append(generateConfig.getBaseComPath())
                .append(" -n ").append(generateConfig.getBaseNbPath())
                .append(" -j ").append(project.getBasePath())
                .append(" -o ").append(generateConfig.getBasePath())
                .append(" -p ").append(generateConfig.getBasePackage()).append(generateConfig.isForce() ? " -f" : "")
                .append(generateConfig.getPages().length() > 1 ? (" -v " + generateConfig.getPages()) : "")
                .append(" -mod ").append(generateConfig.getModelPakName())
                .append(" -ctr ").append(generateConfig.getControllerPakName())
                .append(" -sev ").append(generateConfig.getServicePakName())
                .append(" -vue ").append(generateConfig.isVue())
                .append(generateConfig.isConroller() ? " controller" : "")
                .append(generateConfig.isService() ? " service" : "")
                .append(generateConfig.isLocale() ? " locale" : "")
                .append(generateConfig.isView() ? " view" : "");

        appConfig.PROGRAM_PARAMETERS = programArgs.toString();

        appConfig.WORKING_DIRECTORY = project.getBasePath();
//        String pomFile = appConfig.WORKING_DIRECTORY + "/" + "pom.xml";
//        try {
//            String pomTxt = Files.toString(new File(pomFile), Charset.forName("utf8"));
//            if (!pomTxt.contains("wk-code-codegenerator")) {
//                new ErrorDialog(project, "没有添加 wk-code-codegenerator依赖", "请在pom.xml文件中添加依赖：\r\n" +
//                        "        <dependency>\n" +
//                        "            <groupId>cn.wizzer</groupId>\n" +
//                        "            <artifactId>wk-code-codegenerator</artifactId>\n" +
//                        "            <version>1.0.1</version>\n" +
//                        "        </dependency>").show();
//                return;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            new ErrorDialog(project, "error", ex.getMessage() + "\n该项目不是一个有效的maven项目，暂时不支持使用插件\n" +
//                    "下载wk-code-codegenerator.jar的包添加到项目中，\n" +
//                    "然后使用命令行生成代码").show();
//            return;
//        }
        if (!generateConfig.getBasePath().isEmpty()) {
            appConfig.WORKING_DIRECTORY = project.getBasePath() + "/" + generateConfig.getBaseModelPath();
        }
        System.out.println("project.getBasePath()::" + project.getBasePath());
        System.out.println("generateConfig.getBaseModelPath()::" + generateConfig.getBaseModelPath());
        System.out.println("generateConfig.getBasePath()::" + generateConfig.getBasePath());
        System.out.println("appConfig.WORKING_DIRECTORY::" + appConfig.WORKING_DIRECTORY);
        Module[] modules = ModuleManager.getInstance(project).getModules();
        for(Module module:modules){
            if(module.getName().contains("model")){
                appConfig.setModule(module);
                break;
            }
        }
        RunnerAndConfigurationSettings configuration = runManager.createConfiguration(appConfig, appConfig.getFactory());
        runManager.addConfiguration(configuration, true);
        Executor executor = ExecutorRegistry.getInstance().getExecutorById(com.intellij.openapi.wm.ToolWindowId.DEBUG);
        ExecutionUtil.runConfiguration(configuration, executor);

    }

    private PsiClass getTargetClass(AnActionEvent e, Editor editor) {
        final PsiFile file = e.getData(LangDataKeys.PSI_FILE);
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = file.findElementAt(offset);
        return PsiTreeUtil.getParentOfType(element, PsiClass.class);
    }

    @Override
    public void update(AnActionEvent e) {
        final Project project = e.getData(CommonDataKeys.PROJECT);
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        final PsiClass psiClass = getTargetClass(e, editor);
        e.getPresentation().setVisible((null != project && null != editor && null != psiClass &&
                !psiClass.isEnum() && 0 != psiClass.getAllFields().length));
    }
}
