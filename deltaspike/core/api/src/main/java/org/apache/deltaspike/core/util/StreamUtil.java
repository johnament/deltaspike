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

package org.apache.deltaspike.core.util;

import javax.enterprise.inject.Typed;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

@Typed()
public abstract class StreamUtil
{
    private static boolean streamSupported = true;
    private static Class<?> streamClass;
    private static Method streamMethod;

    static
    {
        try
        {
            streamClass = Class.forName("java.util.stream.Stream");
            streamMethod = Collection.class.getMethod("stream");
        }
        catch (Exception e)
        {
            streamSupported = false;
            streamClass = null;
            streamMethod = null;
        }
    }

    public static boolean isStreamSupported()
    {
        return streamSupported;
    }

    public static boolean isStreamReturned(Method method)
    {
        return isStreamSupported() && method.getReturnType().isAssignableFrom(streamClass);
    }

    public static Object wrap(Object input)
    {
        if (!isStreamSupported() || input == null)
        {
            return input;
        }
        try
        {
            return streamMethod.invoke(input);
        }
        catch (IllegalAccessException e)
        {
        }
        catch (InvocationTargetException e)
        {
        }
        return null;
    }
}
