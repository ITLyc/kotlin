/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.ir.builders.declarations

import org.jetbrains.kotlin.backend.common.descriptors.WrappedValueParameterDescriptor
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.impl.IrValueParameterImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.IrType

const val UNDEFINED_PARAMETER_INDEX = -1

class IrValueParameterBuilder : IrDeclarationBuilder() {
    lateinit var type: IrType

    var index: Int = UNDEFINED_PARAMETER_INDEX
    var varargElementType: IrType? = null
    var isCrossInline = false
    var isNoinline = false

    fun updateFrom(from: IrValueParameter) {
        super.updateFrom(from)

        type = from.type
        index = from.index
        varargElementType = from.varargElementType
        isCrossInline = from.isCrossinline
        isNoinline = from.isNoinline
    }
}

fun IrValueParameterBuilder.build(): IrValueParameter {
    val wrappedDescriptor = WrappedValueParameterDescriptor()
    return IrValueParameterImpl(
        startOffset, endOffset, origin,
        IrValueParameterSymbolImpl(wrappedDescriptor),
        name, index, type, varargElementType, isCrossInline, isNoinline
    ).also {
        wrappedDescriptor.bind(it)
    }
}

inline fun buildValueParameter(b: IrValueParameterBuilder.() -> Unit): IrValueParameter =
    IrValueParameterBuilder().run {
        b()
        build()
    }

inline fun IrFunction.addValueParameter(b: IrValueParameterBuilder.() -> Unit): IrValueParameter =
    IrValueParameterBuilder().run {
        b()
        if (index == UNDEFINED_PARAMETER_INDEX) {
            index = valueParameters.size
        }
        build().also { valueParameter ->
            valueParameters.add(valueParameter)
            valueParameter.parent = this@addValueParameter
        }
    }