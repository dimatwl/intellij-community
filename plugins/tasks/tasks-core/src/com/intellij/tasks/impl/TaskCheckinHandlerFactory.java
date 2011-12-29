/*
 * Copyright 2000-2011 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.tasks.impl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory;
import com.intellij.tasks.TaskManager;
import com.intellij.tasks.context.WorkingContextManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Dmitry Avdeev
 *         Date: 12/29/11
 */
public class TaskCheckinHandlerFactory extends CheckinHandlerFactory {

  @NotNull
  @Override
  public CheckinHandler createHandler(final CheckinProjectPanel panel, final CommitContext commitContext) {
    return new CheckinHandler() {
      @Override
      public void checkinSuccessful() {
        final String message = panel.getCommitMessage();
        if (message != null) {
          final Project project = panel.getProject();
          TaskManagerImpl manager = (TaskManagerImpl)TaskManager.getManager(project);
          if (manager.getState().saveContextOnCommit) {
            SwingUtilities.invokeLater(new Runnable() {
              @Override
              public void run() {
                WorkingContextManager.getInstance(project).saveContext(null, message);
              }
            });
          }
        }
      }
    };
  }
}