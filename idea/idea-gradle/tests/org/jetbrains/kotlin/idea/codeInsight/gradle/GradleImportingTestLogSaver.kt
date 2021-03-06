/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.codeInsight.gradle

import com.intellij.openapi.diagnostic.Logger
import com.intellij.testFramework.TestLoggerFactory

class GradleImportingTestLogSaver() {
    private val persistedLoggerFactory: Logger.Factory? = Logger.getFactory()

    init {
        Logger.setFactory(TestLoggerFactory::class.java)
    }

    fun restore() {
        if (persistedLoggerFactory != null) {
            Logger.setFactory(persistedLoggerFactory)
        }
    }
}