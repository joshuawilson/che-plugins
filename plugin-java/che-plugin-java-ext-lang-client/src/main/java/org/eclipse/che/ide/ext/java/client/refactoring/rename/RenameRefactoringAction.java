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
package org.eclipse.che.ide.ext.java.client.refactoring.rename;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.ide.MimeType;
import org.eclipse.che.ide.api.action.Action;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.app.CurrentProject;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.editor.EditorPartPresenter;
import org.eclipse.che.ide.api.project.node.HasStorablePath;
import org.eclipse.che.ide.api.project.tree.VirtualFile;
import org.eclipse.che.ide.api.selection.Selection;
import org.eclipse.che.ide.api.selection.SelectionAgent;
import org.eclipse.che.ide.ext.java.client.JavaLocalizationConstant;
import org.eclipse.che.ide.ext.java.client.project.node.JavaFileNode;
import org.eclipse.che.ide.ext.java.client.project.node.PackageNode;
import org.eclipse.che.ide.ext.java.client.refactoring.RefactorInfo;
import org.eclipse.che.ide.ext.java.client.refactoring.move.RefactoredItemType;
import org.eclipse.che.ide.ext.java.client.refactoring.rename.wizard.RenamePresenter;
import org.eclipse.che.ide.jseditor.client.texteditor.TextEditor;

import java.util.List;

import static org.eclipse.che.ide.ext.java.client.refactoring.move.RefactoredItemType.COMPILATION_UNIT;
import static org.eclipse.che.ide.ext.java.client.refactoring.move.RefactoredItemType.PACKAGE;

/**
 * Action for launch rename refactoring of java files
 *
 * @author Alexander Andrienko
 * @author Valeriy Svydenko
 */
@Singleton
public class RenameRefactoringAction extends Action {

    private final EditorAgent           editorAgent;
    private final RenamePresenter       renamePresenter;
    private final SelectionAgent        selectionAgent;
    private final JavaRefactoringRename javaRefactoringRename;
    private final AppContext            appContext;

    private RefactoredItemType renamedItemType;
    private List<?>            selectedItems;

    @Inject
    public RenameRefactoringAction(EditorAgent editorAgent,
                                   RenamePresenter renamePresenter,
                                   JavaLocalizationConstant locale,
                                   SelectionAgent selectionAgent,
                                   JavaRefactoringRename javaRefactoringRename,
                                   AppContext appContext) {
        super(locale.renameRefactoringActionName(), locale.renameRefactoringActionDescription());
        this.editorAgent = editorAgent;
        this.renamePresenter = renamePresenter;
        this.selectionAgent = selectionAgent;
        this.javaRefactoringRename = javaRefactoringRename;
        this.appContext = appContext;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (selectedItems != null) {
            RefactorInfo refactorInfo = RefactorInfo.of(renamedItemType, selectedItems);
            renamePresenter.show(refactorInfo);
            return;
        }

        final EditorPartPresenter editorPart = editorAgent.getActiveEditor();
        final TextEditor textEditorPresenter;

        if (editorPart instanceof TextEditor) {
            textEditorPresenter = (TextEditor)editorPart;
        } else {
            return;
        }
        javaRefactoringRename.refactor(textEditorPresenter);
    }

    @Override
    public void update(ActionEvent event) {
        Selection<?> selections = selectionAgent.getSelection();
        if (selections != null) {
            List<?> selectedItems = selections.getAllElements();
            this.selectedItems = selectedItems;

            if (selectedItems.size() != 1) {
                event.getPresentation().setEnabled(false);
                return;
            }

            Object selectedItem = selectedItems.get(0);

            if (!(selectedItem instanceof HasStorablePath)) {
                event.getPresentation().setEnabled(false);
                return;
            }

            HasStorablePath item = (HasStorablePath)selectedItem;

            boolean isSourceFileNode = item instanceof JavaFileNode;
            boolean isPackageNode = item instanceof PackageNode;

            if (isSourceFileNode) {
                renamedItemType = COMPILATION_UNIT;
                event.getPresentation().setEnabled(true);
                return;
            }

            if (isPackageNode) {
                renamedItemType = PACKAGE;
                event.getPresentation().setEnabled(true);
                return;
            }
        }

        selectedItems = null;

        CurrentProject currentProject = appContext.getCurrentProject();
        if (currentProject == null) {
            event.getPresentation().setEnabled(false);
            return;
        }

        //todo Warning:we need improve this code for multi-module projects
        final List<String> language = currentProject.getRootProject().getAttributes().get("language");
        if (language == null || !language.contains("java")) {
            event.getPresentation().setEnabled(false);
            return;
        }

        EditorPartPresenter editorPart = editorAgent.getActiveEditor();
        if (editorPart != null && editorPart instanceof TextEditor && ((TextEditor)editorPart).isFocused()) {
            VirtualFile virtualFile = editorPart.getEditorInput().getFile();
            String mediaType = virtualFile.getMediaType();

            if (mediaType != null && ((mediaType.equals(MimeType.TEXT_X_JAVA) ||
                    mediaType.equals(MimeType.TEXT_X_JAVA_SOURCE) ||
                    mediaType.equals(MimeType.APPLICATION_JAVA_CLASS)))) {
                event.getPresentation().setEnabled(true);
            } else {
                event.getPresentation().setEnabled(false);
            }
        } else {
            event.getPresentation().setEnabled(false);
        }
    }
}
