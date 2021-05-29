/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ml4j.autograd.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import org.ml4j.autograd.AutogradValue;
import org.ml4j.autograd.BackwardConfig;
import org.ml4j.autograd.node.Node;
import org.ml4j.autograd.node.ValueNode;

/**
 * Default implementation of Node.
 * 
 * @author Michael Lavelle
 *
 * @param <V> The type of AutogradValue associated with this Node
 */
public class NodeImpl<V extends AutogradValue<V, ?, ?>> implements ValueNode<V> {

    protected Supplier<V> value;
    protected List<Node<?>> prev;
    protected BiConsumer<V, BackwardConfig> wrapBackward;

    public NodeImpl(Supplier<V> value) {
        this.value = value;
        this.prev = new ArrayList<>();
    }

    public NodeImpl(Supplier<V> value, List<Node<?>> children) {
        this.value = value;
        this.prev = children;
    }

    @Override
    public String toString() {
        if (prev != null && prev.size() > 0) {
            return "NodeImpl [" + hashCode() + ":" + "value=" + value.get().data().get() + ", prev=" + prev + "]";
        } else {
            return "NodeImpl [" + hashCode() + ":" + value.get().data().get() + "]";
        }
    }

    @Override
    public Supplier<V> getValue() {
        return value;
    }

    @Override
    public void backward(BackwardConfig config) {
        if (wrapBackward != null) {
            wrapBackward.accept(this.getValue().get(), config);
        }
    }

    @Override
    public List<Node<?>> prev() {
        return prev;
    }

    @Override
    public void setBackwardFunction(BiConsumer<V, BackwardConfig> wrapBackward) {
        this.wrapBackward = wrapBackward;
    }
}