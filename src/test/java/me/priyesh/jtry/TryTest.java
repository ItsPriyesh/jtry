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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TryTest {

  @Test
  public void testCatchExceptionAndLiftToFailure() {
    RuntimeException ex = new RuntimeException();
    Try<Integer> t = Try.of(() -> { throw ex; });
    assertThat(t, instanceOf(Failure.class));
    try {
      t.get();
      fail();
    } catch (Exception e) { assertEquals(ex, e); }
  }

  @Test
  public void testLiftToSuccess() {
    assertThat(Try.of(() -> 1), instanceOf(Success.class));
  }

  @Test
  public void testGet_Failure() {
    RuntimeException ex = new RuntimeException();
    Try<Integer> t = new Failure<>(ex);
    try {
      t.get();
      fail();
    } catch (Exception e) { assertEquals(ex, e); }
  }

  @Test
  public void testGet_Success() {
    assertEquals(new Integer(1), new Success<>(1).get());
  }

  @Test
  public void testGetOrElse_Failure() {
    Try<Integer> t = new Failure<>(new Exception());
    assertEquals(new Integer(1), t.getOrElse(1));
  }

  @Test
  public void testGetOrElse_Success() {
    Try<Integer> t = new Success<>(1);
    assertEquals(new Integer(1), t.getOrElse(null));
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
    try {
      t.get();
      fail();
    } catch (Exception e) { assertEquals(ex, e); }
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
    try {
      t.get();
      fail();
    } catch (Exception e) { assertEquals(first, e); }
  }

  @Test
  public void testMap_Exception_Success() {
    RuntimeException ex = new RuntimeException();
    Try<Integer> t = new Success<>(1).map(i -> { throw ex; });
    assertThat(t, instanceOf(Failure.class));
    try {
      t.get();
      fail();
    } catch (Exception e) { assertEquals(ex, e); }
  }

  @Test
  public void testFlatMap_NoException_Failure() {
    RuntimeException ex = new RuntimeException();
    Try<Integer> t = new Failure<Integer>(ex).flatMap(i -> new Success<>(i + 1));
    assertThat(t, instanceOf(Failure.class));
    try {
      t.get();
      fail();
    } catch (Exception e) { assertEquals(ex, e); }
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
    try {
      t.get();
      fail();
    } catch (Exception e) { assertEquals(ex, e); }
  }

  @Test
  public void testFlatMap_Exception_Success() {
    RuntimeException ex = new RuntimeException();
    Try<Integer> t = new Success<>(1).flatMap(i -> { throw ex; });
    assertThat(t, instanceOf(Failure.class));
    try {
      t.get();
      fail();
    } catch (Exception e) { assertEquals(ex, e); }
  }
}