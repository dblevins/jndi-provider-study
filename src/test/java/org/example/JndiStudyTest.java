/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.example;

import org.example.blue.BlueInitialContextFactory;
import org.example.green.GreenInitialContextFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;

public class JndiStudyTest {

    @Test
    public void jndiPropertiesFile() throws Exception {
        final InitialContext initialContext = new InitialContext();

        assertEquals("Red", initialContext.lookup("foo"));
        assertEquals("Red", initialContext.lookup("bar"));

        assertEquals("Crimson", initialContext.lookup("java:foo"));
        assertEquals("Crimson", initialContext.lookup("java:bar"));
    }

    @Test
    public void jndiPropertiesFile_SystemProperties() throws Exception {

        System.setProperty(Context.URL_PKG_PREFIXES, "org.example.blue");
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, BlueInitialContextFactory.class.getName());

        final InitialContext initialContext = new InitialContext();

        assertEquals("Blue", initialContext.lookup("foo"));
        assertEquals("Blue", initialContext.lookup("bar"));

        assertEquals("Navy", initialContext.lookup("java:foo"));
        assertEquals("Navy", initialContext.lookup("java:bar"));
    }

    @Test
    public void jndiPropertiesFile_SystemProperties_Hashtable() throws Exception {

        System.setProperty(Context.URL_PKG_PREFIXES, "org.example.blue");
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, BlueInitialContextFactory.class.getName());

        final Hashtable hashtable = new Hashtable();
        hashtable.put(Context.INITIAL_CONTEXT_FACTORY, GreenInitialContextFactory.class.getName());
        hashtable.put(Context.URL_PKG_PREFIXES, "org.example.green");

        final InitialContext initialContext = new InitialContext(hashtable);
        assertEquals("Green", initialContext.lookup("foo"));
        assertEquals("Green", initialContext.lookup("bar"));

        assertEquals("Emerald", initialContext.lookup("java:foo"));
        assertEquals("Emerald", initialContext.lookup("java:bar"));
    }

    @Test
    public void mixed_one() throws Exception {

        final Hashtable hashtable = new Hashtable();
        hashtable.put(Context.INITIAL_CONTEXT_FACTORY, GreenInitialContextFactory.class.getName());

        final InitialContext initialContext = new InitialContext(hashtable);

        assertEquals("Green", initialContext.lookup("foo"));
        assertEquals("Green", initialContext.lookup("bar"));

        assertEquals("Crimson", initialContext.lookup("java:foo"));
        assertEquals("Crimson", initialContext.lookup("java:bar"));
    }

    @Test
    public void mixed_two() throws Exception {

        final Hashtable hashtable = new Hashtable();
        hashtable.put(Context.URL_PKG_PREFIXES, "org.example.green");

        final InitialContext initialContext = new InitialContext(hashtable);

        assertEquals("Red", initialContext.lookup("foo"));
        assertEquals("Red", initialContext.lookup("bar"));

        assertEquals("Emerald", initialContext.lookup("java:foo"));
        assertEquals("Emerald", initialContext.lookup("java:bar"));
    }

    // plumbing to keep us from dirtying the system properties between tests

    private final Properties originalSystemProperties = new Properties();

    @Before
    public void saveSystemProperties() {
        originalSystemProperties.clear();
        originalSystemProperties.putAll(System.getProperties());
    }

    @After
    public void restoreSystemProperties() {
        System.getProperties().clear();
        System.getProperties().putAll(originalSystemProperties);
    }
}
