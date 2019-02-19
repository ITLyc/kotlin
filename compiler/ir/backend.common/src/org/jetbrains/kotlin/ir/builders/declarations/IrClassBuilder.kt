/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.ir.builders.declarations

import org.jetbrains.kotlin.backend.common.descriptors.WrappedClassDescriptor
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.impl.IrClassImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrClassSymbolImpl

class IrClassBuilder : IrDeclarationBuilder() {
    var kind: ClassKind = ClassKind.CLASS
    var modality: Modality = Modality.FINAL

    var isCompanion: Boolean = false
    var isInner: Boolean = false
    var isData: Boolean = false
    var isExternal: Boolean = false
    var isInline: Boolean = false

    fun updateFrom(from: IrClass) {
        super.updateFrom(from)

        kind = from.kind
        modality = from.modality
        isCompanion = from.isCompanion
        isInner = from.isInner
        isData = from.isData
        isExternal = from.isExternal
        isInline = from.isInline
    }
}

fun IrClassBuilder.buildClass(): IrClass {
    val wrappedDescriptor = WrappedClassDescriptor()
    return IrClassImpl(
        startOffset, endOffset, origin,
        IrClassSymbolImpl(wrappedDescriptor),
        name, kind, visibility, modality, isCompanion, isInner, isData, isExternal, isInline
    ).also {
        wrappedDescriptor.bind(it)
    }
}

inline fun buildClass(b: IrClassBuilder.() -> Unit) =
    IrClassBuilder().run {
        b()
        buildClass()
    }