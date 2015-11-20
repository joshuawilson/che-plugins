/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.extension.maven.server.projecttype;

import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.ForbiddenException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.project.server.FolderEntry;
import org.eclipse.che.api.project.server.VirtualFileEntry;
import org.eclipse.che.ide.maven.tools.Build;
import org.eclipse.che.ide.maven.tools.Model;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author Roman Nikitenko
 */
public class MavenClassPathConfigurator {
    private static final String CLASS_PATH_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                                     "<classpath>\n" +
                                                     "\t<classpathentry kind=\"src\" path=\"%s\"/>\n" +
                                                     "\t<classpathentry kind=\"src\" path=\"%s\"/>\n" +
                                                     "\t<classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER\"/>\n" +
                                                     "\t<classpathentry kind=\"con\" path=\"org.eclipse.che.MAVEN2_CLASSPATH_CONTAINER\"/>\n" +
                                                     "</classpath>";

    public static void configure(FolderEntry projectFolder)
            throws ServerException, ForbiddenException, ConflictException {
        try {
            VirtualFileEntry pom = projectFolder.getChild("pom.xml");
            if (pom != null) {
                Model model = Model.readFrom(pom.getVirtualFile());
                configure(projectFolder, model);
            }
        } catch (IOException e) {
            throw new ServerException("Unable read pom.xml.");
        }
    }

    public static void configure(FolderEntry projectFolder, Model model) throws ServerException, ForbiddenException, ConflictException {
        FolderEntry cheFolder = (FolderEntry)projectFolder.getChild(".codenvy");

        if (cheFolder == null) {
            cheFolder = projectFolder.createFolder(".codenvy");
        }

        if (cheFolder != null && cheFolder.getVirtualFile().getChild("classpath") == null) {
            String sourceDirectory = null;
            String testDirectory = null;
            Build build = model.getBuild();
            if (build != null) {
                sourceDirectory = build.getSourceDirectory();
                testDirectory = build.getTestSourceDirectory();
            }
            sourceDirectory = sourceDirectory != null && !sourceDirectory.isEmpty() ? sourceDirectory : "src/main/java";
            testDirectory = testDirectory != null && !testDirectory.isEmpty() ? testDirectory : "src/test/java";

            String classPathContent = String.format(CLASS_PATH_CONTENT, sourceDirectory, testDirectory);
            cheFolder.getVirtualFile().createFile("classpath", null, new ByteArrayInputStream(classPathContent.getBytes()));
        }
    }
}
