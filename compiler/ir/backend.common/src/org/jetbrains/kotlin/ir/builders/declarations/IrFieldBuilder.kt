/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.ir.builders.declarations

import org.jetbrains.kotlin.backend.common.descriptors.WrappedFieldDescriptor
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.impl.IrFieldImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrFieldSymbolImpl
import org.jetbrains.kotlin.ir.types.IrType

class IrFieldBuilder : IrDeclarationBuilder() {

    lateinit var type: IrType

    var isFinal: Boolean = false
    var isExternal: Boolean = false
    var isStatic: Boolean = false

    fun updateFrom(from: IrField) {
        super.updateFrom(from)

        type = from.type
        isFinal = from.isFinal
        isExternal = from.isExternal
        isStatic = from.isStatic
    }
}

fun IrFieldBuilder.buildField(): IrField {
    val wrappedDescriptor = WrappedFieldDescriptor()
    return IrFieldImpl(
        startOffset, endOffset, origin,
        IrFieldSymbolImpl(wrappedDescriptor),
        name, type, visibility, isFinal, isExternal, isStatic
    ).also {
        wrappedDescriptor.bind(it)
    }
}

inline fun buildField(b: IrFieldBuilder.() -> Unit) =
    IrFieldBuilder().run {
        b()
        buildField()
    }
