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
package org.eclipse.che.ide.ext.java;

import org.eclipse.che.jdt.JavadocFinder;
import org.eclipse.che.jdt.javadoc.JavadocContentAccess2;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Evgen Vidolob
 */
public class JavadocTest extends BaseTest {
    private String urlPart = "http://localhost:8080/ws/java-ca?projectpath=/test&handle=";
    private JavadocFinder finder;


    @Before
    public void createFinder() throws Exception {
        finder = new JavadocFinder(urlPart);
    }

    @Test
    public void binaryObjectDoc() throws JavaModelException {
        IType type = project.findType("java.lang.Object");
        assertThat(type).isNotNull();
        String htmlContent = JavadocContentAccess2.getHTMLContent(type, true, urlPart);
        Assert.assertNotNull(htmlContent);
        assertThat(htmlContent).isNotNull().isNotEmpty().contains("Class <code>Object</code> is the root of the class hierarchy.");
    }

    @Test
    public void findObjectDoc() throws JavaModelException {
        String javadoc = finder.findJavadoc(project, "Ljava/lang/Object;");
        Assert.assertNotNull(javadoc);
        assertThat(javadoc).isNotNull().isNotEmpty().contains("Class <code>Object</code> is the root of the class hierarchy.");
    }

    @Test
    public void binaryMethodDoc() throws JavaModelException {
        IType type = project.findType("java.lang.Object");
        assertThat(type).isNotNull();
        IMethod method = type.getMethod("hashCode", null);
        assertThat(method).isNotNull();
        String htmlContent = JavadocContentAccess2.getHTMLContent(method, true,urlPart);
        assertThat(htmlContent).isNotNull().isNotEmpty().contains("Returns a hash code value for the object.");
    }

    @Test
    public void binaryGenericMethodDoc() throws JavaModelException {
        IType type = project.findType("java.util.List");
        assertThat(type).isNotNull();
        IMethod method = type.getMethod("add", new String[]{"TE;"});
        assertThat(method).isNotNull();
        String htmlContent = JavadocContentAccess2.getHTMLContent(method, true, urlPart);
        assertThat(htmlContent).isNotNull().isNotEmpty().contains("Appends the specified element to the end of this list (optional");
    }

    @Test
    public void binaryFieldDoc() throws JavaModelException {
        IType type = project.findType("java.util.ArrayList");
        assertThat(type).isNotNull();
        IField field = type.getField("size");
        assertThat(field).isNotNull();
        String htmlContent = JavadocContentAccess2.getHTMLContent(field, true, urlPart);
        assertThat(htmlContent).isNotNull().isNotEmpty().contains("The size of the ArrayList (the number of elements it contains).");
    }

    @Test
    public void binaryGenericObjectDoc() throws JavaModelException {
        IType type = project.findType("java.util.ArrayList");
        assertThat(type).isNotNull();
        String htmlContent = JavadocContentAccess2.getHTMLContent(type, true, urlPart);
        assertThat(htmlContent).isNotNull().isNotEmpty().contains("Resizable-array implementation of the <tt>List</tt> interface.");
    }

    @Test
    public void binaryHandle() throws JavaModelException, URISyntaxException, UnsupportedEncodingException {
        testDoc("<java.lang(String.class☃String☂java.lang.StringBuffer",
                "A thread-safe, mutable sequence of characters.");
    }

    @Test
    public void binaryHandleMethod() throws JavaModelException, URISyntaxException, UnsupportedEncodingException {
        testDoc("<java.nio.charset(CharsetDecoder.class☃CharsetDecoder☂☂replaceWith☂java.lang.String",
                "Changes this decoder's replacement value.");
    }

    @Test
    public void exceptionMethod() throws JavaModelException, URISyntaxException, UnsupportedEncodingException {
        testDoc("<java.lang(Throwable.class☃Throwable~Throwable~Ljava.lang.String;~Ljava.lang.Throwable;~Z~Z☂☂getStackTrace",
                "Provides programmatic access to the stack trace information printed by");
    }

    @Test
    public void getContextClassLoadingMethod() throws JavaModelException, URISyntaxException, UnsupportedEncodingException {
        testDoc("<java.nio.charset.spi(CharsetProvider.class☃CharsetProvider☂java.lang.Thread☂getContextClassLoader☂",
                "Returns the context ClassLoader for this Thread.");
    }

    private void testDoc(String handle, String content){
        String handl = getHandldeForRtJarStart() + handle;
        String javadoc = finder.findJavadoc4Handle(project, handl);
        assertThat(javadoc).contains(content);
    }

}
