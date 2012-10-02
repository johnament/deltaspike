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
package org.apache.deltaspike.jsf.impl.test.scope.view;


import java.net.URL;

import org.apache.deltaspike.test.category.WebProfileCategory;
import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
//X import org.jboss.arquillian.warp.WarpTest;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

/**
 * Test for the DeltaSpike ViewScoped context
 */
//X @WarpTest
@RunWith(Arquillian.class)
@Category(WebProfileCategory.class)
public class ViewScopedContextTest
{
    @Drone
    private GrapheneSelenium browser;

    @ArquillianResource
    private URL contextPath;

    /**
     * X TODO creating a WebArchive is only a workaround because JavaArchive
     * cannot contain other archives.
     */
    @Deployment
    public static WebArchive deploy()
    {
        // JAR with NO beans.xml!
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "viewScopedContextTest.jar")
//X                 .addPackage(ViewScopedContextTest.class.getPackage())
                .addPackage(GrapheneSelenium.class.getPackage())
                .addPackage(WebProfileCategory.class.getPackage());

        return ShrinkWrap
                .create(WebArchive.class, "viewScopedContextTest.war")
                .addAsLibrary(testJar)

                //X .addAsLibraries(ArchiveUtils.getDeltaSpikeCoreAndJsfArchive())
                .addAsWebInfResource("viewScopedContextTest/WEB-INF/web.xml", "web.xml")
                .addAsWebResource("viewScopedContextTest/index.html", "index.html")
                .addAsWebResource("viewScopedContextTest/page1.xhtml", "page1.xhtml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }


    @Test
    @RunAsClient
    public void testViewScopedContext()
    {
        browser.open(contextPath);
    }

}
