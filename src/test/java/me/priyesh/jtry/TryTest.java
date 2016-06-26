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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static me.priyesh.jtry.TestUtils.assertThrows;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TryTest {

  @Test
  public void testCatchExceptionAndLiftToFailure() {
    RuntimeException ex = new RuntimeException();
    Try<Integer> t = Try.of(() -> { throw ex; });
    assertThat(t, instanceOf(Failure.class));
    assertThrows(ex, t::get);
  }

  @Test
  public void testLiftToSuccess() {
    assertThat(Try.of(() -> 1), instanceOf(Success.class));
  }

  @Test
  public void testGet_Failure() {
    RuntimeException ex = new RuntimeException();
    Try<Integer> t = new Failure<>(ex);
    assertThrows(ex, t::get);
  }

  @Test
  public void testGet_Success() {
    assertEquals(new Integer(1), new Success<>(1).get());
  }

  @Test
  public void testGetOrElse_Failure() {
    Try<Integer> t = new Failure<>(new Exception());
    assertEquals(new Integer(1), t.getOrElse(() -> 1));
  }

  @Test
  public void testGetOrElse_Success() {
    Try<Integer> t = new Success<>(1);
    assertEquals(new Integer(1), t.getOrElse(null));
  }

  @Test
  public void testOrElse_Failure() {
    Try<Integer> t = new Failure<>(new Exception());
    Try<Integer> def = Try.of(() -> 1);
    assertEquals(def, t.orElse(() -> def));
  }

  @Test
  public void testOrElse_Success() {
    Try<Integer> t = new Success<>(1);
    assertEquals(t, t.orElse(() -> Try.of(() -> 2)));
  }

  @Test
  public void testIsFailure_Failure() {
    Try<Integer> t = new Failure<>(new Exception());
    assertTrue(t.isFailure());
    assertFalse(t.isSuccess());
  }

  @Test
  public void testIsSuccess_Success() {
    Try<Integer> t = new Success<>(1);
    assertTrue(t.isSuccess());
    assertFalse(t.isFailure());
  }

  @Test
  public void testMap_NoException_Failure() {
    RuntimeException ex = new RuntimeException();
    Try<String> t = new Failure<>(ex).map(String::valueOf);
    assertThat(t, instanceOf(Failure.class));
    assertThrows(ex, t::get);
  }

  @Test
  public void testMap_NoException_Success() {
    Try<String> t = new Success<>(1).map(String::valueOf);
    assertThat(t, instanceOf(Success.class));
    assertEquals("1", t.get());
  }

  @Test
  public void testMap_Exception_Failure() {
    RuntimeException first = new RuntimeException();
    RuntimeException second = new RuntimeException();
    Try<Integer> t = new Failure<>(first).map(i -> { throw second; });
    assertThat(t, instanceOf(Failure.class));
    assertThrows(first, t::get);
  }

  @Test
  public void testMap_Exception_Success() {
    RuntimeException ex = new RuntimeException();
    Try<Integer> t = new Success<>(1).map(i -> { throw ex; });
    assertThat(t, instanceOf(Failure.class));
    assertThrows(ex, t::get);
  }

  @Test
  public void testFlatMap_NoException_Failure() {
    RuntimeException ex = new RuntimeException();
    Try<Integer> t = new Failure<Integer>(ex).flatMap(i -> new Success<>(i + 1));
    assertThat(t, instanceOf(Failure.class));
    assertThrows(ex, t::get);
  }

  @Test
  public void testFlatMap_NoException_Success() {
    Try<Integer> t = new Success<>(1).flatMap(i -> new Success<>(i + 1));
    assertThat(t, instanceOf(Success.class));
    assertEquals(new Integer(2), t.get());
  }


  @Test
  public void testFlatMap_Exception_Failure() {
    RuntimeException ex = new RuntimeException();
    Try<Integer> t = new Failure<>(ex).flatMap(i -> { throw ex; });
    assertThat(t, instanceOf(Failure.class));
    assertThrows(ex, t::get);
  }

  @Test
  public void testFlatMap_Exception_Success() {
    RuntimeException ex = new RuntimeException();
    Try<Integer> t = new Success<>(1).flatMap(i -> { throw ex; });
    assertThat(t, instanceOf(Failure.class));
    assertThrows(ex, t::get);
  }

  @Test
  public void testForeach_Failure() {
    final int[] a = {0};
    Try<Integer> t = new Failure<>(new Exception());
    t.foreach(i -> a[0] += 1);
    assertEquals(0, a[0]);
  }

  @Test
  public void testForeach_Success() {
    final int[] a = {0};
    Try<Integer> t = new Success<>(1);
    t.foreach(i -> a[0] += i);
    assertEquals(1, a[0]);
  }

  @Test
  public void testRecover_Failure() {
    Try<Integer> t = new Failure<>(new Exception());
    Try<Integer> recovered = t.recover(throwable -> 1);
    assertEquals(new Integer(1), recovered.get());
  }

  @Test
  public void testRecover_Success() {
    Try<Integer> t = new Success<>(1);
    Try<Integer> recovered = t.recover(throwable -> 2);
    assertEquals(t, recovered);
  }

  @Test
  public void testMatch_Failure() {
    RuntimeException ex = new RuntimeException();
    new Failure<Integer>(ex).match(i -> fail(), e -> assertEquals(ex, e));
  }

  @Test
  public void testMatch_Success() {
    new Success<>(1).match(i -> assertEquals(new Integer(1), i), e -> fail());
  }
}