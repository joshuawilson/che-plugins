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

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.ide.api.parts.PartStackUIResources;
import org.eclipse.che.ide.api.parts.base.BaseView;
import org.eclipse.che.ide.extension.machine.client.MachineResources;
import org.eclipse.che.ide.extension.machine.client.outputspanel.OutputsContainerView;
import org.vectomatic.dom.svg.ui.SVGImage;

/**
 * Implementation of {@link ProcessesView}.
 *
 * @author Anna Shumilova
 */
@Singleton
public class ProcessesViewImpl extends BaseView<ProcessesView.ActionDelegate> implements ProcessesView {

    @UiField
    MachineResources resources;

    @Inject
    public ProcessesViewImpl(PartStackUIResources partStackUIResources, ProcessesViewImplUiBinder uiBinder) {
        super(partStackUIResources);

        setContentWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    protected void focusView() {
       //TODO
        getElement().focus();
    }

    interface ProcessesViewImplUiBinder extends UiBinder<Widget, ProcessesViewImpl> {
    }
}
