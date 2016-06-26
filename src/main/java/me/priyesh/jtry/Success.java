/*
 * Copyright (c) Priyesh Patel 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.priyesh.jtry;

import me.priyesh.jtry.functions.Function1;
import me.priyesh.jtry.functions.Supplier;
import me.priyesh.jtry.functions.UFunction1;

public final class Success<A> extends Try<A> {

  Success(A a) {
    super(a);
  }

  public final boolean isFailure() {
    return false;
  }

  public final boolean isSuccess() {
    return true;
  }

  public final A getOrElse(Supplier<A> defaultValue) {
    return get();
  }

  public Try<A> orElse(Supplier<Try<A>> defaultValue) {
    return this;
  }

  public final A get() {
    return value;
  }

  public final <B> Try<B> flatMap(UFunction1<A, Try<B>> f) {
    try {
      return f.apply(get());
    } catch (Throwable throwable) {
      return new Failure<>(throwable);
    }
  }

  public final <B> Try<B> map(Function1<A, B> f) {
    return Try.of(() -> f.apply(get()));
  }

  public <B> void foreach(Function1<A, B> f) {
    f.apply(value);
  }

  public Try<A> recover(UFunction1<Throwable, A> f) {
    return this;
  }
}