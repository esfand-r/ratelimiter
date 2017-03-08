package ratelimiter

import scala.compat.Platform

/**
 * Created by esfandiaramirrahimi on 15-11-03.
 */
case class TimeSlicer(rateDurationSecond: Long, sliceCount: Int) {
  val rateDurationMillisecond: Long = rateDurationSecond * 1000
  val sliceDurationMillisecond: Long = (rateDurationSecond / sliceCount) * 1000

  def getCurrentSliceKey: String = {
    val now: Long = Platform.currentTime
    val currentTime = now / rateDurationMillisecond
    val currentBucket = (now / sliceDurationMillisecond) % sliceCount
    currentTime + "_" + currentBucket
  }

  def getSliceKeys: List[String] = {
    val now: Long = Platform.currentTime
    val currentSliceTime: Long = now / rateDurationMillisecond
    val currentBucketTime: Long = (now / sliceDurationMillisecond) % sliceCount
    generateKeys(List(), currentSliceTime, currentBucketTime)
  }

  def generateKeys(list: List[String], currentSliceTime: Long, currentBucketTime: Long): List[String] = {
    if (list.size == this.sliceCount) {
      return list
    }

    val newList = currentSliceTime + "_" + currentBucketTime :: list

    if (currentBucketTime == 0) {
      generateKeys(newList, currentSliceTime - 1, sliceCount - 1)
    } else {
      generateKeys(newList, currentSliceTime, currentBucketTime - 1)
    }
  }
}
