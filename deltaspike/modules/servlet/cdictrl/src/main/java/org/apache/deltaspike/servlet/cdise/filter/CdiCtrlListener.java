/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.deltaspike.servlet.cdise.filter;

import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;

@WebListener
public class CdiCtrlListener implements ServletRequestListener
{
    private static final Logger LOG = Logger.getLogger(CdiCtrlListener.class.getName());

    protected Set<Class<RequestScoped>> getContextsToStart()
    {
        return Collections.singleton(RequestScoped.class);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent)
    {
        ContextControl controller = (ContextControl)servletRequestEvent.getServletRequest()
                .getAttribute("controller");
        controller.stopContext(RequestScoped.class);
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent)
    {
        System.out.println("Doing filter.");
        ContextControl contextControl = getContextControl();
        contextControl.startContext(RequestScoped.class);
        servletRequestEvent.getServletRequest().setAttribute("controller",contextControl);
    }

    private ContextControl getContextControl()
    {
        BeanManager beanManager = CdiContainerLoader.getCdiContainer().getBeanManager();

        if (beanManager == null)
        {
            LOG.warning("If the CDI-container was started by the environment, you can't use this helper." +
                    "Instead you can resolve ContextControl manually " +
                    "(e.g. via BeanProvider.getContextualReference(ContextControl.class) ). " +
                    "If the container wasn't started already, you have to use CdiContainer#boot before.");

            return null;
        }
        Set<Bean<?>> beans = beanManager.getBeans(ContextControl.class);
        Bean<ContextControl> ctxCtrlBean = (Bean<ContextControl>) beanManager.resolve(beans);

        CreationalContext<ContextControl> ctxCtrlCreationalContext =
                beanManager.createCreationalContext(ctxCtrlBean);

        ContextControl ctxCtrl = (ContextControl)
                beanManager.getReference(ctxCtrlBean, ContextControl.class, ctxCtrlCreationalContext);
        return ctxCtrl;
    }

}
