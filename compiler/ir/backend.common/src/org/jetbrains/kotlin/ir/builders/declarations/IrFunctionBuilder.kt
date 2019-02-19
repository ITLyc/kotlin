/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.ir.builders.declarations

import org.jetbrains.kotlin.backend.common.descriptors.WrappedClassConstructorDescriptor
import org.jetbrains.kotlin.backend.common.descriptors.WrappedSimpleFunctionDescriptor
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.impl.IrConstructorImpl
import org.jetbrains.kotlin.ir.declarations.impl.IrFunctionImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrConstructorSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrSimpleFunctionSymbolImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.impl.IrUninitializedType
import org.jetbrains.kotlin.name.Name

class IrFunctionBuilder : IrDeclarationBuilder() {

    var isInline: Boolean = false
    var isExternal: Boolean = false

    var returnType: IrType = IrUninitializedType

    var modality: Modality = Modality.FINAL
    var isTailrec: Boolean = false
    var isSuspend: Boolean = false

    var isPrimary: Boolean = false

    fun updateFrom(from: IrFunction) {
        super.updateFrom(from)

        isInline = from.isInline
        isExternal = from.isExternal

        if (from is IrSimpleFunction) {
            modality = from.modality
            isTailrec = from.isTailrec
            isSuspend = from.isSuspend
        } else {
            modality = Modality.FINAL
            isTailrec = false
            isSuspend = false
        }

        if (from is IrConstructor) {
            isPrimary = from.isPrimary
        }
    }
}

fun IrFunctionBuilder.buildFun(): IrSimpleFunction {
    val wrappedDescriptor = WrappedSimpleFunctionDescriptor()
    return IrFunctionImpl(
        startOffset, endOffset, origin,
        IrSimpleFunctionSymbolImpl(wrappedDescriptor),
        name, visibility, modality, returnType,
        isInline = isInline, isExternal = isExternal, isTailrec = isTailrec, isSuspend = isSuspend
    ).also {
        wrappedDescriptor.bind(it)
    }
}

fun IrFunctionBuilder.buildConstructor(): IrConstructor {
    val wrappedDescriptor = WrappedClassConstructorDescriptor()
    return IrConstructorImpl(
        startOffset, endOffset, origin,
        IrConstructorSymbolImpl(wrappedDescriptor),
        Name.special("<init>"),
        visibility, returnType,
        isInline = isInline, isExternal = isExternal, isPrimary = isPrimary
    ).also {
        wrappedDescriptor.bind(it)
    }
}

inline fun buildFun(b: IrFunctionBuilder.() -> Unit): IrSimpleFunction =
    IrFunctionBuilder().run {
        b()
        buildFun()
    }

inline fun buildConstructor(b: IrFunctionBuilder.() -> Unit): IrConstructor =
    IrFunctionBuilder().run {
        b()
        buildConstructor()
    }