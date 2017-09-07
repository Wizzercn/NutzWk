package cn.wizzer.app.web.modules.controllers.platform.wf;

import cn.wizzer.app.wf.modules.services.WfCategoryService;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Encoding;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.PUT;
import org.nutz.mvc.annotation.Param;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wizzer on 2017/9/5.
 */
@IocBean
@At("/platform/wf/cfg/model")
public class WfCfgModelConntroller {
    private static final Log log = Logs.get();
    @Inject
    private ProcessEngine processEngine;
    @Inject
    private RepositoryService repositoryService;
    @Inject
    private WfCategoryService wfCategoryService;

    @At("")
    @Ok("beetl:/platform/wf/model/index.html")
    @RequiresPermissions("wf.cfg.model")
    public void index(HttpServletRequest req) {
        req.setAttribute("list", wfCategoryService.query(Cnd.orderBy().asc("location")));
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("wf.cfg.model")
    public Object data(@Param("categoryId") String categoryId, @Param("keyword") String keyword, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        List<Model> list;
        long total;
        ModelQuery modelQuery = repositoryService.createModelQuery();
        ModelQuery modelQuery2;
        if (!Strings.isEmpty(categoryId) && !Strings.isEmpty(keyword)) {
            modelQuery2 = modelQuery.modelCategory(categoryId).modelNameLike(keyword);
        } else if (!Strings.isEmpty(categoryId)) {
            modelQuery2 = modelQuery.modelCategory(categoryId);
        } else if (!Strings.isEmpty(keyword)) {
            modelQuery2 = modelQuery.modelNameLike(keyword);
        } else {
            modelQuery2 = modelQuery;
        }
        total = modelQuery2.count();
        list = modelQuery2.orderByCreateTime().desc().listPage(start, length);
        NutMap re = new NutMap();
        re.put("recordsFiltered", total);
        re.put("data", list);
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;
    }

    @At("/add")
    @Ok("beetl:/platform/wf/model/add.html")
    @RequiresPermissions("wf.cfg.model")
    public void add(HttpServletRequest req) {
        req.setAttribute("list", wfCategoryService.query(Cnd.orderBy().asc("location")));
    }

    @At("/new")
    @Ok("re")
    @RequiresPermissions("wf.cfg.model")
    public String newAdd(@Param("category") String category, @Param("name") String name, @Param("key") String key, @Param("description") String description, HttpServletRequest req) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put("category", category);
            modelObjectNode.put("key", key);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            description = StringUtils.defaultString(description);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setKey(key);
            modelData.setName(name);
            modelData.setCategory(category);
            modelData.setKey(StringUtils.defaultString(key));

            repositoryService.saveModel(modelData);
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("resourceId", modelData.getId());
            ObjectNode properties = objectMapper.createObjectNode();
            properties.put("process_id", key);
            properties.put("process_namespace", category);
            properties.put("name", name);
            properties.put("documentation", description);
            editorNode.put("properties", properties);
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
            return "redirect:/platform/wf/cfg/model/editor/modeler?modelId=" + modelData.getId();
        } catch (Exception e) {
            log.error(e);
        }
        return "raw:error";
    }

    @At("/editor/modeler")
    @Ok("beetl:/platform/wf/model/modeler.html")
    @RequiresPermissions("wf.cfg.model")
    public void modeler(@Param("modelId") String modelId, HttpServletRequest req) {
        req.setAttribute("modelId", modelId);
    }

    @At("/editor/?/json")
    @Ok("raw")
    public ObjectNode getEditorJson(String modelId) {
        ObjectNode modelNode = null;

        Model model = repositoryService.getModel(modelId);

        if (model != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                if (StringUtils.isNotEmpty(model.getMetaInfo())) {
                    modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
                } else {
                    modelNode = objectMapper.createObjectNode();
                    modelNode.put(ModelDataJsonConstants.MODEL_NAME, model.getName());
                }
                modelNode.put(ModelDataJsonConstants.MODEL_ID, model.getId());
                ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(
                        new String(repositoryService.getModelEditorSource(model.getId()), "utf-8"));
                modelNode.put("model", editorJsonNode);

            } catch (Exception e) {
                log.error("Error creating model JSON", e);
                throw new ActivitiException("Error creating model JSON", e);
            }
        }
        return modelNode;
    }

    @At("/editor/?/save")
    @PUT
    @Ok("http:200")
    public void saveModel(String modelId, HttpServletRequest req) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            String body = readStreamParameter(req.getInputStream());
            String[] str = StringUtils.split(body, "&");
            for (int i = 0; i < str.length; i++) {
                String[] temp = StringUtils.split(str[i], "=");
                if (temp.length > 1) {
                    map.put(temp[0], URLDecoder.decode(temp[1], "utf-8"));
                } else {
                    map.put(temp[0], "");
                }
            }
            Model model = repositoryService.getModel(modelId);
            ObjectMapper objectMapper = new ObjectMapper();

            ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());

            modelJson.put(ModelDataJsonConstants.MODEL_NAME, Strings.sNull(map.get("name")));
            modelJson.put(ModelDataJsonConstants.MODEL_DESCRIPTION, Strings.sNull(map.get("description")));
            model.setMetaInfo(modelJson.toString());
            model.setName(Strings.sNull(map.get("name")));

            repositoryService.saveModel(model);

            repositoryService.addModelEditorSource(model.getId(), Strings.sNull(map.get("json_xml")).getBytes("utf-8"));

            InputStream svgStream = new ByteArrayInputStream(Strings.sNull(map.get("svg_xml")).getBytes("utf-8"));
            TranscoderInput input = new TranscoderInput(svgStream);

            PNGTranscoder transcoder = new PNGTranscoder();
            // Setup output
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);

            // Do the transformation
            transcoder.transcode(input, output);
            final byte[] result = outStream.toByteArray();
            repositoryService.addModelEditorSourceExtra(model.getId(), result);
            outStream.close();

        } catch (Exception e) {
            log.error("Error saving model", e);
            throw new ActivitiException("Error saving model", e);
        }
    }

    @At("/editor/stencilset")
    @Ok("raw")
    public String getStencilset() {
        InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("stencilset.json.cfg");
        try {
            return IOUtils.toString(stencilsetStream);
        } catch (Exception e) {
            throw new ActivitiException("Error while loading stencil set", e);
        }
    }

    private String readStreamParameter(ServletInputStream in) {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(new String(Strings.sNull(line).getBytes(), Encoding.UTF8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }
}
