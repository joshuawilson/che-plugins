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

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.ide.api.parts.PartStackUIResources;
import org.eclipse.che.ide.api.parts.base.BaseView;
import org.eclipse.che.ide.extension.machine.client.MachineResources;
import org.eclipse.che.ide.ui.tree.Tree;

import javax.validation.constraints.NotNull;

/**
 * Implementation of {@link ProcessesView}.
 *
 * @author Anna Shumilova
 */
@Singleton
public class ProcessesViewImpl extends BaseView<ProcessesView.ActionDelegate> implements ProcessesView {

    @UiField(provided = true)
    MachineResources      machineResources;
    @UiField(provided = true)
    Tree<ProcessTreeNode> processTree;
    @UiField
    DeckLayoutPanel outputPanel;

    @Inject
    public ProcessesViewImpl(org.eclipse.che.ide.Resources resources, MachineResources machineResources,
                             PartStackUIResources partStackUIResources, ProcessesViewImplUiBinder uiBinder, ProcessTreeRenderer renderer,
                             ProcessDataAdapter adapter) {
        super(partStackUIResources);
        this.machineResources = machineResources;
        renderer.setAddTerminalClickHandler(new AddTerminalClickHandler() {
            @Override
            public void onAddTerminalClick(@NotNull String machineId) {
                delegate.onAddTerminal(machineId);
            }
        });
        processTree = Tree.create(resources, adapter, renderer);
        processTree.asWidget().addStyleName(this.machineResources.getCss().processTree());

        setContentWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    protected void focusView() {
        //TODO
        getElement().focus();
    }

    @Override
    public void addConsole(IsWidget widget) {
        outputPanel.add(widget);
        outputPanel.showWidget(outputPanel.getWidgetIndex(widget));
    }

    @Override
    public void setProcessesData(@NotNull ProcessTreeNode root) {
        processTree.asWidget().setVisible(true);
        processTree.getModel().setRoot(root);
        processTree.renderTree(-1);
    }

    interface ProcessesViewImplUiBinder extends UiBinder<Widget, ProcessesViewImpl> {
    }

}
