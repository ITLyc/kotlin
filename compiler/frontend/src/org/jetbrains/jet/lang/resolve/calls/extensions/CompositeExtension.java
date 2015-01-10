/*
 * Copyright 2010-2014 JetBrains s.r.o.
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

package org.jetbrains.jet.lang.resolve.calls.extensions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.descriptors.CallableDescriptor;
import org.jetbrains.jet.lang.resolve.calls.context.BasicCallResolutionContext;
import org.jetbrains.jet.lang.resolve.calls.model.ResolvedCall;

import java.util.List;

public class CompositeExtension implements CallResolverExtension {

    private final List<CallResolverExtension> extensions;

    public CompositeExtension(@NotNull List<CallResolverExtension> extensions) {
        this.extensions = extensions;
    }

    @Override
    public <F extends CallableDescriptor> void run(
            @NotNull ResolvedCall<F> resolvedCall,
            @NotNull BasicCallResolutionContext context
    ) {
        for (CallResolverExtension resolverExtension : extensions) {
            resolverExtension.run(resolvedCall, context);
        }
    }
}
