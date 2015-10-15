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
package org.eclipse.che.ide.extension.machine.client.perspective.widgets.machine.panel;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;

import org.eclipse.che.api.machine.gwt.client.MachineServiceClient;
import org.eclipse.che.api.machine.shared.dto.MachineDto;
import org.eclipse.che.api.project.shared.dto.ProjectDescriptor;
import org.eclipse.che.api.promises.client.Operation;
import org.eclipse.che.api.promises.client.OperationException;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.workspace.shared.dto.UsersWorkspaceDto;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.app.CurrentProject;
import org.eclipse.che.ide.extension.machine.client.MachineLocalizationConstant;
import org.eclipse.che.ide.extension.machine.client.inject.factories.EntityFactory;
import org.eclipse.che.ide.extension.machine.client.inject.factories.WidgetsFactory;
import org.eclipse.che.ide.extension.machine.client.machine.Machine;
import org.eclipse.che.ide.extension.machine.client.perspective.widgets.machine.appliance.MachineAppliancePresenter;
import org.eclipse.che.ide.ui.dialogs.InputCallback;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Dmitry Shnurenko
 */
@RunWith(MockitoJUnitRunner.class)
public class MachinePanelPresenterTest {

    //constructor mocks
    @Mock
    private MachinePanelView            view;
    @Mock
    private MachineServiceClient        service;
    @Mock
    private EntityFactory               entityFactory;
    @Mock
    private WidgetsFactory              widgetsFactory;
    @Mock
    private MachineLocalizationConstant locale;
    @Mock
    private MachineAppliancePresenter   appliance;
    @Mock
    private EventBus                    eventBus;
    @Mock
    private AppContext                  appContext;

    //additional mocks
    @Mock
    private Promise<List<MachineDto>> machinePromise;
    @Mock
    private CurrentProject            currentProject;
    @Mock
    private ProjectDescriptor         projectDescriptor;
    @Mock
    private MachineDto                machineDescriptor1;
    @Mock
    private MachineDto                machineDescriptor2;
    @Mock
    private Machine                   machine1;
    @Mock
    private Machine                   machine2;
    @Mock
    private AcceptsOneWidget          container;
    @Mock
    private MachineTreeNode           rootNode;
    @Mock
    private MachineTreeNode           machineNode1;
    @Mock
    private MachineTreeNode           machineNode2;
    @Mock
    private UsersWorkspaceDto         workspaceDto;

    @Captor
    private ArgumentCaptor<Operation<List<MachineDto>>> operationCaptor;
    @Captor
    private ArgumentCaptor<InputCallback>               inputCallbackCaptor;

    @InjectMocks
    private MachinePanelPresenter presenter;

    @Before
    public void setUp() {
        when(appContext.getWorkspace()).thenReturn(workspaceDto);
        when(workspaceDto.getId()).thenReturn("id");
        when(entityFactory.createMachine(machineDescriptor1)).thenReturn(machine1);
        when(entityFactory.createMachine(machineDescriptor2)).thenReturn(machine2);

        when(entityFactory.createMachineNode(isNull(MachineTreeNode.class),
                                             anyString(),
                                             Matchers.<List<MachineTreeNode>>anyObject())).thenReturn(rootNode);

        //noinspection unchecked
        when(entityFactory.createMachineNode(eq(rootNode),
                                             eq(machine1),
                                             isNull(List.class))).thenReturn(machineNode1);
        //noinspection unchecked
        when(entityFactory.createMachineNode(eq(rootNode),
                                             eq(machine2),
                                             isNull(List.class))).thenReturn(machineNode2);

        when(service.getWorkspaceMachines(anyString())).thenReturn(machinePromise);
    }

    @Test
    public void treeShouldBeDisplayedWithMachines() throws Exception {
        presenter.showMachines();

        verify(service).getWorkspaceMachines("id");

        verify(machinePromise).then(operationCaptor.capture());
        operationCaptor.getValue().apply(Arrays.asList(machineDescriptor1, machineDescriptor2));

        verify(entityFactory).createMachineNode(isNull(MachineTreeNode.class), eq("root"), Matchers.<List<MachineTreeNode>>anyObject());

        //noinspection unchecked
        verify(entityFactory).createMachineNode(eq(rootNode), eq(machine1), isNull(List.class));
        //noinspection unchecked
        verify(entityFactory).createMachineNode(eq(rootNode), eq(machine2), isNull(List.class));

        verify(view).setData(rootNode);
        verify(view).selectNode(machineNode1);

        verify(view, never()).selectNode(machineNode2);
    }

    @Test
    public void stubShouldBeDisplayedWhenMachinesNotExist() throws OperationException {
        presenter.showMachines();

        verify(machinePromise).then(operationCaptor.capture());
        operationCaptor.getValue().apply(Collections.<MachineDto>emptyList());

        verify(appliance).showStub();
        Collection<MachineTreeNode> children = rootNode.getChildren();
        assertNotNull(children);
        assertEquals(0, children.size());
        assertNull(presenter.getSelectedMachine());
        verify(view).setData(rootNode);
    }

    @Test
    public void onMachineShouldBeClicked() throws Exception {
        presenter.onMachineSelected(machine1);

        verify(appliance).showAppliance(machine1);
    }

    @Test
    public void titleShouldBeReturned() {
        presenter.getTitle();

        verify(locale).machinePanelTitle();
    }

    @Test
    public void titleImageShouldBeReturned() {
        ImageResource resource = presenter.getTitleImage();

        assertThat(resource, nullValue(ImageResource.class));
    }

    @Test
    public void titleTooltipShouldBeReturned() {
        presenter.getTitleToolTip();

        verify(locale).machinePanelTooltip();
    }

    @Test
    public void viewShouldBeSetToContainer() {
        presenter.go(container);

        verify(container).setWidget(view);
    }
}