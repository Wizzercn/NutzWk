package cn.wizzer.app.code.commons.plugin.ui;

import cn.wizzer.app.code.commons.plugin.utils.GenerateConfig;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.panels.HorizontalBox;
import com.intellij.ui.components.panels.VerticalBox;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ghost on 2016/4/1.
 */
public class ConfigDialog extends DialogWrapper {
    private JBCheckBox controllerCheckBox;
    private JBCheckBox serviceCheckBox;
    private JBCheckBox viewCheckBox;
    private JBCheckBox vueCheckBox;

    private JBCheckBox viewAddCheckBox;
    private JBCheckBox viewDetailCheckBox;
    private JBCheckBox viewEditCheckBox;
    private JBCheckBox viewIndexCheckBox;

    private JBCheckBox localeCheckBox;

    private JBCheckBox forceCheckBox;
    private JBLabel baseModelPathLabel;
    private JBLabel basePathLabel;
    private JBLabel baseComPathLabel;
    private JBLabel baseNbPathLabel;
    private JBLabel baseUriLabel;
    private JBLabel basePackageLabel;
    private JBTextField basePathTextField;
    private JBTextField baseComPathTextField;
    private JBTextField baseNbPathTextField;
    private JBTextField baseModelPathTextField;
    private JBTextField baseUriTextField;
    private JBTextField basePackageTextField;


    private JBLabel modPackageLabel;
    private JBLabel serPackageLabel;
    private JBLabel ctrPackageLabel;
    private JBTextField modPackageTextField;
    private JBTextField serPackageTextField;
    private JBTextField ctrPackageTextField;

    private final PsiClass mClass;
    private final String basePackage;
    private final String basePerPackage;
    private final String baseUri;
    private final String modelPackageName;
    private final String modelUriName;

    public ConfigDialog(final PsiClass psiClass) {
        super(psiClass.getProject());
        String arr[] = psiClass.getQualifiedName().split("\\.");
        String modelName = psiClass.getName();
        basePackage = psiClass.getQualifiedName().replace("." + arr[arr.length - 3] + "." + arr[arr.length - 2] + "." + arr[arr.length - 1], "");
        basePerPackage = psiClass.getQualifiedName().replace("." + arr[arr.length - 4] + "." + arr[arr.length - 3] + "." + arr[arr.length - 2] + "." + arr[arr.length - 1], "");
        modelPackageName = arr[arr.length - 2];
        modelUriName = arr[arr.length - 4];
        baseUri = "/platform/" + arr[arr.length - 4];
        mClass = psiClass;
        setupViews(modelName);
        init();


    }


