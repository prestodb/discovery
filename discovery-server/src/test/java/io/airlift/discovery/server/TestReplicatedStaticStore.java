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

import com.google.common.base.Supplier;
import io.airlift.discovery.store.ConflictResolver;
import io.airlift.discovery.store.DistributedStore;
import io.airlift.discovery.store.Entry;
import io.airlift.discovery.store.InMemoryStore;
import io.airlift.discovery.store.RemoteStore;
import io.airlift.discovery.store.StoreConfig;
import org.joda.time.DateTime;

import static com.facebook.airlift.json.JsonCodec.jsonCodec;

public class TestReplicatedStaticStore
    extends TestStaticStore
{
    @Override
    protected StaticStore initializeStore(Supplier<DateTime> timeSupplier)
    {
        RemoteStore dummy = new RemoteStore() {
            public void put(Entry entry) { }
        };

        DistributedStore distributedStore = new DistributedStore("static", new InMemoryStore(new ConflictResolver()), dummy, new StoreConfig(), timeSupplier);

        return new ReplicatedStaticStore(distributedStore, jsonCodec(Service.class));
    }
}
