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

import elemental.dom.Element;
import elemental.html.SpanElement;

import org.eclipse.che.ide.extension.machine.client.perspective.widgets.machine.panel.MachineTreeNode;
import org.eclipse.che.ide.ui.tree.NodeRenderer;
import org.eclipse.che.ide.ui.tree.TreeNodeElement;
import org.eclipse.che.ide.util.dom.Elements;

/**
 * Renderer for {@ProcessTreeNode} UI presentation.
 *
 * @author Anna Shumilova
 */
public class ProcessTreeRenderer implements NodeRenderer<ProcessTreeNode> {

    @Override
    public Element getNodeKeyTextContainer(SpanElement treeNodeLabel) {
        return (Element)treeNodeLabel.getChildNodes().item(1);
    }

    @Override
    public SpanElement renderNodeContents(ProcessTreeNode data) {
        SpanElement root = Elements.createSpanElement();

        Element nodeName = Elements.createSpanElement();
        nodeName.setTextContent(data.getName());

        root.appendChild(nodeName);

        return root;
    }

    @Override
    public void updateNodeContents(TreeNodeElement<ProcessTreeNode> treeNode) {
    }
}
