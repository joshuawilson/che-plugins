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
import com.google.gwt.user.client.Window;
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

    @UiField
    MachineResources      resources;
    @UiField(provided = true)
    Tree<ProcessTreeNode> processTree;

    @Inject
    public ProcessesViewImpl(org.eclipse.che.ide.Resources resources, PartStackUIResources partStackUIResources, ProcessesViewImplUiBinder uiBinder, ProcessTreeRenderer renderer,
                             ProcessDataAdapter adapter) {
        super(partStackUIResources);
        processTree = Tree.create(resources, adapter, renderer);
        setContentWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    protected void focusView() {
        //TODO
        getElement().focus();
    }

    @Override
    public void setProcessesData(@NotNull ProcessTreeNode root) {
        Window.alert(root.getChildren().size()  + "");
        processTree.asWidget().setVisible(true);
        processTree.getModel().setRoot(root);
        processTree.renderTree(-1);
    }

    interface ProcessesViewImplUiBinder extends UiBinder<Widget, ProcessesViewImpl> {
    }
}
