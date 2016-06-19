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

public abstract class Try<A> {

  /**
   * Returns the value from this Try if it's a Success, or throws the exception if it's a Failure.
   * <p>
   * Note that if the Failure encapsulates a checked exception, it will be given wrapped in a RuntimeException.
   * This allows unchecked calls to get().
   */
  public abstract <E extends RuntimeException> A get() throws E;

  /**
   * Returns this Try if it's a Success or the given default value if it's a Failure.
   */
  public abstract A getOrElse(A defaultValue);

  /**
   * Returns the given function applied to the value from this Success or returns this if this is a Failure.
   */
  public abstract <B> Try<B> flatMap(Function1<A, Try<B>> f);

  /**
   * Maps the given function to the value from this Success or returns this if this is a Failure.
   */
  public abstract <B> Try<B> map(Function1<A, B> f);

  /**
   * Returns true if the Try is a Failure, false otherwise.
   */
  public abstract boolean isFailure();

  /**
   * Returns true if the Try is a Success, false otherwise.
   */
  public abstract boolean isSuccess();

  protected final A value;

  Try(A a) {
    this.value = a;
  }

  public static <A> Try<A> of(Function0<A> f) {
    try {
      return new Success<>(f.apply());
    } catch (Throwable throwable) {
      return new Failure<>(throwable);
    }
  }

  public void match(Action<A> onSuccess, Action<RuntimeException> onFailure) {
    if (this instanceof Success) {
      onSuccess.call(get());
    } else if (this instanceof Failure) {
      try { get(); } catch (Exception e) {
        onFailure.call((RuntimeException) e);
      }
    }
  }
}
