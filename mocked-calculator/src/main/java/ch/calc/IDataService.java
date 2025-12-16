package ch.calc;

import jakarta.annotation.Nonnull;

public interface IDataService {
  boolean open(String path);

  void close();

  boolean getFirst(@Nonnull final IntHolder value);

  boolean getNext(@Nonnull final IntHolder value);
}