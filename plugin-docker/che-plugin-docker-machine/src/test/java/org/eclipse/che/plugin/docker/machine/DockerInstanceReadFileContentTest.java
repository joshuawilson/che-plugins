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
package org.eclipse.che.plugin.docker.machine;
/*
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.machine.server.exception.MachineException;
import org.eclipse.che.plugin.docker.client.DockerConnector;
import org.eclipse.che.plugin.docker.client.Exec;
import org.eclipse.che.plugin.docker.client.LogMessage;
import org.eclipse.che.plugin.docker.client.MessageProcessor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
*/
/**
 * @author Anton Korneta
 */
//@Listeners(value = {MockitoTestNGListener.class})
public class DockerInstanceReadFileContentTest {
/*
    private DockerInstance dockerInstance;

    @Mock
    private DockerConnector dockerConnector;
    @Mock
    private DockerInstanceStopDetector dockerInstanceStopDetector;

    @Mock
    private LogMessage logMessage;
    @Mock
    private Exec       exec;

    @BeforeMethod
    public void start() throws IOException {

        dockerInstance = spy(new DockerInstance(dockerConnector,
                                                null,
                                                null,
                                                null,
                                                null,
                                                false,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                10,
                                                dockerInstanceStopDetector));
    }


    @Test(expectedExceptions = MachineException.class)
    public void readFileContentWithNegativeValuesTest() throws MachineException {
        dockerInstance.readFileContent("filePath", -1, -10);
    }

    @Test(expectedExceptions = MachineException.class)
    public void readFileContentWithExecTroubleTest() throws MachineException, ConflictException, IOException {
        String filePath = "filePath";

        when(dockerConnector.createExec(anyString(), anyBoolean(), Matchers.<String>anyVararg())).thenThrow(new IOException("message"));

        dockerInstance.readFileContent(filePath, -1, 10);
    }

    @Test(expectedExceptions = MachineException.class, expectedExceptionsMessageRegExp = "File with path filePath not found")
    public void whenFileNotFoundSedCommandTest() throws MachineException, ConflictException, IOException {
        String filePath = "filePath";
        when(logMessage.getContent()).thenReturn("sed: can't read " + filePath + ": No such file or directory");

        when(dockerConnector.createExec(anyString(), anyBoolean(), Matchers.<String>anyVararg())).thenReturn(exec);
        doAnswer(invocationOnMock -> {
            MessageProcessor<LogMessage> processor = (MessageProcessor<LogMessage>)invocationOnMock.getArguments()[1];
            processor.process(logMessage);
            return processor;
        }).when(dockerConnector).startExec(anyString(), any());

        dockerInstance.readFileContent(filePath, 1, 10);
    }

    @Test(expectedExceptions = MachineException.class, expectedExceptionsMessageRegExp = "File with path filePath not found")
    public void whenFileNotFoundCatCommandTest() throws MachineException, ConflictException, IOException {
        String filePath = "filePath";
        when(logMessage.getContent()).thenReturn("cat: " + filePath + ": No such file or directory");

        when(dockerConnector.createExec(anyString(), anyBoolean(), Matchers.<String>anyVararg())).thenReturn(exec);
        doAnswer(invocationOnMock -> {
            MessageProcessor<LogMessage> processor = (MessageProcessor<LogMessage>)invocationOnMock.getArguments()[1];
            processor.process(logMessage);
            return processor;
        }).when(dockerConnector).startExec(anyString(), any());

        dockerInstance.readFileContent(filePath, 1, 10);
    }

    @Test
    public void readFileContentTest() throws MachineException, ConflictException, IOException {
        String filePath = "filePath";
        String content = "content";
        when(logMessage.getContent()).thenReturn(content);

        when(dockerConnector.createExec(anyString(), anyBoolean(), Matchers.<String>anyVararg())).thenReturn(exec);
        doAnswer(invocationOnMock -> {
            MessageProcessor<LogMessage> processor = (MessageProcessor<LogMessage>)invocationOnMock.getArguments()[1];
            processor.process(logMessage);
            return processor;
        }).when(dockerConnector).startExec(anyString(), any());

        String result = dockerInstance.readFileContent(filePath, 1, 10);

        assertEquals(result.trim(), content);
    }*/
}