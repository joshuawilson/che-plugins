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
package org.eclipse.che.ide.extension.machine.client.processes;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import org.eclipse.che.api.machine.gwt.client.MachineServiceClient;
import org.eclipse.che.api.machine.shared.dto.MachineDescriptor;
import org.eclipse.che.api.promises.client.Operation;
import org.eclipse.che.api.promises.client.OperationException;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.commons.annotation.Nullable;
import org.eclipse.che.ide.Resources;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.ide.api.parts.HasView;
import org.eclipse.che.ide.api.parts.base.BasePresenter;
import org.eclipse.che.ide.extension.machine.client.MachineLocalizationConstant;
import org.eclipse.che.ide.extension.machine.client.inject.factories.EntityFactory;
import org.eclipse.che.ide.extension.machine.client.machine.Machine;
import org.eclipse.che.ide.extension.machine.client.perspective.widgets.machine.panel.MachineTreeNode;
import org.eclipse.che.ide.ui.dialogs.DialogFactory;
import org.vectomatic.dom.svg.ui.SVGResource;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for managing machines process and terminals.
 *
 * @author Anna Shumilova
 */
@Singleton
public class ProcessesPresenter extends BasePresenter implements ProcessesView.ActionDelegate, HasView{

    private final MachineLocalizationConstant localizationConstant;
    private final DialogFactory               dialogFactory;
    private final ProcessesView               view;
    private final Resources                   resources;
    private final AppContext                  appContext;
    private final MachineServiceClient        machineService;
    private final EntityFactory               entityFactory;

    @Inject
    public ProcessesPresenter(ProcessesView view,
                              MachineLocalizationConstant localizationConstant,
                              EventBus eventBus,
                              MachineServiceClient machineService,
                              DialogFactory dialogFactory,
                              EntityFactory entityFactory,
                              Resources resources, AppContext appContext) {
        this.view = view;
        this.localizationConstant = localizationConstant;
        this.dialogFactory = dialogFactory;
        this.resources = resources;
        this.entityFactory = entityFactory;
        this.appContext = appContext;
        this.machineService = machineService;

        this.view.setDelegate(this);
        this.fetchMachines();
    }

    @Override
    public View getView() {
        return view;
    }

    @NotNull
    @Override
    public String getTitle() {
        return localizationConstant.viewProcessesTitle();
    }

    @Override
    public void setVisible(boolean visible) {
        view.setVisible(visible);
    }

    @Nullable
    @Override
    public SVGResource getTitleSVGImage() {
        return resources.outputPartIcon();
    }

    @Override
    public String getTitleToolTip() {
        return localizationConstant.viewProcessesTooltip();
    }

    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    /** Get the list of all available machines.*/
    public void fetchMachines() {
        String workspaceId = appContext.getWorkspace().getId();

        Promise<List<MachineDescriptor>> machinesPromise = machineService.getWorkspaceMachines(workspaceId);

        machinesPromise.then(new Operation<List<MachineDescriptor>>() {
            @Override
            public void apply(List<MachineDescriptor> machines) throws OperationException {
                List<ProcessTreeNode> rootChildren = new ArrayList<>();

                ProcessTreeNode rootNode = new ProcessTreeNode(null, "root", rootChildren);

                for (MachineDescriptor descriptor : machines) {
                    Machine machine = entityFactory.createMachine(descriptor);
                    ProcessTreeNode machineNode = new ProcessTreeNode(rootNode, machine, null);
                    rootChildren.add(machineNode);
                }
                view.setProcessesData(rootNode);
            }
        });
    }
}
