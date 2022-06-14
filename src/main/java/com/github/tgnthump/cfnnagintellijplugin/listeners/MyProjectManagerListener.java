package com.github.tgnthump.cfnnagintellijplugin.listeners;

import com.github.tgnthump.cfnnagintellijplugin.services.MyProjectService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

public class MyProjectManagerListener implements ProjectManagerListener {

    @Override
    public void projectOpened(@NotNull Project project) {
        project.getService(MyProjectService.class);
    }
}
