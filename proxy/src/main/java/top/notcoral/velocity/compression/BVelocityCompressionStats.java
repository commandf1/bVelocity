/*
 * Copyright (C) 2018-2023 Velocity Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.notcoral.velocity.compression;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Tracks aggregate proxy compression statistics.
 */
public final class BVelocityCompressionStats {

  public static final BVelocityCompressionStats INSTANCE = new BVelocityCompressionStats();

  private final LongAdder totalPackets = new LongAdder();
  private final LongAdder compressedPackets = new LongAdder();
  private final LongAdder passthroughPackets = new LongAdder();
  private final LongAdder totalRawBytes = new LongAdder();
  private final LongAdder totalEncodedBytes = new LongAdder();
  private final LongAdder compressedRawBytes = new LongAdder();
  private final LongAdder compressedEncodedBytes = new LongAdder();
  private final AtomicLong resetNanoTime = new AtomicLong(System.nanoTime());

  private BVelocityCompressionStats() {
  }

  public void recordCompressed(int rawBytes, int encodedBytes) {
    record(rawBytes, encodedBytes, true);
  }

  public void recordPassThrough(int rawBytes, int encodedBytes) {
    record(rawBytes, encodedBytes, false);
  }

  private void record(int rawBytes, int encodedBytes, boolean compressed) {
    totalPackets.increment();
    totalRawBytes.add(rawBytes);
    totalEncodedBytes.add(encodedBytes);
    if (compressed) {
      compressedPackets.increment();
      compressedRawBytes.add(rawBytes);
      compressedEncodedBytes.add(encodedBytes);
    } else {
      passthroughPackets.increment();
    }
  }

  public void reset() {
    totalPackets.reset();
    compressedPackets.reset();
    passthroughPackets.reset();
    totalRawBytes.reset();
    totalEncodedBytes.reset();
    compressedRawBytes.reset();
    compressedEncodedBytes.reset();
    resetNanoTime.set(System.nanoTime());
  }

  public Snapshot snapshot() {
    return new Snapshot(
        resetNanoTime.get(),
        totalPackets.sum(),
        compressedPackets.sum(),
        passthroughPackets.sum(),
        totalRawBytes.sum(),
        totalEncodedBytes.sum(),
        compressedRawBytes.sum(),
        compressedEncodedBytes.sum()
    );
  }

  public record Snapshot(
      long resetNanoTime,
      long totalPackets,
      long compressedPackets,
      long passthroughPackets,
      long totalRawBytes,
      long totalEncodedBytes,
      long compressedRawBytes,
      long compressedEncodedBytes
  ) {

    public long elapsedSeconds() {
      return (System.nanoTime() - resetNanoTime) / 1_000_000_000L;
    }
  }
}
