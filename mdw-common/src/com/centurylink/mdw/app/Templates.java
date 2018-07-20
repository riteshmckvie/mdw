/*
 * Copyright (C) 2017 CenturyLink, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.centurylink.mdw.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.centurylink.mdw.util.file.FileHelper;

/**
 * Access to mdw CLI templates (not used by CLI but internally).
 * TODO: substitution mechanism
 */
public class Templates {

    private static Templates instance;
    private Map<String,byte[]> templates = new LinkedHashMap<>();

    public static String get(String path) throws IOException {
        byte[] bytes = getBytes(path);
        return bytes == null ? null : new String(bytes);
    }

    public static byte[] getBytes(String path) throws IOException {
        if (instance == null)
            instance = new Templates();
        byte[] bytes = instance.templates.get(path);
        if (bytes == null)
            bytes = instance.loadTemplate(path);
        return bytes;
    }

    private byte[] loadTemplate(String path) throws IOException {
        InputStream stream = null;
        try {
            stream = openTemplateFile(path);
            if (stream == null) {
                return null;
            }
            else {
                byte[] targetArray = new byte[stream.available()];
                stream.read(targetArray);
                return targetArray;
            }
        }
        finally {
            if (stream != null)
                stream.close();
        }
    }

    private InputStream openTemplateFile(String path) throws IOException {
        return FileHelper.readFile("templates/" + path, Templates.class.getClassLoader());
    }


}