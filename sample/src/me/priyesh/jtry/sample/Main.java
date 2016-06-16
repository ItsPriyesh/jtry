package me.priyesh.jtry.sample;

import me.priyesh.jtry.Try;

import java.util.Random;

public class Main {
  public static void main(String[] args) {
    // Before
    try {
      Degree degree = graduateSchool();
      System.out.println(degree.findJob());
    } catch (FailedException e) {
      System.out.println("Unemployed");
    }

    // After
    String job = Try.of(Main::graduateSchool).getOrElse(() -> "Unemployed").findJob();
    System.out.println(job);
  }

  interface Degree {
    String findJob();
  }

  static class FailedException extends RuntimeException {}

  private static Degree graduateSchool() {
    int average = 30 + new Random().nextInt(70);
    if (average > 90) return () -> "Google";
    if (average > 60) return () -> "McDonald's";
    else throw new FailedException();
  }
}
