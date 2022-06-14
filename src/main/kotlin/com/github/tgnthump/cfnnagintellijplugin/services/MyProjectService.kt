package com.github.tgnthump.cfnnagintellijplugin.services

import com.intellij.openapi.project.Project
import com.github.tgnthump.cfnnagintellijplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
