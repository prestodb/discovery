/*
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
package io.airlift.discovery.store;

import com.facebook.airlift.json.JsonCodec;
import org.testng.annotations.Test;

import static com.facebook.airlift.json.JsonCodec.jsonCodec;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.assertEquals;

public class TestEntry
{
    @Test
    public void testEntry()
    {
        Entry entry = entryOf("fruit", "apple", 123, 456);

        assertEquals(entry.getKey(), "fruit".getBytes(UTF_8));
        assertEquals(entry.getValue(), "apple".getBytes(UTF_8));
        assertEquals(entry.getVersion().getSequence(), 123);
        assertEquals(entry.getTimestamp(), 456);
        assertEquals(entry.getMaxAgeInMs(), null);
    }

    @Test
    public void testSerialization()
            throws Exception
    {
        JsonCodec<Entry> codec = jsonCodec(Entry.class);

        Entry expected = entryOf("fruit", "apple", 123, 456);
        Entry actual = codec.fromJson(codec.toJsonBytes(expected));

        assertEquals(actual, expected);
    }

    private static Entry entryOf(String key, String value, long version, long timestamp)
    {
        return new Entry(key.getBytes(UTF_8), value.getBytes(UTF_8), new Version(version), timestamp, null);
    }
}
