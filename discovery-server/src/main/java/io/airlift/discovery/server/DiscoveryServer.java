/*
 * Copyright 2010 Proofpoint, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.airlift.discovery.server;

import com.facebook.airlift.bootstrap.Bootstrap;
import com.facebook.airlift.discovery.client.Announcer;
import com.facebook.airlift.discovery.client.DiscoveryModule;
import com.facebook.airlift.event.client.HttpEventModule;
import com.facebook.airlift.http.server.HttpServerModule;
import com.facebook.airlift.jaxrs.JaxrsModule;
import com.facebook.airlift.jmx.JmxModule;
import com.facebook.airlift.jmx.http.rpc.JmxHttpRpcModule;
import com.facebook.airlift.json.JsonModule;
import com.facebook.airlift.log.Logger;
import com.facebook.airlift.node.NodeModule;
import com.facebook.airlift.tracetoken.TraceTokenModule;
import com.google.inject.Injector;
import org.weakref.jmx.guice.MBeanModule;

public final class DiscoveryServer
{
    private static final Logger log = Logger.get(DiscoveryServer.class);

    private DiscoveryServer() {}

    public static void main(String[] args)
    {
        try {
            Bootstrap app = new Bootstrap(
                    new MBeanModule(),
                    new NodeModule(),
                    new HttpServerModule(),
                    new JaxrsModule(true),
                    new JsonModule(),
                    new JmxModule(),
                    new JmxHttpRpcModule(),
                    new DiscoveryServerModule(),
                    new HttpEventModule(),
                    new TraceTokenModule(),
                    new DiscoveryModule()
            );

            Injector injector = app.strictConfig().initialize();
            injector.getInstance(Announcer.class).start();
        }
        catch (Throwable t) {
            log.error(t);
            System.exit(1);
        }
    }
}
