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
package org.apache.deltaspike.servlet.cdise.filter.test;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.servlet.cdise.filter.CdiServletContextListener;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class ServletFilterTest
{
    private Tomcat tomcat;
    @Before
    public void build() throws LifecycleException {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();
        container.getContextControl().startContexts();
        tomcat = new Tomcat();
        tomcat.setPort(8080);
        File base = new File("target/webapp-runner");
        if(!base.exists()) {
            base.mkdirs();
        }
        Context ctx = tomcat.addContext("/",base.getAbsolutePath());
        StandardContext standardContext = (StandardContext)ctx;
        standardContext.addApplicationListener(CdiServletContextListener.class.getName());

        Wrapper wrapper = Tomcat.addServlet(ctx,"Greeter",GreeterServlet.class.getName());
        wrapper.addMapping("/*");
//        ctx.getServletContext().addListener(CdiCtrlListener.class);

//        ctx.addLifecycleListener();

        tomcat.start();
//        tomcat.getServer().await();

    }

    @After
    public void shutdown() throws Exception{
        //tomcat.stop();
    }

    @Test
    public void testRead() throws IOException {
        String url = new URL("http","localhost",8080,"/").toString();
        HttpResponse response = new DefaultHttpClient().execute(new HttpGet(url));
        assertEquals(200, response.getStatusLine().getStatusCode());
        InputStream is = response.getEntity().getContent();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String data = br.readLine();
        is.close();
        assertEquals("Hello, world!",data);
    }
}
