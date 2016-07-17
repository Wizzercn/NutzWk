package cn.wizzer.common.view.velocity;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.util.ClassUtils;
import org.apache.velocity.util.ExceptionUtils;

import java.io.InputStream;

public class VelocityResourceLoader extends ClasspathResourceLoader {

    private static final String TEMPLATE_PATH = "/WEB-INF/views";

    /**
     * Get an InputStream so that the Runtime can build a template with it.
     *
     * @param name name of template to get
     * @return InputStream containing the template
     * @throws ResourceNotFoundException if template not found in classpath.
     */
    public InputStream getResourceStream(String name) throws ResourceNotFoundException {
        InputStream result = null;

        if (StringUtils.isEmpty(name)) {
            throw new ResourceNotFoundException("No template name provided");
        }

        /**
         * look for resource in thread classloader first (e.g. WEB-INF\lib in a servlet container) then fall
         * back to the system classloader.
         */

        try {
            result = ClassUtils.getResourceAsStream(getClass(), TEMPLATE_PATH + name);
        } catch (Exception fnfe) {
            throw (ResourceNotFoundException) ExceptionUtils.createWithCause(ResourceNotFoundException.class,
                    "problem with template: " + name, fnfe);
        }

        if (result == null) {
            String msg = "ClasspathResourceLoader Error: cannot find resource " + name;

            throw new ResourceNotFoundException(msg);
        }

        return result;
    }
}
