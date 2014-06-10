/**
 * Copyright 2005-2014 Restlet
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL
 * 1.0 (the "Licenses"). You can select the license that you prefer but you may
 * not use this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.restlet.com/products/restlet-framework
 * 
 * Restlet is a registered trademark of Restlet S.A.S.
 */

package org.restlet.ext.swagger;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 * Swagger enabled application. This subclass of {@link Application} can
 * describe itself in the format described by the <a
 * href="https://github.com/wordnik/swagger-spec/wiki">Swagger specification
 * project</a>. It requires you to set up a specific end point that serves the
 * resource listing and a sub-resource that serves the API declaration of a
 * specific resource. By default, both descriptions are generated by
 * introspecting the application itself, but you can override this behavior.
 * 
 * Sample code integration:
 * <pre>
 * &#064;Override
 * public Restlet createInboundRoot() {
 *     Router apiRouter = new Router(getContext());
 * 
 *     apiRouter.attach(&quot;/contacts/&quot;, ContactListServerResource.class);
 *     apiRouter.attach(&quot;/contacts/{email}&quot;, ContactServerResource.class);
 * 
 *     attachSwaggerSpecificationRestlet(apiRouter);
 * }
 * </pre>
 * 
 * @author Thierry Boileau
 * 
 */
public class SwaggerApplication extends Application {

    /**
     * Defines two routes, one for the high level "Resource listing", and the
     * other one for the "API declaration". The second route is a sub-resource
     * of the first one, defined with the path variable "resource".
     * 
     * @param router
     *            The router on which defining the new route.
     * @param rlPath
     *            The path to which attach the Restlet that serves the resource
     *            listing.
     * @param resourceListingRestlet
     *            The Restlet that serves the resource listing.
     * @param apPath
     *            The path to which attach the Restlet that serves the
     *            declaration of a specific resource.
     * @param apiDeclarationRestlet
     *            The Restlet that serves the declaration of a specific
     *            resource.
     */
    public void attachSwaggerDocumentationRestlets(Router router,
            String rlPath, Restlet resourceListingRestlet, String apPath,
            Restlet apiDeclarationRestlet) {
        router.attach(rlPath, resourceListingRestlet);
        router.attach(apPath, apiDeclarationRestlet);
    }

    /**
     * Defines two routes, one for the high level "Resource listing" (by default
     * "/api-docs"), and the other one for the "API declaration". The second
     * route is a sub-resource of the first one, defined with the path variable
     * "resource" (ie "/api-docs/{resource}").
     * 
     * @param router
     *            The router on which defining the new route.
     */
    public void attachSwaggerSpecificationRestlet(Router router) {
        attachSwaggerSpecificationRestlet(router, "/api-docs");
    }

    /**
     * Defines two routes, one for the high level "Resource listing", and the
     * other one for the "API declaration". The second route is a sub-resource
     * of the first one, defined with the path variable "resource".
     * 
     * @param router
     *            The router on which defining the new route.
     * @param path
     *            The root path of the documentation Restlet.
     */
    public void attachSwaggerSpecificationRestlet(Router router, String path) {
        SwaggerSpecificationRestlet restlet = getSwaggerSpecificationRestlet(getContext());
        attachSwaggerDocumentationRestlets(router, path, restlet, path
                + "/{resource}", restlet);
    }

    /**
     * The dedicated {@link Restlet} that is able to generation the Swagger
     * specification formats.
     * 
     * @return The {@link Restlet} that is able to generation the Swagger
     *         specification formats.
     */
    public SwaggerSpecificationRestlet getSwaggerSpecificationRestlet(
            Context context) {
        SwaggerSpecificationRestlet result = new SwaggerSpecificationRestlet(
                context);
        result.setApiInboundRoot(this);
        return result;
    }

}