    private void setupViews(String modelName) {

        setTitle("Generate Model:" + modelName);
        vueCheckBox = new JBCheckBox("vue", true);
        controllerCheckBox = new JBCheckBox("controllers", true);
        serviceCheckBox = new JBCheckBox("services", true);
        viewCheckBox = new JBCheckBox("views", true);

        viewAddCheckBox = new JBCheckBox("add", true);
        viewDetailCheckBox = new JBCheckBox("detail", true);
        viewEditCheckBox = new JBCheckBox("edit", true);
        viewIndexCheckBox = new JBCheckBox("index", true);

        localeCheckBox = new JBCheckBox("locales", true);

        forceCheckBox = new JBCheckBox("replace", false);
        baseModelPathTextField = new JBTextField("wk-app/wk-model");
        baseComPathTextField = new JBTextField("wk-app/wk-common");
        baseNbPathTextField = new JBTextField("wk-app/wk-nb-service-" + modelUriName);
        basePathTextField = new JBTextField("wk-app/wk-nb-web-vue");
        baseUriTextField = new JBTextField(baseUri);
        basePackageTextField = new JBTextField(basePackage);
        baseModelPathLabel = new JBLabel("base model Path:");
        baseComPathLabel = new JBLabel("base common Path:");
        baseNbPathLabel = new JBLabel("base NB-service Path:");
        basePathLabel = new JBLabel("base web Path:");
        baseUriLabel = new JBLabel("base Uri:");
        basePackageLabel = new JBLabel("base Package:");

        modPackageLabel = new JBLabel("models Package:");
        serPackageLabel = new JBLabel("services Package:");
        ctrPackageLabel = new JBLabel("controllers Package:");
        modPackageTextField = new JBTextField(basePackage + ".modules.models");
        modPackageTextField.disable();
        serPackageTextField = new JBTextField(basePackage + ".modules.services");
        ctrPackageTextField = new JBTextField(basePerPackage + ".web.modules.controllers.platform." + modelUriName);

        viewCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (viewCheckBox.isSelected()) {

                    viewAddCheckBox.setSelected(true);
                    viewDetailCheckBox.setSelected(true);
                    viewEditCheckBox.setSelected(true);
                    viewIndexCheckBox.setSelected(true);

                    viewAddCheckBox.setVisible(true);
                    viewDetailCheckBox.setVisible(true);
                    viewEditCheckBox.setVisible(true);
                    viewIndexCheckBox.setVisible(true);
                } else {
                    viewAddCheckBox.setSelected(false);
                    viewDetailCheckBox.setSelected(false);
                    viewEditCheckBox.setSelected(false);
                    viewIndexCheckBox.setSelected(false);

                    viewAddCheckBox.setVisible(false);
                    viewDetailCheckBox.setVisible(false);
                    viewEditCheckBox.setVisible(false);
                    viewIndexCheckBox.setVisible(false);
                }
            }
        });

        vueCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (vueCheckBox.isSelected()) {
                    basePathTextField.setText("wk-app/wk-nb-web-vue");
                } else {
                    basePathTextField.setText("wk-app/wk-nb-web-platform");
                }
            }
        });
    }

    @Nullable
    @Override
    protected JComponent createSouthPanel() {
        JComponent southPanel = super.createSouthPanel();
        if (null == southPanel) {
            return null;
        }
        final VerticalBox root = new VerticalBox();
        root.add(baseModelPathLabel);
        root.add(baseModelPathTextField);
        root.add(baseComPathLabel);
        root.add(baseComPathTextField);
        root.add(baseNbPathLabel);
        root.add(baseNbPathTextField);
        root.add(basePathLabel);
        root.add(basePathTextField);
        root.add(baseUriLabel);
        root.add(baseUriTextField);
        root.add(basePackageLabel);
        root.add(basePackageTextField);

        root.add(modPackageLabel);
        root.add(modPackageTextField);
        root.add(serPackageLabel);
        root.add(serPackageTextField);
        root.add(ctrPackageLabel);
        root.add(ctrPackageTextField);

        root.add(forceCheckBox);
        root.add(southPanel);
        return root;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JComponent centerPanel = super.createContentPane();
        final VerticalBox verticalBox = new VerticalBox();
        final HorizontalBox horizontalBox1 = new HorizontalBox();

        final HorizontalBox horizontalBox2 = new HorizontalBox();

        horizontalBox1.add(vueCheckBox);
        horizontalBox1.add(controllerCheckBox);
        horizontalBox1.add(serviceCheckBox);
        horizontalBox1.add(localeCheckBox);
        horizontalBox1.add(viewCheckBox);

        horizontalBox2.add(viewAddCheckBox);
        horizontalBox2.add(viewDetailCheckBox);
        horizontalBox2.add(viewEditCheckBox);
        horizontalBox2.add(viewIndexCheckBox);
        verticalBox.add(horizontalBox1);
        verticalBox.add(horizontalBox2);
        centerPanel.add(verticalBox);
        return centerPanel;
    }

    public GenerateConfig getGenerateConfig() {
        GenerateConfig config = new GenerateConfig();
        config.setBasePackage(basePackage);
        config.setBaseModelPath(baseModelPathTextField.getText().trim());
        config.setBasePath(basePathTextField.getText().trim());
        config.setBaseComPath(baseComPathTextField.getText().trim());
        config.setBaseNbPath(baseNbPathTextField.getText().trim());
        config.setBaseUri(baseUriTextField.getText().trim());
        config.setVue(vueCheckBox.isSelected());
        config.setConroller(controllerCheckBox.isSelected());
        config.setService(serviceCheckBox.isSelected());
        config.setLocale(localeCheckBox.isSelected());
        config.setView(viewCheckBox.isSelected());
        config.setForce(forceCheckBox.isSelected());
        config.setModelPakName(modPackageTextField.getText().trim());
        config.setServicePakName(serPackageTextField.getText().trim());
        config.setControllerPakName(ctrPackageTextField.getText().trim());
        StringBuilder pages = new StringBuilder();
        pages.append(viewIndexCheckBox.isSelected() ? "index_" : "").append(viewAddCheckBox.isSelected() ? "add_" : "")
                .append(viewDetailCheckBox.isSelected() ? "detail_" : "").append(viewEditCheckBox.isSelected() ? "edit_" : "");
        config.setPages(pages.toString());
        return config;
    }

}
